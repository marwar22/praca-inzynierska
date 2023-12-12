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
// TODO move to server
const sortedPlayers = computed(
  () =>
    players.value?.toSorted((p1, p2) => {
      return (p2.rating ?? 0) - (p1.rating ?? 0);
    })
);
</script>
<template>
  <div class="page__margin">
    <h1 class="mb-4 mt-2 text-3xl font-bold">Ranking</h1>
    <table>
      <thead>
        <tr>
          <th class="border border-champagne-900 bg-champagne-300 px-2 py-1">Ranking</th>
          <th class="border border-champagne-900 bg-champagne-300 px-2 py-1">Zawodnik</th>
          <th class="border border-champagne-900 bg-champagne-300 px-2 py-1">Punkty</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(player, index) in sortedPlayers">
          <td class="border border-champagne-900 px-2 py-1">{{ index + 1 }}</td>
          <td class="border border-champagne-900 px-2 py-1">{{ nameFromApplicationUser(player) }}</td>
          <td class="border border-champagne-900 px-2 py-1">{{ player.rating ?? 0 }}</td>
        </tr>
      </tbody>
    </table>
    <ApiError :api-error="apiError" />
  </div>
</template>
