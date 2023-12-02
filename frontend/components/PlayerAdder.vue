<script setup lang="ts">
import { type ApplicationUserBasic } from '~/types/applicationuser.d';

const props = defineProps<{
  numberOfPlayers: number;
  selectedApplicationUsers: ApplicationUserBasic[];
}>();

const config = useRuntimeConfig();

const playerSearchValue = ref('');
const { data: applicationUsersData } = useAsyncData(
  `users`,
  async () => {
    if (playerSearchValue.value.trim() === '') return [];
    return $fetch<ApplicationUserBasic[]>(`${config.public.BACKEND_API}/user`, {
      method: 'GET',
      query: {
        test: 'a',
        name: playerSearchValue.value.trim(),
        limit: 5,
        exclude: props.selectedApplicationUsers.map((sau) => sau.id)
      }
    });
  },
  { watch: [playerSearchValue, props.selectedApplicationUsers] }
);

const applicationUsers = computed(() => applicationUsersData.value ?? []);

let searchCounter = 0;
let lastSearchCounterValue = 0;

async function onInput() {
  // if (playerSearchValue.value.trim() === '') {
  //   lastSearchCounterValue = searchCounter;
  //   applicationUsers.value = [];
  //   return;
  // }
  // const mySearchCounterValue = ++searchCounter;
  // const res = await $fetch<ApplicationUserBasic[]>(`${config.public.BACKEND_API}/user`, {
  //   method: 'GET',
  //   query: {
  //     name: playerSearchValue.value.trim(),
  //     limit: 5,
  //     exclude: props.selectedApplicationUsers.map((sau) => sau.id)
  //   }
  // });
  // if (mySearchCounterValue <= lastSearchCounterValue) return;
  // lastSearchCounterValue = mySearchCounterValue;
  // applicationUsers.value = res;
}

function addApplicationUser(applicationUser: ApplicationUserBasic) {
  props.selectedApplicationUsers.push(applicationUser);
  props.selectedApplicationUsers.sort((au1, au2) => {
    const cmpLastName = au1.lastName.localeCompare(au2.lastName);
    if (cmpLastName !== 0) return cmpLastName;
    return au1.firstName.localeCompare(au2.firstName);
  });
  onInput();
}
function removeApplicationUser(index: number) {
  props.selectedApplicationUsers.splice(index, 1);
  onInput();
}
</script>

<template>
  <h2 class="mt-2 text-2xl font-bold">Zawodnicy</h2>
  <input
    v-model="playerSearchValue"
    class="z-10 mt-1 h-12 rounded-t-lg border-4 border-olive-500 px-2 py-1 outline-none ring-olive-600 focus:ring-2"
    placeholder="Wyszukaj zawodnika"
    @input="onInput"
  />
  <div class="min-h-[11.25rem] pb-2">
    <div
      v-for="applicationUser in applicationUsers"
      class="flex items-center border-x-4 border-x-olive-500 last:rounded-b-lg last:border-b-4 last:border-olive-500 [&:last-child>*]:rounded-b [&:last-child>*]:border-b-0"
    >
      <button
        class="h-full w-full border-b-2 bg-olive-50 px-2 py-1 text-left after:text-neutral-600 hover:bg-olive-100 active:bg-olive-200 disabled:bg-zinc-200 disabled:after:content-['_(Wybrany)']"
        :disabled="selectedApplicationUsers.some((sau) => sau.id === applicationUser.id)"
        @click="addApplicationUser(applicationUser)"
      >
        {{ applicationUser.firstName }} {{ applicationUser.lastName }}
      </button>
    </div>
    <div
      v-if="applicationUsers.length === 0 && playerSearchValue !== ''"
      class="rounded-b border-4 border-t-0 border-olive-500 bg-zinc-100 px-2 py-1 text-neutral-500"
    >
      brak wyników
    </div>
  </div>
  <h2 class="mb-1 mt-2 text-2xl font-bold">
    Wybrani zawodnicy ({{ selectedApplicationUsers.length }}/{{ numberOfPlayers }})
  </h2>
  <div class="flex flex-wrap">
    <div
      v-for="(selectedApplicationUser, index) in selectedApplicationUsers"
      class="mb-2 mr-2 flex items-center rounded-lg bg-olive-50"
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
