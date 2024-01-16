<script setup lang="ts">
import type { ApplicationUserContact } from '~/types/applicationuser';
import type { Match, Tournament } from '~/types/tournament';

const route = useRoute();
const router = useRouter();
const config = useRuntimeConfig();
const authStatus = useAuthStatus();

const selectedGroupNumber = ref<number>(parseInt(route.query['grupa']?.toString() ?? '0') ?? 0);
watch(selectedGroupNumber, (value) => {
  router.push({ path: route.fullPath, query: { grupa: value }, replace: true });
});

const { data: tournament, error } = await useFetch<Tournament>(
  `${config.public.BACKEND_API}/tournament/${route.params.id}`,
  { key: 'tournament' }
);

function getTournamentTitle() {
  if (tournament.value) return tournament.value.name;
  return 'Rozgrywka';
}

useSeoMeta({
  title: getTournamentTitle,
  description: () => `Szczegóły ${getTournamentTitle()}`
});

const apiError = computed(() => {
  return error.value ? fetchErrorToApiError(error.value) : null;
});

const players = computed(() => {
  return entityArrayToMap(tournament.value?.players ?? []);
});

const showContacts = ref(false);
const contacts = ref<Map<number, ApplicationUserContact>>(new Map());
watch(
  [players, authStatus],
  async (_, __) => {
    const getContacts = async () => {
      if (!authStatus.value.loggedIn) return [];
      if (!players.value.get(authStatus.value.applicationUserId)) return [];
      const tournamentGroup = tournament.value?.groups.find((tg) =>
        tg.playerIds.includes(authStatus.value.applicationUserId)
      );
      if (!tournamentGroup) return [];
      const { data } = await useFetch<ApplicationUserContact[]>(
        `${config.public.BACKEND_API}/tournament/${route.params.id}/group/${tournamentGroup.id}/contact`,
        { credentials: 'include' }
      );
      return data.value;
    };
    contacts.value = entityArrayToMap(await getContacts());
  },
  { immediate: true }
);

const matches = computed(() => {
  const map = new Map<string, Match>();
  for (const group of tournament.value?.groups ?? []) {
    for (const match of group.matches) {
      map.set(`${match.firstPlayerId}|${match.secondPlayerId}`, match);
    }
  }
  return map;
});

const completedMatches = computed(() => {
  let all = 0;
  let completed = 0;
  for (const group of tournament.value?.groups ?? []) {
    for (const match of group.matches) {
      all++;
      if (match.result) completed++;
    }
  }
  return { completed, all };
});

const groupStageFinished = computed(() => {
  return completedMatches.value.completed === completedMatches.value.all;
});

async function createKnockoutBracket() {
  await $fetch(`${config.public.BACKEND_API}/tournament/${route.params.id}/knockout-bracket`, {
    method: 'POST',
    credentials: 'include'
  });
  refreshNuxtData('tournament');
}
async function deleteKnockoutBracket() {
  await $fetch(`${config.public.BACKEND_API}/tournament/${route.params.id}/knockout-bracket`, {
    method: 'DELETE',
    credentials: 'include'
  });
  refreshNuxtData('tournament');
}
</script>
<template>
  <div class="page__margin pb-10">
    <div v-if="tournament">
      <h1 class="mb-2 mt-8 text-3xl font-bold">
        {{ tournament.name }}
      </h1>
      <div class="mb-2">
        <font-awesome-icon icon="fa-solid fa-calendar-days" />
        {{ new Date(tournament.startDate).toLocaleDateString() }} -
        {{ new Date(tournament.endDate).toLocaleDateString() }}
      </div>
      <h2 class="py-1 text-2xl font-bold">Punktacja</h2>
      <div class="flex justify-between max-md:flex-col">
        <div v-if="tournament.hasGroupStage" class="table__scrollbar table__scrollbar--champagne mb-5 overflow-x-auto">
          <table>
            <thead>
              <tr>
                <th class="border-2 border-champagne-600 bg-champagne-300 px-2 py-1">Wynik meczu</th>
                <th class="border-2 border-champagne-600 bg-champagne-300 px-2 py-1">Wygrana</th>
                <th class="border-2 border-champagne-600 bg-champagne-300 px-2 py-1">Przegrana</th>
                <th class="border-2 border-champagne-600 bg-champagne-300 px-2 py-1">Walkover</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td class="border px-2 py-1">Punkty w grupie</td>
                <td class="border px-2 py-1">{{ tournament.scoring.groupPointsForWin }}</td>
                <td class="border px-2 py-1">{{ tournament.scoring.groupPointsForLoss }}</td>
                <td class="border px-2 py-1">{{ tournament.scoring.groupPointsForWalkover }}</td>
              </tr>
              <tr>
                <td class="border px-2 py-1">Punkty do rankingu</td>
                <td class="border px-2 py-1">{{ tournament.scoring.ratingForMatchWin }}</td>
                <td class="border px-2 py-1">{{ tournament.scoring.ratingForMatchLoss }}</td>
                <td class="border px-2 py-1">{{ tournament.scoring.ratingForMatchWalkover }}</td>
              </tr>
            </tbody>
          </table>
        </div>
        <div class="table__scrollbar table__scrollbar--champagne overflow-x-auto">
          <table>
            <thead>
              <tr>
                <th class="border-2 border-champagne-600 bg-champagne-300 px-2 py-1">Udział w etapie</th>
                <th class="border-2 border-champagne-600 bg-champagne-300 px-2 py-1">{{ tournament.hasGroupStage ? 'Grupa' : 'Turniej' }}</th>
                <th
                  v-for="[index, ratingForStage] in tournament.scoring.ratingForKnockoutStageParticipation.entries()"
                  class="border-2 border-champagne-600 bg-champagne-300 px-2 py-1"
                >
                  {{ stageName(index, tournament.scoring) }}
                </th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td class="border px-2 py-1">Punkty&nbsp;do rankingu</td>
                <td class="border px-2 py-1">{{ tournament.scoring.ratingForTournamentParticipation }}</td>
                <td
                  v-for="ratingForStage in tournament.scoring.ratingForKnockoutStageParticipation"
                  class="border px-2 py-1"
                >
                  {{ ratingForStage }}
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <h2 class="mb-1 text-2xl font-bold">Grupy</h2>
      <div class="flex flex-wrap">
        <div v-for="(group, groupNumber) in tournament.groups" class="m-1 flex flex-col">
          <table>
            <thead>
              <tr>
                <th class="border-2 border-champagne-600 bg-champagne-300 px-2 py-1 text-left">
                  {{ `Grupa ${String.fromCharCode(65 + groupNumber)}` }}
                </th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="playerId in group.playerIds" class="border-2">
                <td class="min-w-[12rem] px-2 py-1 hover:bg-olive-50">
                  <span v-if="playerId" class="flex justify-between">
                    <span>{{ nameFromApplicationUser(players.get(playerId)) }}</span>
                    <span v-if="showContacts && contacts.get(playerId)" class="pl-3">{{
                      contacts.get(playerId)?.email
                    }}</span>
                  </span>
                  <span v-else> --- </span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <label v-if="contacts.size > 0">
        <input type="checkbox" v-model="showContacts" />
        Pokaż dane kontaktowe osób w twojej grupie
      </label>

      <div v-if="tournament.hasGroupStage">
        <h2 class="mt-4 text-2xl font-bold">Faza Grupowa</h2>
        <div class="mx-[1px] my-2 flex">
          <button
            v-for="(group, groupNumber) in tournament.groups"
            class="w-24 border border-white px-2 py-4"
            :class="
              groupNumber === selectedGroupNumber
                ? 'bg-champagne-400 font-bold text-white hover:bg-champagne-500 active:bg-champagne-600'
                : 'bg-champagne-300  hover:bg-champagne-400 active:bg-champagne-500'
            "
            @click="selectedGroupNumber = groupNumber"
          >
            {{ `Grupa ${String.fromCharCode(65 + groupNumber)}` }}
          </button>
        </div>
        <GroupRanking
          :tournament="tournament"
          :players="players"
          :groupNumber="selectedGroupNumber"
          :matches="matches"
        />
        <div class="h-8"></div>
        <GroupResults
          :tournament="tournament"
          :players="players"
          :groupNumber="selectedGroupNumber"
          :matches="matches"
        />
        <div class="mt-16">
          Ukończone mecze fazy grupowej
          <div class="flex flex-col">
            <ProgressBar class="w-48" :completed="completedMatches.completed" :all="completedMatches.all" />
            <div class="flex">
              <button
                v-if="tournament.knockoutBracket == null"
                class="my-2 flex items-center"
                :class="{
                  ['cursor-not-allowed text-neutral-400']:
                    !groupStageFinished || tournament.organizerId !== authStatus.applicationUserId
                }"
                @click="createKnockoutBracket"
              >
                <font-awesome-icon icon="fa-solid fa-network-wired" rotation="90" />
                <div v-if="groupStageFinished && tournament.organizerId === authStatus.applicationUserId" class="pl-1">
                  Utwórz drabinkę fazy pucharowej
                </div>
                <span v-else-if="tournament.organizerId !== authStatus.applicationUserId" class="pl-1"
                  >Drabinkę może utworzyć tylko organizator</span
                >
                <span v-else class="pl-1">Drabinkę można utworzyć, gdy każdy mecz fazy grupowej ma wynik</span>
              </button>
              <button v-else @click="deleteKnockoutBracket">Usuń drabinkę</button>
            </div>
          </div>
        </div>
      </div>
      <h2 class="mt-4 text-2xl font-bold">Faza Pucharowa</h2>
      <KnockoutBracket
        :knockout-bracket="tournament.knockoutBracket"
        :sets-to-win="tournament.setsToWin"
        :players="players"
        :show-edit="true"
      />
    </div>
    <ApiError :api-error="apiError" />
  </div>
</template>
