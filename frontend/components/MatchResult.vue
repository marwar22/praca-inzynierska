<script setup lang="ts">
import type { ApplicationUserBasic } from '~/types/applicationuser';
import type { Match } from '~/types/tournament';

defineProps<{
  match: Match;
  data: { firstPlayer: ApplicationUserBasic; secondPlayer: ApplicationUserBasic } | null;
}>();
</script>

<template>
  <table class="my-2 mr-2" v-if="match.result">
    <tr>
      <td
        class="border bg-champagne-300 px-2 py-1"
        :class="{ 'font-bold': match.result.winnerId === match.firstPlayerId }"
      >
        {{ nameFromApplicationUser(data?.firstPlayer) }}
      </td>
      <td
        class="border px-1.5 py-1"
        v-for="setResult in match.result.setResults"
        :class="{ 'bg-atlantis-50 font-bold': setResult.firstPlayerScore > setResult.secondPlayerScore }"
      >
        <span>
          {{ setResult.firstPlayerScore }}
        </span>
      </td>
    </tr>
    <tr>
      <td
        class="border bg-champagne-300 px-2 py-1"
        :class="{ 'font-bold': match.result.winnerId === match.secondPlayerId }"
      >
        {{ nameFromApplicationUser(data?.secondPlayer) }}
      </td>
      <td
        class="border px-1.5 py-1"
        v-for="setResult in match.result.setResults"
        :class="{ 'bg-atlantis-50 font-bold': setResult.firstPlayerScore < setResult.secondPlayerScore }"
      >
        <span>
          {{ setResult.secondPlayerScore }}
        </span>
      </td>
    </tr>
  </table>
</template>
