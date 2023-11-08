<script setup lang="ts">
defineProps<{
  numberOfGroups: number;
  playersInGroups: number[];
}>();
</script>
<template>
  <div class="mt-2 flex items-center">
    <h2 class="text-2xl font-bold">Grupy</h2>
    <button
      class="ml-2 rounded-lg bg-atlantis-600 px-2.5 py-1 text-white outline-none ring-atlantis-800 hover:bg-atlantis-700 focus-visible:ring-2 active:bg-atlantis-800"
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
          <td class="min-w-[12rem] px-2 py-1 hover:bg-atlantis-50">Gracz {{ player }}</td>
        </tr>
      </table>
    </div>
  </div>
</template>
