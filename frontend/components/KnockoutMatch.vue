<script setup lang="ts">
import type { ApplicationUser } from '~/types/applicationuser';
import type { Match } from '~/types/tournament';

const props = defineProps<{
  match: Match;
  stage: number;
  bottom?: boolean;
  last?: boolean;
}>();

const users = Boolean(true)
  ? {
      firstPlayer: { id: 103, username: 'adam', firstName: 'Adam', lastName: 'Adamski' } as ApplicationUser,
      secondPlayer: { id: 102, username: 'abcd', firstName: 'Abcd', lastName: 'Dcba' } as ApplicationUser,
      lastModifiedById: { id: 104, username: 'jan12', firstName: 'Jan', lastName: 'Janowski' } as ApplicationUser
    }
  : {};

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
      <tr>
        <td
          class="border bg-champagne-300 px-2 py-1"
          :class="{ 'font-bold': match.result?.winnerId === match.firstPlayerId }"
        >
          {{ nameFromApplicationUser(users?.firstPlayer) + 'a'.repeat(Math.random() * 10) }}
        </td>
        <td
          v-for="setResult in match.result?.setResults ?? []"
          class="w-[25px] border px-1.5 py-1"
          :class="{ 'bg-atlantis-50 font-bold': setResult.firstPlayerScore > setResult.secondPlayerScore }"
        >
          <span>
            {{ setResult.firstPlayerScore }}
          </span>
        </td>
        <td v-for="empty in 5 - (match.result?.setResults?.length ?? 0)" class="w-[25px] border px-1.5 py-1"></td>
      </tr>
      <tr>
        <td
          class="border bg-champagne-300 px-2 py-1"
          :class="{ 'font-bold': match.result?.winnerId === match.secondPlayerId }"
        >
          {{ nameFromApplicationUser(users?.secondPlayer) }}
        </td>
        <td
          v-for="setResult in match.result?.setResults ?? []"
          class="w-[25px] border px-1.5 py-1"
          :class="{ 'bg-atlantis-50 font-bold': setResult.firstPlayerScore < setResult.secondPlayerScore }"
        >
          <span>
            {{ setResult.secondPlayerScore }}
          </span>
        </td>
        <td v-for="empty in 5 - (match.result?.setResults?.length ?? 0)" class="w-[25px] border px-1.5 py-1"></td>
      </tr>
    </table>
  </div>
</template>
