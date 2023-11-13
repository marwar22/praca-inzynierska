<script setup lang="ts">
const config = useRuntimeConfig();
const authStatus = useAuthStatus();
const pages = [
  { href: '/', text: 'Home' },
  { href: '/rozgrywki', text: 'Rozgrywki' },
  { href: '/ranking', text: 'Ranking' }
];
async function logOut() {
  const res = await $fetch(`${config.public.BACKEND_API}/auth/logout`, { method: 'POST', credentials: 'include' });
  authStatus.value.loggedIn = false;
}
</script>

<template>
  <div class="flex min-h-screen flex-col font-montserrat">
    <div class="flex h-12 items-center border-b-2 bg-atlantis-600">
      <h1 class="text-xl">LIGA</h1>
      <nav class="mr-3 flex h-full flex-1 items-center text-white">
        <NuxtLink v-for="page in pages" :to="page.href" class="px-2 text-lg">{{ page.text }}</NuxtLink>
        <div class="flex-1"></div>
        <div v-if="!authStatus.loggedIn" class="flex h-full items-center">
          <NuxtLink to="/logowanie" class="pr-2">Zaloguj się</NuxtLink>
          <NuxtLink to="/rejestracja" class="rounded-md border-4 border-white px-2 py-1"> Zarejestruj się </NuxtLink>
        </div>
        <div v-else class="">
          <span class="mr-2 font-bold"> {{ authStatus.username }}</span>
          <button class="rounded-md border-4 border-white px-2 py-1" @click="logOut">Wyloguj się</button>
        </div>
        <NuxtLink to="/konto">
          <font-awesome-icon icon="fa-solid fa-circle-user" size="2x" transform="" class="pl-2" />
        </NuxtLink>
      </nav>
    </div>
    <main class="flex w-full flex-1 flex-col">
      <NuxtPage />
    </main>
  </div>
</template>
