<script setup lang="ts">
import type { Match } from '~/types/tournament';

const props = defineProps<{
  playerIds: [number, number];
  organizerId: number;
  matches: Map<string, Match>;
  hoveredPlayerId: number;
}>();

const match = computed(() => {
  let match = props.matches.get(`${props.playerIds[0]}|${props.playerIds[1]}`);
  let playerOnLeftId = 0;
  if (!match) {
    match = props.matches.get(`${props.playerIds[1]}|${props.playerIds[0]}`)!;
    playerOnLeftId = 1;
  }
  return { ...match, playerOnLeftId };
});
const leftPlayerIndex = computed(() => match.value.playerOnLeftId);
const rightPlayerIndex = computed(() => 1 - match.value.playerOnLeftId);

const hov1 = computed(() => props.hoveredPlayerId === props.playerIds[0]);
const hov2 = computed(() => props.hoveredPlayerId === props.playerIds[1] && false);
</script>

<template>
  <NuxtLink :to="`/mecz/${match.id}`" class="flex flex-col items-center justify-center px-1.5 py-1">
    <div v-if="match.result">
      <div class="flex flex-col items-center justify-start">
        <span class="inline-flex text-xl font-bold">
          <span :class="[{ [`text-olive-700`]: hov1 }]">
            <font-awesome-icon
              :icon="['far', 'flag']"
              :class="{ invisible: !(match.result.walkover && match.result.winnerId === playerIds[1]) }"
            />
            {{ match.result?.setsScored[leftPlayerIndex] }}
          </span>
          :
          <span :class="[{ [`text-olive-700`]: hov2 }]">
            {{ match.result?.setsScored[rightPlayerIndex] }}
            <font-awesome-icon
              :icon="['far', 'flag']"
              :class="{ invisible: !(match.result.walkover && match.result.winnerId === playerIds[0]) }"
            />
          </span>
        </span>
        <span class="text-center text-sm">
          <span v-for="(setResult, setNumber) in match.result.playedSetResults" class="[&:last-child>*:nth-child(2)]:hidden">
            <span
              >(<span :class="[{ [`text-olive-700`]: hov1 }]"
                ><font-awesome-icon
                  class="pr-0.5"
                  v-if="
                    match.result.scratch &&
                    match.result.winnerId === playerIds[1] &&
                    setNumber === match.result.playedSetResults.length - 1
                  "
                  :icon="['fas', 'house-medical-flag']"
                />{{ setResult.gamesScored[leftPlayerIndex] }}</span
              >:<span :class="[{ [`text-olive-700`]: hov2 }]"
                >{{ setResult.gamesScored[rightPlayerIndex]
                }}<font-awesome-icon
                  class="pl-0.5"
                  v-if="
                    match.result.scratch &&
                    match.result.winnerId === playerIds[0] &&
                    setNumber === match.result.playedSetResults.length - 1
                  "
                  :icon="['fas', 'house-medical-flag']" /></span
              >)</span
            ><span>,&nbsp;</span>
            <br v-if="setNumber === 2" />
          </span>
        </span>
      </div>
    </div>
    <span v-else>Brak wyniku</span>
  </NuxtLink>
</template>
