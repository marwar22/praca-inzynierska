<script setup lang="ts">
import type { ApplicationUser } from '~/types/applicationuser';
import type { Match, Tournament } from '~/types/tournament';

const authStatus = useAuthStatus();

defineProps<{
  tournament: Tournament;
  players: Map<number, ApplicationUser>;
  groupNumber: number;
  matches: Map<string, Match>;
}>();
</script>

<template>
  <div class="overflow-x-auto">
    <table>
      <tr>
        <td class="border-2 bg-champagne-300 text-center font-bold">
          {{ `Grupa ${String.fromCharCode(65 + groupNumber)}` }}
        </td>
        <th
          v-for="firstPlayerId in tournament.groups[groupNumber].playerIds"
          class="h-12 w-40 min-w-[10rem] border-2 px-2 py-1"
          :class="authStatus.applicationUserId === firstPlayerId ? 'bg-atlantis-300' : 'bg-atlantis-200'"
        >
          {{ nameFromApplicationUser(players.get(firstPlayerId)!) }}
        </th>
      </tr>
      <tr v-for="firstPlayerId in tournament.groups[groupNumber].playerIds">
        <th
          class="h-18 w-48 border-2 px-2 py-1"
          :class="authStatus.applicationUserId === firstPlayerId ? 'bg-atlantis-300' : 'bg-atlantis-200'"
        >
          {{ nameFromApplicationUser(players.get(firstPlayerId)!) }}
        </th>
        <td v-for="secondPlayerId in tournament.groups[groupNumber].playerIds" class="h-16 border-2">
          <div
            v-if="firstPlayerId !== secondPlayerId"
            class="relative flex h-full w-full items-center justify-center px-2 py-1"
          >
            <MatchResultInTable :matches="matches" :firstPlayerId="firstPlayerId" :secondPlayerId="secondPlayerId" />
          </div>
          <div v-else class="text-center">---</div>
        </td>
      </tr>
    </table>
  </div>
  <div v-if="!authStatus.loggedIn">
    <font-awesome-icon icon="fa-solid fa-circle-info" /> Mecze mogą edytować tylko zalogowani użytkownicy
  </div>
</template>
