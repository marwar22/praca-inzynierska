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
const hoveredPlayerId = ref(-1);
function onMouseEnter(playerId: number) {
  hoveredPlayerId.value = playerId;
}
function onMouseLeave(playerId: number) {
  if (hoveredPlayerId.value === playerId) hoveredPlayerId.value = -1;
}
</script>

<template>
  <div class="table__scrollbar table__scrollbar table__scrollbar--olive overflow-x-auto w-full">
    <table class="w-full">
      <thead>
        <tr>
          <td class="border-2 bg-champagne-300 text-center font-bold">
            {{ `Grupa ${String.fromCharCode(65 + groupNumber)}` }}
          </td>
          <th
            v-for="secondPlayerId in tournament.groups[groupNumber].playerIds"
            class="h-12 w-40 min-w-[10rem] border-2 bg-olive-200 px-2 py-1"
          >
            {{ nameFromApplicationUser(players.get(secondPlayerId)!) }}
          </th>
        </tr>
      </thead>
      <tbody>
        <tr
          v-for="firstPlayerId in tournament.groups[groupNumber].playerIds"
          @mouseenter="onMouseEnter(firstPlayerId)"
          @mouseleave="onMouseLeave(firstPlayerId)"
        >
          <th
            class="h-18 w-48 border-2 px-2 py-1"
            :class="[hoveredPlayerId === firstPlayerId ? 'bg-olive-300' : 'bg-olive-200']"
          >
            {{ nameFromApplicationUser(players.get(firstPlayerId)!) }}
          </th>
          <td
            v-for="secondPlayerId in tournament.groups[groupNumber].playerIds"
            class="h-16 border-2 hover:bg-olive-50"
          >
            <div
              v-if="firstPlayerId !== secondPlayerId"
              class="relative flex h-full w-full items-center justify-center"
            >
              <MatchResultInTable
                :matches="matches"
                :playerIds="[firstPlayerId, secondPlayerId]"
                :organizerId="tournament.organizerId"
                :hoveredPlayerId="hoveredPlayerId"
              />
            </div>
            <div v-else class="text-center">---</div>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>
