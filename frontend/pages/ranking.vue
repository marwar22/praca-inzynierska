<script setup lang="ts">
import type { ApplicationUserBasic } from '~/types/applicationuser';

const route = useRoute();
const config = useRuntimeConfig();

const { data: players, error } = await useFetch<ApplicationUserBasic[]>(
  `${config.public.BACKEND_API}/user?limit=999999999`
);

const apiError = computed(() => {
  return error.value ? fetchErrorToApiError(error.value) : null;
});

const sortedPlayers = computed(() => {
  const sp =
    players.value?.toSorted((p1, p2) => {
      return (p1.ranking ?? Number.MAX_VALUE) - (p2.ranking ?? Number.MAX_VALUE);
    }) ?? [];
  return sp.map((player, index) => {
    return {
      ...player,
      rating: player.rating ?? 0,
      prvRating: player.prvRating ?? 0,
      ranking: player.ranking ?? index,
      prvRanking: player.prvRanking ?? index
    };
  });
});
</script>
<template>
  <div class="page__margin flex flex-col items-center">
    <h1 class="mb-4 mt-2 text-3xl font-bold">Ranking</h1>
    <table>
      <thead>
        <tr>
          <th class="border border-champagne-900 bg-champagne-300 px-2 py-1" colspan="2">Ranking</th>
          <th class="border border-champagne-900 bg-champagne-300 px-2 py-1">Zawodnik</th>
          <th class="border border-champagne-900 bg-champagne-300 px-2 py-1">Punkty</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(player, index) in sortedPlayers">
          <td class="border border-champagne-900 px-2 py-1 border-r-0">
            {{ player.ranking + 1 }}
          </td>
          <td class="border-y border-champagne-900 pr-1 py-1 text-left">
            <font-awesome-icon
              v-if="player.ranking < player.prvRanking"
              :icon="['fas', 'arrow-up']"
              class="text-olive-600"
            />
            <font-awesome-icon
              v-else-if="player.ranking > player.prvRanking"
              :icon="['fas', 'arrow-down']"
              class="text-rose-600"
            />
            <font-awesome-icon v-else :icon="['fas', 'equals']" class="text-neutral-500"/>
            <span v-if="player.ranking != player.prvRanking" class="pl-1">{{ Math.abs(player.ranking - player.prvRanking) }}</span>
          </td>
          <td class="border border-champagne-900 px-2 py-1">{{ nameFromApplicationUser(player) }}</td>
          <td class="border border-champagne-900 px-2 py-1">{{ player.rating ?? 0 }}</td>
        </tr>
      </tbody>
    </table>
    <ApiError :api-error="apiError" />
  </div>
</template>
