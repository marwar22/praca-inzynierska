<script setup lang="ts">
import type { ApplicationUserContact } from '~/types/applicationuser';
import type { Match, Tournament } from '~/types/tournament';

const route = useRoute();
const config = useRuntimeConfig();
const authStatus = useAuthStatus();

const selectedGroupNumber = ref(0);

const { data: tournament, error } = await useFetch<Tournament>(
  `${config.public.BACKEND_API}/tournament/${route.params.id}`
);

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
  return completedMatches.value.completed === completedMatches.value.all || true;
});

async function createKnockoutBracket() {
  await $fetch(`${config.public.BACKEND_API}/tournament/${route.params.id}/create-knockout-bracket`, {
    body: { numberOfPlayers: 4 },
    method: 'POST',
    credentials: 'include'
  });
}
</script>
<template>
  <div class="page__margin pb-10">
    <div v-if="tournament">
      <h1 class="mt-8 text-3xl font-bold">
        {{ tournament.name }}
      </h1>
      <div>
        <font-awesome-icon icon="fa-solid fa-calendar-days" />
        {{ new Date(tournament.startDate).toLocaleDateString() }} -
        {{ new Date(tournament.endDate).toLocaleDateString() }}
      </div>

      <div class="flex flex-wrap">
        <div v-for="(group, groupNumber) in tournament.groups" class="m-1 flex flex-col">
          <table>
            <tr class="border-2">
              <th class="bg-champagne-300 px-2 py-1 text-left">
                <NuxtLink :to="`/rozgrywki/${route.params.id}/grupa/${groupNumber}`">
                  {{ `Grupa ${String.fromCharCode(65 + groupNumber)}` }}
                </NuxtLink>
              </th>
            </tr>
            <tr v-for="playerId in group.playerIds" class="border-2">
              <td class="min-w-[12rem] px-2 py-1 hover:bg-atlantis-50">
                <span v-if="playerId" class="flex justify-between">
                  <span>{{ nameFromApplicationUser(players.get(playerId)) }}</span>
                  <span v-if="showContacts && contacts.get(playerId)" class="pl-3">{{
                    contacts.get(playerId)?.email
                  }}</span>
                </span>
                <span v-else> --- </span>
              </td>
            </tr>
          </table>
        </div>
      </div>
      <label v-if="contacts.size > 0">
        <input type="checkbox" v-model="showContacts" />
        Pokaż dane kontaktowe osób w twojej grupie
      </label>

      <h2 class="mt-4 text-xl font-bold">Faza Grupowa</h2>
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
      <GroupRanking :tournament="tournament" :players="players" :groupNumber="selectedGroupNumber" :matches="matches" />
      <div class="h-8"></div>
      <GroupResults :tournament="tournament" :players="players" :groupNumber="selectedGroupNumber" :matches="matches" />

      <div class="mt-16">
        Ukończone mecze fazy grupowej
        <div class="flex items-center">
          <ProgressBar class="w-48" :completed="completedMatches.completed" :all="completedMatches.all" />
          <button
            v-if="tournament.knockoutBracket == null"
            class="ml-2"
            :class="{
              ['cursor-not-allowed text-neutral-400']:
                !groupStageFinished || tournament.organizerId !== authStatus.applicationUserId
            }"
            @click="createKnockoutBracket"
          >
            <font-awesome-icon icon="fa-solid fa-network-wired" rotation="90" />
            <span v-if="groupStageFinished && tournament.organizerId === authStatus.applicationUserId" class="pl-1">
              Utwórz drabinkę fazy pucharowej
            </span>
            <span v-else-if="tournament.organizerId !== authStatus.applicationUserId" class="pl-1"
              >Drabinkę może utworzyć tylko organizator</span
            >
            <span v-else class="pl-1">Drabinkę można utworzyć, gdy każdy mecz fazy grupowej ma wynik</span>
          </button>
        </div>
      </div>
      <h2 class="mt-4 text-xl font-bold">Faza Pucharowa</h2>
      <KnockoutBracket />
    </div>
    <ApiError :api-error="apiError" />
  </div>
</template>
