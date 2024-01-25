<script setup lang="ts">
import { ref } from 'vue';

import type { ApiError } from '~/types/apierrror';
import type { ApplicationUser, ApplicationUserBasic } from '~/types/applicationuser';
import VueDatePicker from '@vuepic/vue-datepicker';
import type { KnockoutBracket, Tournament, TournamentScoring } from '~/types/tournament';

useSeoMeta({
  title: 'Tworzenie rozgrywki',
  description: 'Tworzenie spersonalizowanej rozgrywki tenisa ziemnego na rozgrywkitenisa.pl'
});

const config = useRuntimeConfig();

const MIN_NUMBER_OF_PLAYERS = 2;
const MAX_NUMBER_OF_PLAYERS = 128;

const MIN_NUMBER_OF_GROUPS = 1;
const MAX_NUMBER_OF_GROUPS = 24;

const apiError = ref<ApiError | null>(null);
const createError = ref<string | null>(null);
const name = ref('');
const date = ref([new Date(), new Date()]);

const numberOfPlayers = ref(16);
const isNumberOfPlayersInRange = computed(
  () => MIN_NUMBER_OF_PLAYERS <= numberOfPlayers.value && numberOfPlayers.value <= MAX_NUMBER_OF_PLAYERS
);

const hasGroupStage = ref(true);
const numberOfGroups = ref(4);
const isNumberOfGroupsInRange = computed(
  () => MIN_NUMBER_OF_GROUPS <= numberOfGroups.value && numberOfGroups.value <= MAX_NUMBER_OF_GROUPS
);
const knockoutBracket = ref<KnockoutBracket>({ id: -1, matches: [] });

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
  const maxPlayers = hasGroupStage.value
    ? clamp(MIN_NUMBER_OF_PLAYERS, numberOfPlayers.value, MAX_NUMBER_OF_PLAYERS)
    : MAX_NUMBER_OF_PLAYERS;
  let max = 1 << (31 - Math.clz32(maxPlayers));
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

watch(hasGroupStage, () => {
  if (!hasGroupStage.value) groups.value = [];
});
watch([numberOfPlayersInKnockoutBracket, hasGroupStage], ([newValue, _]) => {
  if (!hasGroupStage.value) numberOfPlayers.value = newValue;
});
const nameError = computed(() => {
  if (name.value.trim().length < 5) return 'Nazwa musi mieć co najmniej 5 znaków';
  if (name.value.trim().length > 255) return 'Nazwa musi mieć co najwyżej 255 znaków';
  return null;
});
const numberOfPlayersError = computed(() => {
  if (isNumberOfPlayersInRange.value) return null;
  return `Liczba graczy musi być w przedziale od ${MIN_NUMBER_OF_PLAYERS} do ${MAX_NUMBER_OF_PLAYERS}`;
});
const numberOfGroupsError = computed(() => {
  if (isNumberOfGroupsInRange.value) return null;
  return `Liczba grup musi być w przedziale od ${MIN_NUMBER_OF_GROUPS} do ${MAX_NUMBER_OF_GROUPS}`;
});
const numberOfSelectedPlayersError = computed(() => {
  if (selectedApplicationUsers.value.length === numberOfPlayers.value) return null;
  return `Liczba wybranych zawodników musi wynosić ${numberOfPlayers.value}`;
});
const groupsError = computed(() => {
  if (!hasGroupStage.value) return null;
  const playerCount = groups.value.reduce((acc, g) => {
    return acc + g.length;
  }, 0);
  if (playerCount === numberOfPlayers.value) return null;
  return 'Należy wygenerować skład grup';
});
watch([nameError, numberOfPlayersError, numberOfGroupsError, numberOfSelectedPlayersError, groupsError], () => {
  createError.value = null;
});
async function create() {
  createError.value = null;
  createError.value ??= nameError.value;
  createError.value ??= numberOfPlayersError.value;
  createError.value ??= numberOfGroupsError.value;
  createError.value ??= numberOfSelectedPlayersError.value;
  createError.value ??= groupsError.value;

  if (createError.value !== null) return;

  try {
    const [startDate, endDate] = date.value;
    const res = await $fetch<Tournament>(`${config.public.BACKEND_API}/tournament`, {
      method: 'POST',
      body: {
        name: name.value.trim(),
        numberOfPlayers: numberOfPlayers.value,
        hasGroupStage: hasGroupStage.value,
        numberOfGroups: hasGroupStage.value ? numberOfGroups.value : 0,
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
    await navigateTo(`/rozgrywki/${res.id}`, { replace: true });
  } catch (error) {
    apiError.value = fetchErrorToApiError(error);
  }
}
</script>

<template>
  <div class="page__margin flex flex-col pb-20">
    <h1 class="mt-5 text-3xl font-bold">Tworzenie nowej rozgrywki</h1>
    <div class="flex max-md:flex-col">
      <label class="mr-5 flex flex-1 flex-col max-md:w-full">
        Nazwa
        <ContestCreateInput placeholder="Nazwa rozgrywki" v-model.number="name" :error="nameError" />
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
      <label v-if="hasGroupStage" class="flex w-64 flex-col max-md:w-full md:mr-5">
        Liczba zawodników
        <ContestCreateInput
          placeholder="Liczba grup"
          type="number"
          v-model.number="numberOfPlayers"
          :min="1"
          :max="nextPowerOf10(MAX_NUMBER_OF_PLAYERS) - 1"
          :error="numberOfPlayersError"
        />
      </label>

      <label v-if="hasGroupStage" class="flex w-64 flex-col max-md:w-full md:mr-5">
        Liczba grup
        <ContestCreateInput
          placeholder="Liczba grup"
          type="number"
          v-model.number="numberOfGroups"
          :min="MIN_NUMBER_OF_GROUPS"
          :max="nextPowerOf10(MAX_NUMBER_OF_GROUPS) - 1"
          :error="numberOfGroupsError"
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
    <label class="flex">
      <input type="checkbox" class="w-4" v-model="hasGroupStage" />
      <span class="pl-2">Faza grupowa</span>
    </label>
    <GroupsCreator
      v-if="hasGroupStage"
      v-model:groups="groups"
      :numberOfGroups="numberOfGroups"
      :playersInGroups="playersInGroups"
      :selectedApplicationUsers="selectedApplicationUsers"
    />
    <KnockoutBracketCreator
      v-else
      v-model:knockout-bracket="knockoutBracket"
      :numberOfPlayersInKnockoutBracket="numberOfPlayersInKnockoutBracket"
      :sets-to-win="setsToWin"
      :selectedApplicationUsers="selectedApplicationUsers"
    />

    <button
      class="my-2 rounded-lg bg-olive-600 py-1 text-lg font-bold text-white hover:bg-olive-700 active:bg-olive-800"
      @click="create"
    >
      Utwórz
    </button>
    <div v-if="createError" class="my-1 font-bold text-red-500">
      {{ createError }}
    </div>
    <ApiError :api-error="apiError"></ApiError>
  </div>
</template>
