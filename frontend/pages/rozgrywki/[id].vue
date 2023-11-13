<script setup lang="ts">
import type { Tournament } from '~/types/tournament';

const route = useRoute();
const config = useRuntimeConfig();

const { data: tournament, error } = await useFetch<Tournament>(
  `${config.public.BACKEND_API}/tournament/${route.params.id}`
);
const apiError = computed(() => {
  return error.value ? fetchErrorToApiError(error.value) : null;
});
</script>
<template>
  <div v-if="tournament">
    <h1 class="text-3xl font-bold">
      {{ tournament.name }}
    </h1>
    <div>
      <font-awesome-icon icon="fa-solid fa-calendar-days" />
      {{ new Date(tournament.startDate).toLocaleDateString() }} -
      {{ new Date(tournament.endDate).toLocaleDateString() }}
    </div>
    <div class="flex flex-wrap">
      <div
        v-for="(group, index) in tournament.groups"
        class="mx-2 my-1 rounded-lg bg-champagne-300 px-2 py-1 font-bold"
      >
        Grupa {{ String.fromCharCode(65 + index) }}
      </div>
    </div>
    <div class="flex flex-wrap">
      <div v-for="(group, groupIndex) in tournament.groups" class="m-1 flex flex-col">
        <table>
          <tr class="border-2">
            <th class="bg-champagne-300 px-2 py-1 text-left">{{ `Grupa ${String.fromCharCode(65 + groupIndex)}` }}</th>
          </tr>
          <tr v-for="player in group.playerIds" class="border-2">
            <td class="min-w-[12rem] px-2 py-1 hover:bg-atlantis-50">
              <span v-if="player"> {{ player }}</span>
              <span v-else>---</span>
            </td>
          </tr>
        </table>
      </div>
    </div>
  </div>
  <ApiError :api-error="apiError" />
</template>
