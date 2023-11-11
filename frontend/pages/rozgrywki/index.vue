<script setup lang="ts">
import type { Tournament } from '~/types/tournament';

const config = useRuntimeConfig();
const leagues = ['Lato 2023', 'Zima 2023', 'Lato 2022', 'Zima 2022'];
const { data: tournaments } = await useFetch<Tournament[]>(`${config.public.BACKEND_API}/tournament`);
</script>
<template>
  <div class="container__margin flex flex-col">
    <h1 class="mb-2 mt-4 text-3xl font-bold">Rozgrywki</h1>
    <div class="flex h-12 items-center">
      <input
        class="h-full rounded-lg border-4 border-atlantis-500 px-2 py-1 outline-none ring-atlantis-600 focus-visible:ring-2"
        placeholder="Wyszukaj"
      />
      <div class="flex-1"></div>
      <NuxtLink
        to="/rozgrywki/utworz"
        class="my-1 flex h-full items-center justify-center rounded-lg bg-champagne-500 px-4 py-1 text-lg font-bold text-white outline-none ring-champagne-800 hover:bg-champagne-600 focus-visible:ring-2 active:bg-champagne-700"
      >
        Utwórz rozgrywkę
      </NuxtLink>
    </div>
    <button
      v-for="tournament in tournaments"
      class="my-2 flex w-full items-center justify-between rounded-lg border-4 border-atlantis-500 px-2 py-1 text-left hover:bg-atlantis-50 active:bg-atlantis-100"
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
        <span class="ml-1">{{ tournament.groups.reduce((sum, t) => sum + t.playerIds.length, 0) }}</span>
      </div>
    </button>
  </div>
</template>

<style>
.container__margin {
  margin: 0 clamp(8px, calc((100% - 1100px) * 0.35), 16%);
}
</style>
