<script setup lang="ts">
import type { ApiError } from '~/types/apierrror';
import type { Match } from '~/types/tournament';

const route = useRoute();
const config = useRuntimeConfig();

const apiError = ref(null as ApiError | null);
const { data: match } = await useFetch<Match>(`${config.public.BACKEND_API}/match/${route.params.id}`);

if (match.value) {
  match.value.result = match.value.result ?? {
    id: -1,
    setResults: Array.from({ length: 3 }, () => ({ firstPlayerScore: 0, secondPlayerScore: 0 })),
    winnerId: -1
  };
}

const numberOfSets = ref(match.value?.result?.setResults.length ?? 0);
const matchResult = computed(() => {
  return (match.value?.result?.setResults ?? []).reduce(
    (matchResult, setResult) => [
      matchResult[0] + (setResult.firstPlayerScore > setResult.secondPlayerScore ? 1 : 0),
      matchResult[1] + (setResult.secondPlayerScore > setResult.firstPlayerScore ? 1 : 0)
    ],
    [0, 0]
  );
});
updateWinnerId();
function onNumberOfSetsChange() {
  if (numberOfSets.value > 0 && match.value) {
    while (match.value.result!.setResults.length < numberOfSets.value) {
      match.value.result!.setResults.push({ firstPlayerScore: 0, secondPlayerScore: 0 });
    }
    match.value.result!.setResults.length = numberOfSets.value;
  }
  updateWinnerId();
}
function updateWinnerId() {
  if (!match.value?.result) return;
  if (matchResult.value[0] > matchResult.value[1]) match.value.result.winnerId = match.value.firstPlayerId;
  else if (matchResult.value[0] < matchResult.value[1]) match.value.result.winnerId = match.value.secondPlayerId;
  else match.value.result.winnerId = -1;
}

async function onSave() {
  if (!match.value) return;
  try {
    const res = await $fetch(`${config.public.BACKEND_API}/match/${match.value.id}`, {
      method: 'PATCH',
      body: { result: match.value.result },
      credentials: 'include'
    });
    apiError.value = null;
  } catch (error) {
    apiError.value = fetchErrorToApiError(error);
  }
}
</script>
<template>
  {{ apiError }}
  <div class="page__margin" v-if="match">
    <h1 class="text-2xl font-bold">Edycja Meczu</h1>
    <div v-if="match.result" class="flex flex-col">
      <span class="text-lg font-bold">
        Wynik: {{ matchResult[0] }}:{{ matchResult[1] }}
        <font-awesome-icon icon="fa-solid fa-circle-info" size="sm" title="Obliczony na podstawie wyników setów" />
      </span>
      Zwycięzca: {{ match.result.winnerId }}

      <label class="mt-4">
        Ilość setów
        <select v-model="numberOfSets" @change="onNumberOfSetsChange" class="px-1.5 py-1">
          <option v-for="n in [2, 3, 4, 5]">{{ n }}</option>
        </select>
      </label>
      <div class="flex">
        <div v-for="(setResult, setNumber) in match.result.setResults" class="flex flex-col">
          Set {{ setNumber + 1 }}
          <div class="mr-3 flex">
            <input
              type="number"
              min="0"
              v-model="setResult.firstPlayerScore"
              class="w-16 rounded-l-lg border-4 border-r-2 border-atlantis-500 px-2 py-1 outline-none ring-atlantis-600 focus-visible:z-[1] focus-visible:ring-2"
              @input="updateWinnerId"
            />
            <input
              type="number"
              min="0"
              v-model="setResult.secondPlayerScore"
              class="w-16 rounded-r-lg border-4 border-l-2 border-atlantis-500 px-2 py-1 outline-none ring-atlantis-600 focus-visible:ring-2"
              @input="updateWinnerId"
            />
          </div>
        </div>
      </div>
    </div>
    <button
      class="my-4 h-10 rounded-lg bg-atlantis-600 px-2.5 py-1 text-lg font-bold text-white outline-none ring-atlantis-800 hover:bg-atlantis-700 focus-visible:ring-2 active:bg-atlantis-800"
      @click="onSave"
    >
      Zapisz
    </button>
    <ApiError :api-error="apiError" />
  </div>
</template>
