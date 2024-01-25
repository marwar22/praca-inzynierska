<script setup lang="ts">
import type { ApplicationUser, ApplicationUserRole } from '~/types/applicationuser';

useSeoMeta({
  title: 'Konto',

});


definePageMeta({
  middleware: ['logged-in']
});

const config = useRuntimeConfig();
const authStatus = useAuthStatus();

const { data: applicationUser } = await useFetch<ApplicationUser>(`${config.public.BACKEND_API}/user/me`, {
  credentials: 'include'
});

const { data: otherUsersUnsorted } = await useFetch<ApplicationUser[]>(`${config.public.BACKEND_API}/user?all=true`, {
  credentials: 'include',
  key: 'otherUsers'
});
const otherUsers = computed(() => {
  return (
    otherUsersUnsorted.value?.toSorted((a, b) => {
      if (a.lastName === b.lastName) return a.firstName.localeCompare(b.firstName);
      return a.lastName.localeCompare(b.lastName);
    }) ?? []
  );
});
const modifiableRoles: ApplicationUserRole[] = ['TOURNAMENT_ORGANIZER'];
async function addRole(user: ApplicationUser, role: ApplicationUserRole) {
  await $fetch(`${config.public.BACKEND_API}/user/${user.id}/role/change`, {
    method: 'POST',
    credentials: 'include',
    body: [...user.roles, role]
  });
  refreshNuxtData('otherUsers');
}
async function removeRole(user: ApplicationUser, role: ApplicationUserRole) {
  await $fetch(`${config.public.BACKEND_API}/user/${user.id}/role/change`, {
    method: 'POST',
    credentials: 'include',
    body: user.roles.filter((r) => r !== role)
  });
  refreshNuxtData('otherUsers');
}
</script>
<template>
  <div class="page__margin pt-8">
    <div v-if="authStatus.loggedIn && applicationUser" class="flex flex-col">
      <h1 class="text-3xl font-bold">Twoje konto</h1>
      <div>{{ `${applicationUser.firstName} ${applicationUser.lastName}` }}</div>
      <div>Nazwa użytkownika: {{ applicationUser.username }}</div>
      <div>Email: {{ applicationUser.email }}</div>
      <div class="flex flex-col">
        Role:
        <ul class="list-inside list-disc">
          <li v-for="role in applicationUser.roles">{{ roleToLabel(role) }}</li>
          <li v-if="applicationUser.roles.length == 0">Nie masz żadnej roli</li>
        </ul>
      </div>
      <div class="flex flex-col">
        Uprawnienia administratorskie:
        <ul class="list-inside list-disc">
          <li v-for="permission in applicationUser.permissions">{{ permissionToLabel(permission) }}</li>
          <li v-if="applicationUser.roles.length == 0">Nie masz żadnych uprawnień</li>
        </ul>
      </div>
    </div>
    <div v-if="applicationUser?.roles.includes('SUPER_ADMIN')">
      <h2 class="mt-2 font-bold">Inni użytkownicy</h2>
      <table>
        <tr v-for="user in otherUsers">
          <th class="border-2 px-1.5 py-1">{{ nameFromApplicationUser(user) }}</th>
          <td class="border-2 px-1.5 py-1">
            <span v-for="role in user.roles">
              <button v-if="modifiableRoles.includes(role)" @click="removeRole(user, role)">
                (
                <font-awesome-icon :icon="['fas', 'minus']" size="lg" />
                {{ roleToLabel(role) }})
              </button>
              <span v-else>
                {{ roleToLabel(role) }}
              </span>
            </span>
          </td>
          <td class="border-2 px-1.5 py-1">
            <button v-for="role in modifiableRoles.filter((r) => !user.roles.includes(r))" @click="addRole(user, role)">
              (<font-awesome-icon :icon="['fas', 'plus']" size="lg" /> {{ roleToLabel(role) }})
            </button>
          </td>
        </tr>
      </table>
    </div>
  </div>
</template>
