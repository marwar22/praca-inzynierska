<script setup lang="ts">
import axios, { AxiosError } from 'axios';
import { ref } from 'vue';

import type { ApiError } from '~/types/apierrror';
import type { ApplicationUser, ApplicationUserBasic } from '~/types/applicationuser';
import VueDatePicker from '@vuepic/vue-datepicker';

const config = useRuntimeConfig();

const MIN_NUMBER_OF_PLAYERS = 1;
const MAX_NUMBER_OF_PLAYERS = 128;

const MIN_NUMBER_OF_GROUPS = 1;
const MAX_NUMBER_OF_GROUPS = 24;

const apiError = ref(null as ApiError | null);
const name = ref('');
const date = ref([new Date(), new Date()]);
const numberOfPlayers = ref(16);
const isNumberOfPlayersInRange = computed(
  () => MIN_NUMBER_OF_PLAYERS <= numberOfPlayers.value && numberOfPlayers.value <= MAX_NUMBER_OF_PLAYERS
);
const numberOfGroups = ref(4);
const isNumberOfGroupsInRange = computed(
  () => MIN_NUMBER_OF_GROUPS <= numberOfGroups.value && numberOfGroups.value <= MAX_NUMBER_OF_GROUPS
);

const selectedApplicationUsers = ref([] as ApplicationUserBasic[]);
const groups = ref([] as ApplicationUser[][]);

const playersInGroups = computed(() => {
  const res = [];
  let players = numberOfPlayers.value;
  let groups = numberOfGroups.value;
  for (; groups > 0; groups--) {
    let playersInGroup = Math.ceil(players / groups);
    players -= playersInGroup;
    res.push(playersInGroup);
  }
  return res;
});

function onNumberOfPlayersInput() {}
async function create() {
  try {
    const [startDate, endDate] = date.value;
    const res = await axios.post(
      `${config.public.BACKEND_API}/tournament`,
      {
        name: name.value,
        numberOfPlayers: numberOfPlayers.value,
        numberOfGroups: numberOfGroups.value,
        playerIds: selectedApplicationUsers.value.map((sau) => sau.id),
        groups: groups.value
          .map((group, index) => {
            return { playerIds: group.map((au) => au.id).slice(0, playersInGroups.value[index]) };
          })
          .slice(0, numberOfGroups.value),
        startDate,
        endDate
      },
      { withCredentials: true }
    );
    apiError.value = null;
  } catch (error) {
    apiError.value = await errorToApiError(error);
  }
}
</script>

<template>
  <div class="page__margin flex flex-col">
    <h1 class="mt-5 text-3xl font-bold">Tworzenie nowej rozgrywki</h1>
    <div class="flex max-md:flex-col">
      <label class="flex flex-1 flex-col mr-5">
        Nazwa
        <ContestCreateInput
          placeholder="Nazwa rozgrywki"
          v-model.number="name"
          @input="onNumberOfPlayersInput"
          :error="name && name.trim().length < 5 ? 'Nazwa musi mieć co najmniej 5 znaków' : ''"
        />
      </label>

      <label class="flex w-64 flex-col mr-5">
        Liczba zawodników
        <ContestCreateInput
          placeholder="Liczba grup"
          type="number"
          v-model.number="numberOfPlayers"
          @input="onNumberOfPlayersInput"
          :error="
            !isNumberOfPlayersInRange
              ? `Liczba graczy musi być w przedziale od ${MIN_NUMBER_OF_PLAYERS} do ${MAX_NUMBER_OF_PLAYERS}`
              : ''
          "
        />
      </label>

      <label class="flex w-64 flex-col">
        Liczba grup
        <ContestCreateInput
          placeholder="Liczba grup"
          type="number"
          v-model.number="numberOfGroups"
          @input="onNumberOfPlayersInput"
          :error="
            !isNumberOfGroupsInRange
              ? `Liczba grup musi być w przedziale od ${MIN_NUMBER_OF_GROUPS} do ${MAX_NUMBER_OF_GROUPS}`
              : ''
          "
        />
      </label>
    </div>
    <label>
      Czas trwania
      <div class="datepicker rounded-lg border-4 border-olive-500">
        <VueDatePicker
          v-model="date"
          range
          :enableTimePicker="false"
          format="dd.MM.yyyy"
          locale="pl-PL"
          select-text="Wybierz"
          cancel-text="Anuluj"
        />
      </div>
    </label>
    <PlayerAdder :numberOfPlayers="numberOfPlayers" v-model:selectedApplicationUsers="selectedApplicationUsers" />
    <GroupsCreator
      v-model:groups="groups"
      :numberOfGroups="numberOfGroups"
      :playersInGroups="playersInGroups"
      :selectedApplicationUsers="selectedApplicationUsers"
    >
    </GroupsCreator>
    <button
      class="my-2 rounded-lg bg-olive-600 py-1 text-lg font-bold text-white hover:bg-olive-700 active:bg-olive-800"
      @click="create"
    >
      Utwórz
    </button>
    <ApiError :api-error="apiError"></ApiError>
  </div>
</template>
