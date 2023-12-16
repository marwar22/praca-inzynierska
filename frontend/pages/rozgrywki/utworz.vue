<script setup lang="ts">
import { ref } from 'vue';

import type { ApiError } from '~/types/apierrror';
import type { ApplicationUser, ApplicationUserBasic } from '~/types/applicationuser';
import VueDatePicker from '@vuepic/vue-datepicker';
import type { Tournament, TournamentScoring } from '~/types/tournament';

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

const setsToWin = ref(2);
const setsToWinOptions = Array.from({ length: MAX_SETS_TO_WIN - MIN_SETS_TO_WIN + 1 }, (_, i) => MIN_SETS_TO_WIN + i);

const scoring = ref<TournamentScoring>({
  groupPointsForWin: 2,
  groupPointsForLoss: 1,
  groupPointsForWalkover: 0,
  ratingForTournamentParticipation: 75,
  ratingForMatchWin: 25,
  ratingForMatchLoss: 13,
  ratingForMatchWalkover: 0,
  ratingForTournamentWin: 125,
  ratingForKnockoutStageParticipation: [2]
});

const numberOfPlayersInKnockoutBracket = ref(4);
const numberOfPlayersInKnockoutBracketOptions = computed(() => {
  let max = 1 << (31 - Math.clz32(numberOfPlayers.value));
  const res = [];
  for (let i = max; i > 1; i /= 2) res.push(i);
  res.reverse();
  return res;
});

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

watch(numberOfPlayers, () => {
  if (numberOfPlayersInKnockoutBracket.value > numberOfPlayers.value) {
    numberOfPlayersInKnockoutBracket.value = numberOfPlayersInKnockoutBracketOptions.value.at(-1) ?? 2;
  }
});

async function create() {
  try {
    const [startDate, endDate] = date.value;
    const res = await $fetch<Tournament>(`${config.public.BACKEND_API}/tournament`, {
      method: 'POST',
      body: {
        name: name.value,
        numberOfPlayers: numberOfPlayers.value,
        numberOfGroups: numberOfGroups.value,
        setsToWin: setsToWin.value,
        scoring: scoring.value,
        numberOfPlayersInKnockoutBracket: numberOfPlayersInKnockoutBracket.value,
        playerIds: selectedApplicationUsers.value.map((sau) => sau.id),
        groups: groups.value
          .map((group, index) => {
            return { playerIds: group.map((au) => au.id).slice(0, playersInGroups.value[index]) };
          })
          .slice(0, numberOfGroups.value),
        startDate,
        endDate
      },
      credentials: 'include'
    });
    apiError.value = null;
    await navigateTo(`/rozgrywki/${res.id}`)
  } catch (error) {
    apiError.value = fetchErrorToApiError(error);
  }
}
</script>

<template>
  <div class="page__margin flex flex-col">
    <h1 class="mt-5 text-3xl font-bold">Tworzenie nowej rozgrywki</h1>
    <div class="flex max-md:flex-col">
      <label class="mr-5 flex flex-1 flex-col">
        Nazwa
        <ContestCreateInput
          placeholder="Nazwa rozgrywki"
          v-model.number="name"
          :error="name && name.trim().length < 5 ? 'Nazwa musi mieć co najmniej 5 znaków' : ''"
        />
      </label>
      <label class="flex flex-col md:min-w-[24rem]">
        Czas trwania
        <div class="datepicker my-1 h-12 rounded-lg border-4 border-olive-500">
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
    </div>
    <div class="mt-2 flex flex-wrap">
      <label class="mr-5 flex w-64 flex-col">
        Liczba zawodników
        <ContestCreateInput
          placeholder="Liczba grup"
          type="number"
          v-model.number="numberOfPlayers"
          :min="MIN_NUMBER_OF_PLAYERS"
          :max="MAX_NUMBER_OF_PLAYERS * 10"
          :error="
            !isNumberOfPlayersInRange
              ? `Liczba graczy musi być w przedziale od ${MIN_NUMBER_OF_PLAYERS} do ${MAX_NUMBER_OF_PLAYERS}`
              : ''
          "
        />
      </label>

      <label class="mr-5 flex w-64 flex-col">
        Liczba grup
        <ContestCreateInput
          placeholder="Liczba grup"
          type="number"
          v-model.number="numberOfGroups"
          :min="MIN_NUMBER_OF_GROUPS"
          :max="MAX_NUMBER_OF_GROUPS * 10"
          :error="
            !isNumberOfGroupsInRange
              ? `Liczba grup musi być w przedziale od ${MIN_NUMBER_OF_GROUPS} do ${MAX_NUMBER_OF_GROUPS}`
              : ''
          "
        />
      </label>
      <div class="mb-2 mr-5 flex flex-col">
        Liczba finalistów
        <div class="my-1 flex h-12 items-center">
          <RadioGroup v-model="numberOfPlayersInKnockoutBracket" :values="numberOfPlayersInKnockoutBracketOptions" />
        </div>
      </div>
      <div class="mb-2 flex flex-col">
        Liczba setów do wygranej
        <div class="my-1 flex h-12 items-center">
          <RadioGroup v-model="setsToWin" :values="setsToWinOptions" />
        </div>
      </div>
    </div>

    <div>
      <h2 class="mt-2 text-2xl font-bold">Punktacja</h2>
      <TournamentScoringCreator
        v-model:scoring="scoring"
        :numberOfPlayersInKnockoutBracket="numberOfPlayersInKnockoutBracket"
      />
    </div>

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
