<script setup lang="ts">
import gsap from 'gsap';
import type { AuthStatus } from './types/auth';
import { faTowerObservation } from '@fortawesome/free-solid-svg-icons';

const config = useRuntimeConfig();
const authStatus = useAuthStatus();

$fetch<AuthStatus>(`${config.public.BACKEND_API}/auth/status`, { credentials: 'include' }).then((resAuthStatus) => {
  if (resAuthStatus) authStatus.value = resAuthStatus;
});

const pages = [
  { href: '/', text: 'Home' },
  { href: '/rozgrywki', text: 'Rozgrywki' },
  { href: '/ranking', text: 'Ranking' }
];
async function logOut() {
  await $fetch(`${config.public.BACKEND_API}/auth/logout`, { method: 'POST', credentials: 'include' });
  authStatus.value = {
    loggedIn: false,
    applicationUserId: -1,
    roles: [],
    username: ''
  };
}

const tweened = reactive({
  number: 0
});
let tween: gsap.core.Tween | null = null;
let targetNumber = 360;

function onTennisBallClick() {
  tweened.number = 0;
  if ((tween?.ratio ?? 1) == 1) {
    targetNumber = 360;
    tween = gsap.to(tweened, { duration: 0.5, number: targetNumber });
  } else if (tween?.ratio) {
    const currentNumber = tween.ratio * targetNumber;
    targetNumber = targetNumber + 720;

    let duration = 0.5 + Math.sqrt(((targetNumber - currentNumber) / 720)) * 0.5;
    tween = gsap.to(tweened, { duration, number: targetNumber });
    tween.ratio = currentNumber / targetNumber;
  }
}
</script>

<template>
  <div class="flex min-h-screen flex-col font-montserrat">
    <div class="flex h-12 items-center border-b-2 bg-atlantis-600">
      <NuxtLink to="/" class="pl-2 pr-1" @click="onTennisBallClick">
        <TennisBall :size="36" :style="`transform: rotate(${tweened.number - 20}deg)`" />
      </NuxtLink>
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
