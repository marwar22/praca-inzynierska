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

const buckets = computed(() => {
  const sortedPlayers = props.selectedApplicationUsers.toSorted((p1, p2) => {
    const p1Rating = p1.rating ?? 0;
    const p2Rating = p2.rating ?? 0;
    if (p1Rating === p2Rating) return p1.id - p2.id;
    return p2Rating - p1Rating;
  });
  const buckets = [];
  for (let i = 0; i < sortedPlayers.length; i += props.numberOfGroups) {
    const bucket = [];
    for (let j = i; j < Math.min(sortedPlayers.length, i + props.numberOfGroups); j++) {
      bucket.push(sortedPlayers[j]);
    }
    buckets.push(bucket);
  }
  return buckets;
});

function generateGroups() {
  const groups = Array.from({ length: props.numberOfGroups }, (_) => [] as ApplicationUserBasic[]);
  for (const bucket of buckets.value) {
    const shuffledBucket = [...bucket];
    shuffleArray(shuffledBucket);

    for (const [index, player] of shuffledBucket.entries()) {
      groups[index].push(player);
    }
  }
  emit('update:groups', groups);
}
</script>
<template>
  <div class="mt-4 flex items-center">
    <h2 class="text-2xl font-bold">Grupy</h2>
    <button
      class="ml-2 rounded-lg bg-olive-600 px-2.5 py-1 text-white outline-none ring-olive-800 hover:bg-olive-700 focus-visible:ring-2 active:bg-olive-800"
      @click="generateGroups()"
    >
      Wylosuj skład grup
      <font-awesome-icon icon="fa-solid fa-dice" class="pl-1" />
    </button>
  </div>
  <div class="table__scrollbar table__scrollbar--champagne mt-4 overflow-x-auto">
    <table>
      <tr v-for="[index, bucket] in buckets.entries()">
        <th class="border bg-champagne-300 px-2 py-1">Koszyk&nbsp;{{ index + 1 }}</th>
        <td
          class="border px-2 py-1"
          v-for="player in bucket"
          :class="[{ 'bg-red-400/80': playersInGroups[0] <= index }]"
        >
          <div class="flex justify-between">
            <span>{{ nameFromApplicationUser(player) }}</span>
            <span class="pl-3">({{ player.rating ?? 0 }})</span>
          </div>
        </td>
      </tr>
    </table>
  </div>

  <div class="mt-4 flex flex-wrap">
    <div v-for="(_, groupIndex) in numberOfGroups" class="m-1 flex flex-col" :key="groupIndex">
      <table>
        <thead>
          <tr class="border-2">
            <th class="bg-champagne-300 px-2 py-1 text-left">{{ `Grupa ${String.fromCharCode(65 + groupIndex)}` }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(_, playerIndex) in playersInGroups[groupIndex]" class="border-2" :key="playerIndex">
            <td class="min-w-[12rem] px-2 py-1 hover:bg-olive-50">
              <span v-if="groups[groupIndex] && groups[groupIndex][playerIndex]">
                {{ groups[groupIndex][playerIndex].firstName }} {{ groups[groupIndex][playerIndex].lastName }}</span
              >
              <span v-else>---</span>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>
