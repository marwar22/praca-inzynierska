<script setup lang="ts">
import type { ApplicationUser, ApplicationUserBasic } from '~/types/applicationuser';

const props = defineProps<{
  numberOfGroups: number;
  playersInGroups: number[];
  selectedApplicationUsers: ApplicationUserBasic[];
  groups: ApplicationUserBasic[][];
}>();

const emit = defineEmits<{
  (e: 'update:groups', groups: ApplicationUserBasic[][]): void;
}>();

function shuffleArray<T>(array: T[]) {
  for (let i = array.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1));
    [array[i], array[j]] = [array[j], array[i]];
  }
}

function generateGroups() {
  const players = [...props.selectedApplicationUsers];
  const groups = Array.from({ length: props.numberOfGroups }, (_) => [] as ApplicationUserBasic[]);
  shuffleArray(players);
  let groupId = 0;
  for (let i = 0; i < players.length; i++) {
    if (groups[groupId].length < props.playersInGroups[groupId]) {
      groups[groupId].push(players[i]);
    }
    groupId = (groupId + 1) % props.numberOfGroups;
  }
  emit('update:groups', groups);
}
</script>
<template>
  <div class="mt-4 flex items-center">
    <h2 class="text-2xl font-bold">Grupy</h2>
    <button
      class="ml-2 rounded-lg bg-atlantis-600 px-2.5 py-1 text-white outline-none ring-atlantis-800 hover:bg-atlantis-700 focus-visible:ring-2 active:bg-atlantis-800"
      @click="generateGroups()"
    >
      Wylosuj skład grup
      <font-awesome-icon icon="fa-solid fa-dice" class="pl-1" />
    </button>
  </div>
  <div class="flex flex-wrap">
    <div v-for="(_, group) in numberOfGroups" class="m-1 flex flex-col">
      <table>
        <tr class="border-2">
          <th class="bg-champagne-300 px-2 py-1 text-left">{{ `Grupa ${String.fromCharCode(65 + group)}` }}</th>
        </tr>
        <tr v-for="(_, player) in playersInGroups[group]" class="border-2">
          <td class="min-w-[12rem] px-2 py-1 hover:bg-atlantis-50">
            <span v-if="groups[group] && groups[group][player]">
              {{ groups[group][player].firstName }} {{ groups[group][player].lastName }}</span
            >
            <span v-else>---</span>
          </td>
        </tr>
      </table>
    </div>
  </div>
</template>
