<script setup lang="ts">
import type { ApplicationUser } from '~/types/applicationuser';

const config = useRuntimeConfig();

const { data: applicationUser } = await useFetch<ApplicationUser>(`${config.public.BACKEND_API}/user/me`, {
  credentials: 'include'
});
const authStatus = useAuthStatus();
</script>
<template>
  {{ JSON.stringify(authStatus) }}
  <div v-if="applicationUser" class="flex flex-col">
    <h1 class="text-3xl font-bold">Twoje konto</h1>
    <div>{{ `${applicationUser.firstName} ${applicationUser.lastName}` }}</div>
    <div>Nazwa użytkownika: {{ applicationUser.username }}</div>
    <div>Email: {{ applicationUser.email }}</div>
  </div>
</template>
