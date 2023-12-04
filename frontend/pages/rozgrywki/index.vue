<script setup lang="ts">
import type { Tournament, TournamentBasic } from '~/types/tournament';

const config = useRuntimeConfig();
const authStatus = useAuthStatus();
// const { data: tournaments } = await useFetch<TournamentBasic[]>(`${config.public.BACKEND_API}/tournament`);

const canCreateTournament = computed(() => {
  return authStatus.value.permissions.includes('TOURNAMENT:CREATE');
});

const tournamentNameSearch = ref('');
const { data: tournamentsData } = useAsyncData(
  async () => {
    return $fetch<TournamentBasic[]>(`${config.public.BACKEND_API}/tournament`, {
      method: 'GET',
      query: {
        name: tournamentNameSearch.value
      }
    });
  },
  { watch: [tournamentNameSearch] }
);

const tournaments = computed(() => tournamentsData.value ?? []);
</script>
<template>
  <div class="page__margin flex flex-col">
    <h1 class="mb-2 mt-4 text-3xl font-bold">Rozgrywki</h1>
    <div class="flex h-12 items-center">
      <input
        v-model="tournamentNameSearch"
        class="h-full rounded-lg border-4 border-olive-500 px-2 py-1 outline-none ring-olive-600 focus-visible:ring-2"
        placeholder="Wyszukaj"
      />
      <div class="flex-1"></div>
      <NuxtLink
        :to="canCreateTournament ? '/rozgrywki/utworz' : ''"
        class="my-1 flex h-full items-center justify-center rounded-lg bg-champagne-500 px-4 py-1 text-lg font-bold text-white outline-none ring-champagne-800 focus-visible:ring-2"
        :class="{
          ['hover:bg-champagne-600 active:bg-champagne-700']: canCreateTournament,
          ['cursor-not-allowed ']: !canCreateTournament
        }"
        :title="canCreateTournament ? '' : 'Musisz być zalogowany i mieć uprawnienia do tworzenia rozgrywek'"
      >
        <span class="max-md:hidden">Utwórz rozgrywkę</span>
        <font-awesome-icon :icon="['fas', 'plus']" size="xl" class="md:hidden" />
      </NuxtLink>
    </div>
    <NuxtLink
      v-for="tournament in tournaments"
      :to="`/rozgrywki/${tournament.id}`"
      class="my-2 flex w-full items-center justify-between rounded-lg border-4 border-olive-500 px-2 py-1 text-left hover:bg-olive-50 active:bg-olive-100"
    >
      <div class="flex flex-col">
        <div>
          <h2 class="inline-block text-lg font-bold">{{ tournament.name }}</h2>
        </div>

        <div>
          <font-awesome-icon icon="fa-solid fa-calendar-days" />
          {{ new Date(tournament.startDate).toLocaleDateString() }} -
          {{ new Date(tournament.endDate).toLocaleDateString() }}
        </div>
      </div>
      <div>
        <font-awesome-icon icon="fa-solid fa-users" />
        <span class="ml-1">{{ tournament.numberOfPlayers }}</span>
      </div>
    </NuxtLink>
  </div>
</template>
