<script setup lang="ts">
import type { Match, Tournament } from '~/types/tournament';

const route = useRoute();
const config = useRuntimeConfig();

const selectedGroupNumber = ref(0);

const { data: tournament, error } = await useFetch<Tournament>(
  `${config.public.BACKEND_API}/tournament/${route.params.id}`
);

const apiError = computed(() => {
  return error.value ? fetchErrorToApiError(error.value) : null;
});

const players = computed(() => {
  const map = new Map();
  for (const player of tournament.value?.players ?? []) {
    map.set(player.id, player);
  }
  return map;
});

const matches = computed(() => {
  const map = new Map<string, Match>();
  for (const group of tournament.value?.groups ?? []) {
    for (const match of group.matches) {
      map.set(`${match.firstPlayerId}|${match.secondPlayerId}`, match);
    }
  }
  return map;
});
const completedMatches = computed(() => {
  let all = 0;
  let completed = 0;
  for (const group of tournament.value?.groups ?? []) {
    for (const match of group.matches) {
      all++;
      if (match.result) completed++;
    }
  }
  const fraction = completed / all;
  return { completed, all, fraction };
});
</script>
<template>
  <div class="page__margin pb-10">
    <div v-if="tournament">
      <h1 class="mt-8 text-3xl font-bold">
        {{ tournament.name }}
      </h1>
      <div>
        <font-awesome-icon icon="fa-solid fa-calendar-days" />
        {{ new Date(tournament.startDate).toLocaleDateString() }} -
        {{ new Date(tournament.endDate).toLocaleDateString() }}
      </div>

      <div class="flex flex-wrap">
        <div v-for="(group, groupNumber) in tournament.groups" class="m-1 flex flex-col">
          <table>
            <tr class="border-2">
              <th class="bg-champagne-300 px-2 py-1 text-left">
                <NuxtLink :to="`/rozgrywki/${route.params.id}/grupa/${groupNumber}`">
                  {{ `Grupa ${String.fromCharCode(65 + groupNumber)}` }}
                </NuxtLink>
              </th>
            </tr>
            <tr v-for="playerId in group.playerIds" class="border-2">
              <td class="min-w-[12rem] px-2 py-1 hover:bg-atlantis-50">
                <span v-if="playerId"> {{ nameFromApplicationUser(players.get(playerId)) }}</span>
                <span v-else> --- </span>
              </td>
            </tr>
          </table>
        </div>
      </div>

      <div>
        Ukończone mecze
        <ProgressBar class="w-48" :completed="completedMatches.completed" :all="completedMatches.all" />
      </div>
      <div class="mx-[1px] my-2 flex">
        <button
          v-for="(group, groupNumber) in tournament.groups"
          class="w-24 border border-white px-2 py-4"
          :class="
            groupNumber === selectedGroupNumber
              ? 'bg-champagne-400 font-bold text-white hover:bg-champagne-500 active:bg-champagne-600'
              : 'bg-champagne-300  hover:bg-champagne-400 active:bg-champagne-500'
          "
          @click="selectedGroupNumber = groupNumber"
        >
          {{ `Grupa ${String.fromCharCode(65 + groupNumber)}` }}
        </button>
      </div>
      <GroupRanking :tournament="tournament" :players="players" :groupNumber="selectedGroupNumber" :matches="matches" />
      <div class="h-8"></div>
      <GroupResults :tournament="tournament" :players="players" :groupNumber="selectedGroupNumber" :matches="matches" />
    </div>
    <ApiError :api-error="apiError" />
  </div>
</template>
