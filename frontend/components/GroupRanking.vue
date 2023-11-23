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
    if (!match.result) continue;
    console.log(match);
    setsWon.set(match.firstPlayerId, (setsWon.get(match.firstPlayerId) ?? 0) + match.result.firstPlayerScore);
    setsWon.set(match.secondPlayerId, (setsWon.get(match.secondPlayerId) ?? 0) + match.result.secondPlayerScore);

    setsLost.set(match.firstPlayerId, (setsLost.get(match.firstPlayerId) ?? 0) + match.result.secondPlayerScore);
    setsLost.set(match.secondPlayerId, (setsLost.get(match.secondPlayerId) ?? 0) + match.result.firstPlayerScore);

    matchesPlayed.set(match.firstPlayerId, (matchesPlayed.get(match.firstPlayerId) ?? 0) + 1);
    matchesPlayed.set(match.secondPlayerId, (matchesPlayed.get(match.secondPlayerId) ?? 0) + 1);
    points.set(match.result.winnerId, (points.get(match.result.winnerId) ?? 0) + 1);
    let firstPlayerGamesWon = 0;
    let secondPlayerGamesWon = 0;
    for (const setResult of match.result.setResults) {
      firstPlayerGamesWon += setResult.firstPlayerScore;
      secondPlayerGamesWon += setResult.secondPlayerScore;
    }
    gamesWon.set(match.firstPlayerId, (gamesWon.get(match.firstPlayerId) ?? 0) + firstPlayerGamesWon);
    gamesWon.set(match.secondPlayerId, (gamesWon.get(match.secondPlayerId) ?? 0) + secondPlayerGamesWon);

    gamesLost.set(match.firstPlayerId, (gamesLost.get(match.firstPlayerId) ?? 0) + secondPlayerGamesWon);
    gamesLost.set(match.secondPlayerId, (gamesLost.get(match.secondPlayerId) ?? 0) + firstPlayerGamesWon);
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
  <table>
    <tr>
      <th class="border-2 border-champagne-600 bg-champagne-300 px-2 py-1">#</th>
      <th class="border-2 border-champagne-600 bg-champagne-300 px-2 py-1">Zawodnik</th>
      <th class="border-2 border-champagne-600 bg-champagne-300 px-2 py-1">Punkty</th>
      <th class="border-2 border-champagne-600 bg-champagne-300 px-2 py-1">Sety wygrane</th>
      <th class="border-2 border-champagne-600 bg-champagne-300 px-2 py-1">Sety przegrane</th>
      <th class="border-2 border-champagne-600 bg-champagne-300 px-2 py-1">Gemy wygrane</th>
      <th class="border-2 border-champagne-600 bg-champagne-300 px-2 py-1">Gemy przegrane</th>
      <th class="border-2 border-champagne-600 bg-champagne-300 px-2 py-1">Mecze</th>
    </tr>
    <tr v-for="(player, place) in sortedPlayers">
      <td class="border-2 px-2 py-1">{{ place + 1 }}</td>
      <td class="border-2 px-2 py-1">{{ nameFromApplicationUser(player) }}</td>
      <td class="border-2 px-2 py-1">{{ player.points }}</td>
      <td class="border-2 px-2 py-1">{{ player.setsWon }}</td>
      <td class="border-2 px-2 py-1">{{ player.setsLost }}</td>
      <td class="border-2 px-2 py-1">{{ player.gamesWon }}</td>
      <td class="border-2 px-2 py-1">{{ player.gamesLost }}</td>
      <td class="border-2 px-2 py-1">{{ player.matchesPlayed }}</td>
    </tr>
  </table>
</template>
