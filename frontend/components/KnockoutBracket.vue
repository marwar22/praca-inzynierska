<script setup lang="ts">
import type { Match } from '~/types/tournament';

let match = {
  id: 1,
  result: {
    id: 1,
    winnerId: 102,
    setResults: [
      {
        firstPlayerScore: 2,
        secondPlayerScore: 6
      },
      {
        firstPlayerScore: 6,
        secondPlayerScore: 7
      },
      {
        firstPlayerScore: 6,
        secondPlayerScore: 3
      },
      {
        firstPlayerScore: 6,
        secondPlayerScore: 1
      },
      {
        firstPlayerScore: 4,
        secondPlayerScore: 6
      }
    ],
    firstPlayerScore: 2,
    secondPlayerScore: 3
  },
  firstPlayerId: 103,
  secondPlayerId: 102,
  lastModifiedById: 104,
  createdDateTime: '2023-11-22T22:52:32.322961',
  updatedDateTime: '2023-11-22T23:07:56.140228'
} as Match;
match.result = null;
</script>
<template>
  <div class="flex overflow-x-auto overflow-y-hidden">
    <div class="flex shrink-0 flex-col">
      <KnockoutMatch :match="match" :stage="0" v-for="n in 8" :bottom="n % 2 == 1" />
    </div>
    <div class="flex shrink-0 flex-col">
      <KnockoutMatch :match="match" :stage="1" v-for="n in 4" :bottom="n % 2 == 1" />
    </div>
    <div class="flex shrink-0 flex-col">
      <KnockoutMatch :match="match" :stage="2" v-for="n in 2" :bottom="n % 2 == 1" />
    </div>
    <div class="flex shrink-0 flex-col">
      <KnockoutMatch :match="match" :stage="3" v-for="n in 1" :bottom="n % 2 == 1" :last="true" />
    </div>
  </div>
</template>
