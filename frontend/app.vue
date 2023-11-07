<script setup lang="ts">
const pages = [
  { href: '/', text: 'Home' },
  { href: '/ligi', text: 'Ligi' },
  { href: '/ranking', text: 'Ranking' }
];
</script>

<template>
  <div class="flex min-h-screen flex-col font-montserrat">
    <!-- <NuxtWelcome /> -->
    <div class="flex h-12 items-center border-b-2 bg-atlantis-600">
      <h1 class="text-xl">LIGA</h1>
      <nav class="mr-3 flex flex-1 items-center">
        <a v-for="page in pages" :href="page.href" class="px-2 text-white">{{ page.text }}</a>
        <div class="flex-1"></div>
        <a href="/logowanie" class="pr-2 text-white">Zaloguj się</a>
        <a href="/rejestracja" class="rounded-md border-4 border-white px-2 py-1 text-white">Zarejestruj się</a>
        <a href="/konto">
          <font-awesome-icon icon="fa-solid fa-circle-user" size="2x" transform="" class="pl-2 text-white" />
        </a>
      </nav>
    </div>
    <NuxtPage />
  </div>
</template>
