<script setup lang="ts">
import type { ApplicationUser } from '~/types/applicationuser';

definePageMeta({
  middleware: ['logged-in']
});

const config = useRuntimeConfig();
const authStatus = useAuthStatus();

const { data: applicationUser } = await useFetch<ApplicationUser>(`${config.public.BACKEND_API}/user/me`, {
  credentials: 'include'
});
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
  </div>
</template>
