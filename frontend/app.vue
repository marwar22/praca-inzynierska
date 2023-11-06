<script setup lang="ts">
const pages = [
  { href: '/', text: 'Home' },
  { href: '/ligi', text: 'Ligi' },
  { href: '/konto', text: 'Konto' }
];
</script>

<template>
  <div class="flex min-h-screen flex-col">
    <!-- <NuxtWelcome /> -->
    <div class="flex justify-between border-b-2">
      <h1 class="text-xl">LIGA</h1>
      <nav class="flex">
        <a v-for="page in pages" :href="page.href" class="px-2">{{ page.text }}</a>
      </nav>
    </div>
    <NuxtPage />
  </div>
</template>
