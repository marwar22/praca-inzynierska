<script setup lang="ts">
import type { ApiError } from '~/types/apierrror';
import type { ApplicationUser } from '~/types/applicationuser';
import type { Match } from '~/types/tournament';

const route = useRoute();
const config = useRuntimeConfig();
const authStatus = useAuthStatus();

const { data: match } = await useFetch<Match>(`${config.public.BACKEND_API}/match/${route.params.id}`);
const { data: users } = await useAsyncData(async () => {
  const [firstPlayer, secondPlayer, lastModifiedBy] = await Promise.all([
    $fetch<ApplicationUser>(`${config.public.BACKEND_API}/user/${match.value?.firstPlayerId}`),
    $fetch<ApplicationUser>(`${config.public.BACKEND_API}/user/${match.value?.secondPlayerId}`),
    match.value?.lastModifiedById
      ? $fetch<ApplicationUser>(`${config.public.BACKEND_API}/user/${match.value?.lastModifiedById}`)
      : null
  ]);
  return { firstPlayer, secondPlayer, lastModifiedBy };
});

const canEdit = computed(() => {
  // TODO  match.value?.organizerId
  return [match.value?.firstPlayerId, match.value?.secondPlayerId].includes(authStatus.value.applicationUserId);
});
</script>
<template>
  <div class="page__margin" v-if="match">
    <h1 class="mt-8 text-2xl font-bold">
      {{ nameFromApplicationUser(users?.firstPlayer) }} vs {{ nameFromApplicationUser(users?.secondPlayer) }}
    </h1>
    <div>
      <span v-if="match.result" class="text-lg"
        >Wynik: {{ match.result.firstPlayerScore }}:{{ match.result.secondPlayerScore }}</span
      >
      <div class="flex items-center">
        <table class="my-2 mr-2" v-if="match.result">
          <tr>
            <td
              class="border bg-champagne-300 px-2 py-1"
              :class="{ 'font-bold': match.result.winnerId === match.firstPlayerId }"
            >
              {{ nameFromApplicationUser(users?.firstPlayer) }}
            </td>
            <td
              class="border px-1.5 py-1"
              v-for="setResult in match.result.setResults"
              :class="{ 'bg-atlantis-50 font-bold': setResult.firstPlayerScore > setResult.secondPlayerScore }"
            >
              <span>
                {{ setResult.firstPlayerScore }}
              </span>
            </td>
          </tr>
          <tr>
            <td
              class="border bg-champagne-300 px-2 py-1"
              :class="{ 'font-bold': match.result.winnerId === match.secondPlayerId }"
            >
              {{ nameFromApplicationUser(users?.secondPlayer) }}
            </td>
            <td
              class="border px-1.5 py-1"
              v-for="setResult in match.result.setResults"
              :class="{ 'bg-atlantis-50 font-bold': setResult.firstPlayerScore < setResult.secondPlayerScore }"
            >
              <span>
                {{ setResult.secondPlayerScore }}
              </span>
            </td>
          </tr>
        </table>
        <NuxtLink
          :to="`/mecz/${route.params.id}/edytuj`"
          v-if="canEdit"
          class="hover:text-atlantis-700 active:text-atlantis-400"
          title="Edytuj"
        >
          <font-awesome-icon icon="fa-solid fa-file-pen" size="xl" />
        </NuxtLink>
        <div
          v-else-if="authStatus.loggedIn"
          title="Edytować mogą tylko zawodnicy, lub organizator"
          class="cursor-not-allowed text-gray-300"
        >
          <font-awesome-icon icon="fa-solid fa-file-pen" size="xl" /> Edytować mogą tylko zawodnicy, lub organizator
        </div>
      </div>

      <div class="flex flex-col" v-if="match.result">
        Ostatnia modyfikacja:
        <span>
          <font-awesome-icon :icon="['fas', 'user']" />
          {{ nameFromApplicationUser(users?.lastModifiedBy) }}
        </span>
        <span>
          <font-awesome-icon :icon="['fas', 'clock']" />
          {{ new Date(match.updatedDateTime).toLocaleDateString() }}
          {{ new Date(match.updatedDateTime).toLocaleTimeString() }}
        </span>
      </div>
    </div>
    <div>
      <font-awesome-icon icon="fa-solid fa-circle-info" /> Mecze mogą edytować tylko zalogowani użytkownicy
    </div>
  </div>
</template>
