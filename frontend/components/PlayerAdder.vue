<script setup lang="ts">
import axios from 'axios';
import { type ApplicationUser } from '~/types/applicationuser.d';

const props = defineProps<{
  numberOfPlayers: number;
  selectedApplicationUsers: ApplicationUser[];
}>();
console.log(props.selectedApplicationUsers);
const config = useRuntimeConfig();

const playerSearchValue = ref('');
const applicationUsers = ref([] as ApplicationUser[]);

let searchCounter = 0;
let lastSearchCounterValue = 0;
async function onInput() {
  if (playerSearchValue.value.trim() === '') {
    lastSearchCounterValue = searchCounter;
    applicationUsers.value = [];
    return;
  }
  const mySearchCounterValue = ++searchCounter;
  const res = await axios.get(`${config.public.BACKEND_API}/user`, {
    params: { limit: 5, name: playerSearchValue.value.trim() }
  });
  const data = await res.data;
  if (mySearchCounterValue <= lastSearchCounterValue) return;
  lastSearchCounterValue = mySearchCounterValue;
  applicationUsers.value = data;
}

function addApplicationUser(applicationUser: ApplicationUser) {
  props.selectedApplicationUsers.push(applicationUser);
  props.selectedApplicationUsers.sort((au1, au2) => {
    const cmpLastName = au1.lastName.localeCompare(au2.lastName);
    if (cmpLastName !== 0) return cmpLastName;
    return au1.firstName.localeCompare(au2.firstName);
  });
}
function removeApplicationUser(index: number) {
  props.selectedApplicationUsers.splice(index, 1);
}
</script>

<template>
  <h2 class="mt-2 text-2xl font-bold">Zawodnicy</h2>
  <input
    v-model="playerSearchValue"
    class="z-10 mt-1 h-12 rounded-t-lg border-4 border-atlantis-500 px-2 py-1 outline-none ring-atlantis-600 focus:ring-2"
    placeholder="Wyszukaj zawodnika"
    @input="onInput"
  />
  <div class="min-h-[11.25rem] pb-2">
    <div
      v-for="applicationUser in applicationUsers"
      class="flex items-center border-x-4 border-x-atlantis-500 last:rounded-b-lg last:border-b-4 last:border-atlantis-500 [&:last-child>*]:rounded-b [&:last-child>*]:border-b-0"
    >
      <!-- <button class="mr-2 text-atlantis-600 hover:text-atlantis-700 active:text-atlantis-800"><font-awesome-icon icon="fa-solid fa-circle-plus" class="text-[1.75rem]"/></button> -->
      <button
        class="after:text-neutral-600 h-full w-full border-b-2 bg-atlantis-50 px-2 py-1 text-left disabled:after:content-['_(Wybrany)'] hover:bg-atlantis-100 active:bg-atlantis-200 disabled:bg-zinc-200"
        :disabled="selectedApplicationUsers.some((sau) => sau.id === applicationUser.id)"
        @click="addApplicationUser(applicationUser)"
      >
        {{ applicationUser.firstName }} {{ applicationUser.lastName }}
      </button>
    </div>
    <div
      v-if="applicationUsers.length === 0 && playerSearchValue !== ''"
      class="rounded-b border-4 border-t-0 border-atlantis-500 bg-zinc-100 px-2 py-1 text-neutral-500"
    >
      brak wyników
    </div>
  </div>
  <h2 class="mb-1 mt-2 text-2xl font-bold">Wybrani zawodnicy ({{ selectedApplicationUsers.length }}/{{ numberOfPlayers }})</h2>
  <div class="flex flex-wrap">
    <div
      v-for="(selectedApplicationUser, index) in selectedApplicationUsers"
      class="mr-2 flex items-center rounded-lg bg-atlantis-50"
    >
      <span class="pl-2">{{ selectedApplicationUser.firstName }} {{ selectedApplicationUser.lastName }}</span>
      <button
        class="flex items-center px-3 py-1 text-red-500 hover:text-red-600 active:text-red-700"
        @click="removeApplicationUser(index)"
      >
        <font-awesome-icon icon="fa-solid fa-xmark" class="text-[1.75rem]" />
      </button>
    </div>
  </div>
</template>
