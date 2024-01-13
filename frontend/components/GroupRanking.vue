<script setup lang="ts">
import type { ApplicationUser } from '~/types/applicationuser';
import type { Tournament, Match } from '~/types/tournament';

const props = defineProps<{
  tournament: Tournament;
  players: Map<number, ApplicationUser>;
  groupNumber: number;
  matches: Map<string, Match>;
}>();

const group = computed(() => {
  return props.tournament.groups[props.groupNumber];
});

const results = computed(() => {
  const points = new Map<number, number>();
  const matchesPlayed = new Map<number, number>();
  const setsWon = new Map<number, number>();
  const setsLost = new Map<number, number>();
  const gamesWon = new Map<number, number>();
  const gamesLost = new Map<number, number>();

  for (const match of group.value.matches) {
    const { firstPlayerId, secondPlayerId } = match;
    if (!match.result || !firstPlayerId || !secondPlayerId) continue;
    for (const [index, playerId] of [firstPlayerId, secondPlayerId].entries()) {
      setsWon.set(playerId, (setsWon.get(playerId) ?? 0) + match.result.setsScored[index]);
      setsLost.set(playerId, (setsLost.get(playerId) ?? 0) + match.result.setsScored[1 - index]);
      matchesPlayed.set(playerId, (matchesPlayed.get(playerId) ?? 0) + 1);
      let playerGamesWon = 0;
      let playerGamesLost = 0;
      for (const setResult of match.result.setResults) {
        playerGamesWon += setResult.gamesScored[index];
        playerGamesLost += setResult.gamesScored[1 - index];
      }
      gamesWon.set(playerId, (gamesWon.get(playerId) ?? 0) + playerGamesWon);
      gamesLost.set(playerId, (gamesLost.get(playerId) ?? 0) + playerGamesLost);
    }
    points.set(
      match.result.winnerId,
      (points.get(match.result.winnerId) ?? 0) + props.tournament.scoring.groupPointsForWin
    );

    const loserId = match.result.winnerId === firstPlayerId ? firstPlayerId : secondPlayerId;
    points.set(
      loserId,
      (points.get(loserId) ?? 0) +
        (match.result.walkover
          ? props.tournament.scoring.groupPointsForWalkover
          : props.tournament.scoring.groupPointsForLoss)
    );
  }
  return { matchesPlayed, points, setsWon, setsLost, gamesWon, gamesLost };
});

const sortedPlayers = computed(() => {
  const players = group.value.playerIds.map((playerId) => {
    return {
      ...props.players.get(playerId)!,

      matchesPlayed: results.value.matchesPlayed.get(playerId) ?? 0,
      points: results.value.points.get(playerId) ?? 0,
      setsWon: results.value.setsWon.get(playerId) ?? 0,
      setsLost: results.value.setsLost.get(playerId) ?? 0,
      gamesWon: results.value.gamesWon.get(playerId) ?? 0,
      gamesLost: results.value.gamesLost.get(playerId) ?? 0
    };
  });
  players.sort((a, b) => {
    // prettier-ignore
    if (a.points === b.points) {
      if (a.setsWon === b.setsWon) return (b.gamesWon - b.gamesLost) - (a.gamesWon - a.gamesLost);
      return (b.setsWon - b.setsLost) - (a.setsWon-a.setsLost);
    }
    return b.points - a.points;
  });
  return players;
});
</script>
<template>
  <div class="table__scrollbar table__scrollbar--champagne overflow-x-auto">
    <table>
      <thead>
        <tr>
          <th class="border-2 border-champagne-600 bg-champagne-300 px-2 py-1">#</th>
          <th class="border-2 border-champagne-600 bg-champagne-300 px-2 py-1">Zawodnik</th>
          <th class="border-2 border-champagne-600 bg-champagne-300 px-2 py-1">Punkty</th>
          <th class="border-2 border-champagne-600 bg-champagne-300 px-2 py-1">Sety wygrane</th>
          <th class="border-2 border-champagne-600 bg-champagne-300 px-2 py-1">Sety przegrane</th>
          <th class="border-2 border-champagne-600 bg-champagne-300 px-2 py-1">Gemy</th>
          <th class="border-2 border-champagne-600 bg-champagne-300 px-2 py-1">Mecze</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(player, place) in sortedPlayers">
          <td class="border-2 px-2 py-1">{{ place + 1 }}</td>
          <td class="border-2 px-2 py-1">{{ nameFromApplicationUser(player) }}</td>
          <td class="border-2 px-2 py-1">{{ player.points }}</td>
          <td class="border-2 px-2 py-1">{{ player.setsWon }}</td>
          <td class="border-2 px-2 py-1">{{ player.setsLost }}</td>
          <td class="border-2 px-2 py-1">
            {{ player.gamesWon > player.gamesLost ? '+' : '' }}{{ player.gamesWon - player.gamesLost }}
          </td>
          <td class="border-2 px-2 py-1">{{ player.matchesPlayed }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>
