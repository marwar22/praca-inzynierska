<script setup lang="ts">
import type { ApplicationUserBasic } from '~/types/applicationuser';
import type { KnockoutBracketMatch } from '~/types/tournament';

const props = defineProps<{
  kbMatch: KnockoutBracketMatch;
  knockoutBracketMatches: Map<number, KnockoutBracketMatch>;
  players: Map<number, ApplicationUserBasic>;
  stage: number;
  setsToWin: number;
  bottom?: boolean;
  last?: boolean;
  showEdit: boolean;
}>();

const match = computed(() => props.kbMatch.match);
const nextKBMatch = computed(() => props.knockoutBracketMatches.get(props.kbMatch.nextMatchInKnockoutBracketId));
const firstPlayer = computed(() => props.players.get(match.value?.playerIds[0] ?? -1));
const secondPlayer = computed(() => props.players.get(match.value?.playerIds[1] ?? -1));

const MATCH_PADDING = 0.5;
const padding = computed(() => {
  return (Math.pow(2, props.stage) - 1) * (2.25 + MATCH_PADDING) + MATCH_PADDING;
});

const heightWithPadding = computed(() => {
  return 2.25 + MATCH_PADDING;
});

const lineTranslateY = computed(() => {
  return heightWithPadding.value * Math.pow(2, props.stage - 1) * (props.bottom ? 1 : -1);
});

const maxSetsInMatch = computed(() => props.setsToWin * 2 - 1);
</script>

<template>
  <div :style="`padding: ${padding}rem 2rem ${padding}rem ${stage > 0 ? 2 : 0}rem;`" class="relative flex items-center">
    <div
      v-if="stage > 0"
      class="absolute left-0 h-[calc(50%+2px)] w-8 border-red-500"
      :style="`transform: translateY(${lineTranslateY}rem);`"
      :class="{ ['border-t-2']: bottom, ['border-b-2']: !bottom }"
    ></div>
    <div
      v-if="!last"
      class="absolute right-0 h-[calc(50%+2px)] w-8 border-r-2 border-red-500"
      :style="`transform: translateY(${lineTranslateY}rem);`"
      :class="{ ['border-t-2']: bottom, ['border-b-2']: !bottom }"
    ></div>
    <table class="h-[4.5rem] w-full">
      <tbody>
        <tr>
          <td
            class="min-w-[8rem] border bg-champagne-300 px-2 py-1"
            :class="{ 'font-bold': match?.result?.winnerId === match?.playerIds[0] }"
          >
            {{
              firstPlayer || match.playerIds[0] === null
                ? nameFromApplicationUser(firstPlayer)
                : 'Błąd - nieznany zawodnik'
            }}&nbsp;
          </td>
          <td
            v-for="setResult in match?.result?.playedSetResults ?? []"
            class="w-[25px] border px-1.5 py-1"
            :class="{ 'bg-olive-50 font-bold': setResult.gamesScored[0] > setResult.gamesScored[1] }"
          >
            <span>{{ setResult.gamesScored[0] }}</span>
          </td>
          <td
            v-for="_ in maxSetsInMatch - (match?.result?.playedSetResults?.length ?? 0)"
            class="w-[25px] border px-1.5 py-1"
          ></td>
          <td rowspan="2" class="w-8 border" v-if="showEdit && match && nextKBMatch?.match.result == null">
            <NuxtLink :to="`/mecz/${match?.id}`" class="h-full pl-1">
              <font-awesome-icon :icon="['fas', 'file-pen']" size="xl" />
            </NuxtLink>
          </td>
        </tr>
        <tr>
          <td
            class="min-w-[8rem] border bg-champagne-300 px-2 py-1"
            :class="{ 'font-bold': match?.result?.winnerId === match?.playerIds[1] }"
          >
            {{
              secondPlayer || match.playerIds[1] === null
                ? nameFromApplicationUser(secondPlayer)
                : 'Błąd - nieznany zawodnik'
            }}&nbsp;
          </td>
          <td
            v-for="setResult in match?.result?.playedSetResults ?? []"
            class="w-[25px] border px-1.5 py-1"
            :class="{ 'bg-olive-50 font-bold': setResult.gamesScored[0] < setResult.gamesScored[1] }"
          >
            <span>{{ setResult.gamesScored[1] }}</span>
          </td>
          <td
            v-for="_ in maxSetsInMatch - (match?.result?.playedSetResults?.length ?? 0)"
            class="w-[25px] border px-1.5 py-1"
          ></td>
        </tr>
      </tbody>
    </table>
  </div>
</template>
