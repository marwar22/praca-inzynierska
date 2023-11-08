<script setup lang="ts">
const pages = [
  { href: '/', text: 'Home' },
  { href: '/rozgrywki', text: 'Rozgrywki' },
  { href: '/ranking', text: 'Ranking' }
];
</script>

<template>
  <div class="flex min-h-screen flex-col font-montserrat">
    <!-- <NuxtWelcome /> -->
    <div class="flex h-12 items-center border-b-2 bg-atlantis-600">
      <h1 class="text-xl">LIGA</h1>
      <nav class="mr-3 flex flex-1 items-center">
        <NuxtLink v-for="page in pages" :to="page.href" class="px-2 text-lg text-white">{{ page.text }}</NuxtLink>
        <div class="flex-1"></div>
        <NuxtLink to="/logowanie" class="pr-2 text-white">Zaloguj się</NuxtLink>
        <NuxtLink to="/rejestracja" class="rounded-md border-4 border-white px-2 py-1 text-white"
          >Zarejestruj się</NuxtLink
        >
        <NuxtLink to="/konto">
          <font-awesome-icon icon="fa-solid fa-circle-user" size="2x" transform="" class="pl-2 text-white" />
        </NuxtLink>
      </nav>
    </div>
    <main class="flex w-full flex-1 flex-col">
      <NuxtPage />
    </main>
  </div>
</template>
