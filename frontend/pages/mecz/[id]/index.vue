<script setup lang="ts">
import type { ApiError } from '~/types/apierrror';
import type { ApplicationUserBasic } from '~/types/applicationuser';
import type { Match, Tournament } from '~/types/tournament';

const route = useRoute();
const config = useRuntimeConfig();
const authStatus = useAuthStatus();

const apiError = ref(null as ApiError | null);
const editMode = ref(false);

const { data: match } = await useFetch<Match>(`${config.public.BACKEND_API}/match/${route.params.id}`, {
  key: 'match'
});
const { data, error } = useAsyncData('data', async () => {
  const [firstPlayer, secondPlayer, lastModifiedBy, tournament] = await Promise.all([
    match.value?.firstPlayerId
      ? $fetch<ApplicationUserBasic>(`${config.public.BACKEND_API}/user/${match.value?.firstPlayerId}`)
      : null,
    match.value?.secondPlayerId
      ? $fetch<ApplicationUserBasic>(`${config.public.BACKEND_API}/user/${match.value?.secondPlayerId}`)
      : null,
    match.value?.lastModifiedById
      ? $fetch<ApplicationUserBasic>(`${config.public.BACKEND_API}/user/${match.value.lastModifiedById}`)
      : null,
    match.value?.tournamentId
      ? $fetch<Tournament>(`${config.public.BACKEND_API}/tournament/${match.value.tournamentId}`)
      : null
  ]);
  return { firstPlayer, secondPlayer, lastModifiedBy, tournament };
});

const setsToWin = computed(() => data.value?.tournament?.setsToWin ?? 2);

watchEffect(() => {
  if (match.value && !match.value.result) {
    match.value.result = match.value.result ?? {
      id: -1,
      winnerId: -1,
      scratch: false,
      walkover: false,
      setResults: Array.from({ length: 3 }, () => ({ gamesScored: [0, 0] })),
      setsScored: [0, 0]
    };
  }
});

watchEffect(() => {
  if (error.value) apiError.value = fetchErrorToApiError(error.value);
});

const canEdit = computed(() => {
  return [match.value?.firstPlayerId, match.value?.secondPlayerId, data.value?.tournament?.organizerId].includes(
    authStatus.value.applicationUserId
  );
});

function onEditModeChange() {
  if (editMode.value) save();
  else editMode.value = true;
}

async function save() {
  if (!match.value) return;
  if (!isResultCorrect(match.value, setsToWin.value)) return;
  try {
    const res = await $fetch<Match>(`${config.public.BACKEND_API}/match/${match.value.id}`, {
      method: 'PATCH',
      body: { result: match.value.result },
      credentials: 'include'
    });
    editMode.value = false;
    apiError.value = null;
    match.value = res;
    refreshNuxtData('data');
  } catch (error) {
    apiError.value = fetchErrorToApiError(error);
  }
}
</script>
<template>
  <div class="page__margin max-md:text-md" v-if="match">
    <h1 class="mt-8 text-2xl font-bold">
      {{ nameFromApplicationUser(data?.firstPlayer) }} vs {{ nameFromApplicationUser(data?.secondPlayer) }}
    </h1>
    <h2 v-if="data?.tournament" class="text-lg font-semibold">Rozgrywki: {{ data.tournament.name }}</h2>
    <div>
      <span v-if="match.result" class="max-md:text-base md:text-lg">
        Wynik: {{ match.result.setsScored[0] }}:{{ match.result.setsScored[1] }}
      </span>
      <span class="">
        Zwycięzca:
        <span v-if="match.result?.winnerId === match.firstPlayerId">{{
          nameFromApplicationUser(data?.firstPlayer)
        }}</span>
        <span v-else-if="match.result?.winnerId === match.secondPlayerId">{{
          nameFromApplicationUser(data?.secondPlayer)
        }}</span>
        <span v-else>brak</span>
      </span>
      <div class="flex items-start">
        <MatchResultEditor
          :match="match"
          :first-player="data?.firstPlayer ?? null"
          :second-player="data?.secondPlayer ?? null"
          :editMode="editMode"
          class="md:mr-1"
        />
        <button
          v-if="canEdit"
          class="-mr-2 -mt-2 p-2 hover:text-olive-700 active:text-olive-400 disabled:cursor-not-allowed disabled:text-red-500"
          :title="editMode ? 'Zapisz' : 'Edytuj'"
          @click="onEditModeChange"
          :disabled="editMode && !isResultCorrect(match, setsToWin)"
        >
          <font-awesome-icon v-if="!editMode" icon="fa-solid fa-file-pen" size="xl" />
          <font-awesome-icon v-else :icon="['fas', 'floppy-disk']" size="xl" />
        </button>
        <div
          v-else-if="authStatus.loggedIn"
          title="Edytować mogą tylko zawodnicy, lub organizator"
          class="cursor-not-allowed text-gray-300"
        >
          <font-awesome-icon icon="fa-solid fa-file-pen" size="xl" /> Edytować mogą tylko zawodnicy, lub organizator
        </div>
      </div>

      <div class="flex flex-col" v-if="match.result">
        Ostatnia modyfikacja:
        <span>
          <font-awesome-icon :icon="['fas', 'user']" />
          {{ nameFromApplicationUser(data?.lastModifiedBy) }}
        </span>
        <span>
          <font-awesome-icon :icon="['fas', 'clock']" />
          {{ new Date(match.updatedDateTime).toLocaleDateString() }}
          {{ new Date(match.updatedDateTime).toLocaleTimeString() }}
        </span>
      </div>
    </div>
    <div v-if="!authStatus.loggedIn">
      <font-awesome-icon icon="fa-solid fa-circle-info" /> Mecze mogą edytować tylko zalogowani użytkownicy
    </div>
    <ApiError :api-error="apiError" /> 
  </div>
</template>
