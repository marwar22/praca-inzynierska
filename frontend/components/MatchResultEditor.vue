<script setup lang="ts">
import { faL } from '@fortawesome/free-solid-svg-icons';
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
  if (result.scratch || result.walkover) return;
  if (result.firstPlayerScore > result.secondPlayerScore) result.winnerId = firstPlayerId;
  else if (result.firstPlayerScore < result.secondPlayerScore) result.winnerId = secondPlayerId;
  else result.winnerId = -1;
});

const canAddSetResult = computed(() => props.match.result && props.match.result.setResults.length < MAX_SETS_IN_MATCH);
const canRemoveSetResult = computed(() => {
  const result = props.match.result;
  if (!result) return false;
  if (result.scratch) return 1 < result.setResults.length;
  return MIN_SETS_IN_MATCH < result.setResults.length;
});

const tieBreakerSetIndex = computed(() => calculateTieBreakerSetIndex(props.match.result));

function addSetResult() {
  if (!canAddSetResult) return;
  props.match.result?.setResults.push({ firstPlayerScore: 0, secondPlayerScore: 0, gamesScored: [0, 0] });
}

function removeSetResult(index: number) {
  if (!canRemoveSetResult) return;
  props.match.result?.setResults.splice(index, 1);
}
function onNormal() {
  if (!props.match.result) return;
  props.match.result.walkover = false;
  props.match.result.scratch = false;
  while (props.match.result.setResults.length < 2) addSetResult();
}

function onWalkover(winnerId: number | null) {
  if (!props.match.result || !winnerId) return;
  if (props.match.result.walkover && props.match.result.winnerId === winnerId) {
    props.match.result.walkover = false;
    return;
  }
  props.match.result.walkover = true;
  props.match.result.scratch = false;
  props.match.result.winnerId = winnerId;

  props.match.result.setResults = Array.from([1, 2], () => {
    if (winnerId === props.match.firstPlayerId)
      return { firstPlayerScore: 6, secondPlayerScore: 0, gamesScored: [6, 0] };
    return { firstPlayerScore: 0, secondPlayerScore: 6, gamesScored: [0, 6] };
  });
}

function onScratch(winnerId: number | null) {
  if (!props.match.result || !winnerId) return;
  if (props.match.result.scratch && props.match.result.winnerId === winnerId) {
    props.match.result.scratch = false;
    return;
  }
  props.match.result.scratch = true;
  props.match.result.walkover = false;
  props.match.result.winnerId = winnerId;
}

const _playerFields = computed(
  () =>
    [
      { id: 'firstPlayerId', score: 'firstPlayerScore', player: props.firstPlayer },
      { id: 'secondPlayerId', score: 'secondPlayerScore', player: props.secondPlayer }
    ] as const
);
const playerFields = computed(() => [
  { ..._playerFields.value[0], other: _playerFields.value[1] },
  { ..._playerFields.value[1], other: _playerFields.value[0] }
]);
</script>
<template>
  <div class="flex max-w-full flex-col">
    <div class="table__scrollbar table__scrollbar--champagne overflow-x-auto max-md:text-sm">
      <table class="h-[4.5rem]" v-if="match.result">
        <tr v-for="({ id, score, player, other: { id: otherId, score: otherScore } }, index) in playerFields">
          <td class="border bg-champagne-300 px-2 py-1" :class="{ 'font-bold': match.result.winnerId === match[id] }">
            {{ nameFromApplicationUser(player) }}
          </td>
          <td
            class="w-9 border px-1 py-1"
            v-for="(setResult, index) in match.result.setResults"
            :class="[
              'text-center',
              isSetResultCorrect(match.result, index, tieBreakerSetIndex)
                ? setResult[score] > setResult[otherScore]
                  ? 'bg-olive-50 font-bold'
                  : ''
                : 'bg-red-100'
            ]"
          >
            <input
              v-if="editMode"
              type="number"
              :disabled="match.result.walkover"
              v-model="setResult[score]"
              class="w-6 border-b-2 bg-transparent px-0.5 outline-none"
            />
            <span v-else class="px-0.5 py-0.5">{{ setResult[score] }}</span>
          </td>
          <td v-if="index === 0 && editMode && canAddSetResult" rowspan="2" class="border p-0">
            <button
              class="h-full w-full px-2 disabled:text-neutral-400"
              @click="addSetResult"
              :disabled="match.result.walkover"
            >
              <font-awesome-icon :icon="['fas', 'plus']" size="xl" />
            </button>
          </td>
          <td
            v-if="index === 0 && editMode && canAddSetResult"
            rowspan="2"
            class="border-x-2 border-champagne-950 p-0"
          ></td>
          <td v-if="index === 0 && editMode" rowspan="2" class="w-9 min-w-[2rem] border p-0 text-center">
            <button
              class="h-full w-full"
              @click="onNormal()"
              :class="{ 'bg-olive-400': !match.result.walkover && !match.result.scratch }"
            >
              <font-awesome-icon :icon="['fas', 'list-ol']" />
            </button>
          </td>
          <td v-if="editMode" class="w-9 min-w-[2rem] border p-0 text-center">
            <button
              class="b h-full w-full"
              @click="onWalkover(match[otherId])"
              :class="{ 'bg-orange-500 text-white': match.result.walkover && match.result.winnerId === match[otherId] }"
            >
              <font-awesome-icon :icon="['fas', 'flag']" />
            </button>
          </td>
          <td v-if="editMode" class="w-9 min-w-[2rem] border p-0 text-center">
            <button
              class="h-full w-full"
              @click="onScratch(match[otherId])"
              :class="{ 'bg-rose-500 text-white': match.result.scratch && match.result.winnerId === match[otherId] }"
            >
              <font-awesome-icon :icon="['fas', 'house-medical-flag']" />
            </button>
          </td>

          <td
            v-if="!editMode && match.result.walkover && match.result.winnerId === match[otherId]"
            class="border px-1 py-1.5 text-center"
          >
            <font-awesome-icon :icon="['far', 'flag']" /> Walkover
          </td>
          <td
            v-if="!editMode && match.result.scratch && match.result.winnerId === match[otherId]"
            class="border px-1 py-1.5 text-center"
          >
            <font-awesome-icon :icon="['fas', 'house-medical-flag']" /> Krecz
          </td>
        </tr>
        <tr v-if="editMode">
          <td></td>
          <td class="border p-0" v-for="(_, index) in match.result.setResults">
            <button
              class="flex h-full w-full items-center justify-center px-1 py-1 text-red-500 disabled:text-neutral-400"
              :disabled="!canRemoveSetResult"
              @click="removeSetResult(index)"
            >
              <font-awesome-icon :icon="['fas', 'xmark']" size="xl" />
            </button>
          </td>
        </tr>
      </table>
    </div>
    <div class="mb-4 flex w-max flex-col" v-if="editMode">
      Legenda:
      <table>
        <tr>
          <td class="text-center"><font-awesome-icon :icon="['fas', 'list-ol']" /></td>
          <td>-Zwycięstwo na punkty</td>
        </tr>
        <tr>
          <td class="text-center"><font-awesome-icon :icon="['far', 'flag']" transform="left-1.5" /></td>
          <td>-Walkover</td>
        </tr>
        <tr>
          <td class="text-center"><font-awesome-icon :icon="['fas', 'house-medical-flag']" /></td>
          <td>-Krecz</td>
        </tr>
      </table>
    </div>
  </div>
</template>
<style scoped>
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
