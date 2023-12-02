<script setup lang="ts">
import type { ApplicationUserBasic } from '~/types/applicationuser';
import type { Match } from '~/types/tournament';
import { calculateTieBreakerSetIndex } from '~/utils/tournament';

const props = defineProps<{
  match: Match;
  firstPlayer: ApplicationUserBasic | null;
  secondPlayer: ApplicationUserBasic | null;
  editMode: boolean;
}>();

watchEffect(() => {
  if (!props.match.result) return;
  const [firstPlayerScore, secondPlayerScore] = (props.match.result.setResults ?? []).reduce(
    (matchResult, setResult) => [
      matchResult[0] + (setResult.firstPlayerScore > setResult.secondPlayerScore ? 1 : 0),
      matchResult[1] + (setResult.secondPlayerScore > setResult.firstPlayerScore ? 1 : 0)
    ],
    [0, 0]
  );
  props.match.result.firstPlayerScore = firstPlayerScore;
  props.match.result.secondPlayerScore = secondPlayerScore;
});
watchEffect(() => {
  const { result, firstPlayerId, secondPlayerId } = props.match;
  if (!result || !firstPlayerId || !secondPlayerId) return;
  if (result.firstPlayerScore > result.secondPlayerScore) result.winnerId = firstPlayerId;
  else if (result.firstPlayerScore < result.secondPlayerScore) result.winnerId = secondPlayerId;
  else result.winnerId = -1;
});

const canAddSetResult = computed(() => props.match.result && props.match.result.setResults.length < MAX_SETS_IN_MATCH);
const canRemoveSetResult = computed(
  () => props.match.result && MIN_SETS_IN_MATCH < props.match.result.setResults.length
);
const tieBreakerSetIndex = computed(() => calculateTieBreakerSetIndex(props.match));

function addSetResult() {
  if (!canAddSetResult) return;
  props.match.result?.setResults.push({ firstPlayerScore: 0, secondPlayerScore: 0 });
}

function removeSetResult(index: number) {
  if (!canRemoveSetResult) return;
  props.match.result?.setResults.splice(index, 1);
}
</script>
<template>
  <div class="table__scrollbar table__scrollbar--champagne overflow-x-auto max-md:text-sm">
    <table class="h-[4.5rem]" v-if="match.result">
      <tr>
        <td
          class="border bg-champagne-300 px-2 py-1"
          :class="{ 'font-bold': match.result.winnerId === match.firstPlayerId }"
        >
          {{ nameFromApplicationUser(firstPlayer) }}
        </td>
        <td
          class="w-9 border px-1 py-1"
          v-for="(setResult, index) in match.result.setResults"
          :class="[
            'text-center',
            isSetResultCorrect(setResult, index === tieBreakerSetIndex)
              ? setResult.firstPlayerScore > setResult.secondPlayerScore
                ? 'bg-olive-50 font-bold'
                : ''
              : 'bg-red-100'
          ]"
        >
          <input
            v-if="editMode"
            type="number"
            v-model="setResult.firstPlayerScore"
            class="w-6 border-b-2 bg-transparent px-0.5 outline-none"
          />
          <span v-else class="px-0.5 py-0.5">{{ setResult.firstPlayerScore }}</span>
        </td>
        <td v-if="editMode && canAddSetResult" rowspan="2" class="border p-0">
          <button class="h-full w-full px-1" @click="addSetResult">
            <font-awesome-icon :icon="['fas', 'plus']" size="xl" />
          </button>
        </td>
      </tr>
      <tr>
        <td
          class="border bg-champagne-300 px-2 py-1"
          :class="{ 'font-bold': match.result.winnerId === match.secondPlayerId }"
        >
          {{ nameFromApplicationUser(secondPlayer) }}
        </td>
        <td
          class="w-9 border px-1 py-1"
          v-for="(setResult, index) in match.result.setResults"
          :class="[
            'text-center',
            isSetResultCorrect(setResult, index === tieBreakerSetIndex)
              ? setResult.firstPlayerScore < setResult.secondPlayerScore
                ? 'bg-olive-50 font-bold'
                : ''
              : 'bg-red-100'
          ]"
        >
          <input
            v-if="editMode"
            type="number"
            v-model="setResult.secondPlayerScore"
            class="w-6 border-b-2 bg-transparent px-0.5 outline-none"
          />
          <span v-else class="px-0.5 py-0.5 text-center">{{ setResult.secondPlayerScore }}</span>
        </td>
      </tr>
      <tr v-if="editMode && canRemoveSetResult">
        <td></td>
        <td class="border p-0" v-for="(_, index) in match.result.setResults">
          <button
            class="flex h-full w-full items-center justify-center px-1 py-1 text-red-500"
            @click="removeSetResult(index)"
          >
            <font-awesome-icon :icon="['fas', 'xmark']" size="xl" />
          </button>
        </td>
      </tr>
    </table>
  </div>
</template>
<style>
/* TODO this style affects everything in this project (shouldn't be)*/

input::-webkit-outer-spin-button,
input::-webkit-inner-spin-button {
  -webkit-appearance: none;
}

/* Firefox */
input[type='number'] {
  /* ignore */
  -moz-appearance: textfield;
  appearance: textfield;
}
</style>
