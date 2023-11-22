<script setup lang="ts">
import type { ApplicationUser } from '~/types/applicationuser';

definePageMeta({
  middleware: [
    'logged-in',
  ],
});


const config = useRuntimeConfig();
const authStatus = useAuthStatus();

const { data: applicationUser } = await useFetch<ApplicationUser>(`${config.public.BACKEND_API}/user/me`, {
  credentials: 'include'
});

</script>
<template>
  <div v-if="authStatus.loggedIn && applicationUser" class="flex flex-col">
    <h1 class="text-3xl font-bold">Twoje konto</h1>
    <div>{{ `${applicationUser.firstName} ${applicationUser.lastName}` }}</div>
    <div>Nazwa użytkownika: {{ applicationUser.username }}</div>
    <div>Email: {{ applicationUser.email }}</div>
  </div>
</template>
