<script setup lang="ts">
import gsap from 'gsap';
import type { AuthStatus } from './types/auth';

useHead({
  htmlAttrs: { lang: 'pl' }
});

const route = useRoute();
const config = useRuntimeConfig();
const authStatus = useAuthStatus();

$fetch<AuthStatus>(`${config.public.BACKEND_API}/auth/status`, { credentials: 'include' }).then((resAuthStatus) => {
  if (resAuthStatus) authStatus.value = resAuthStatus;
});

const pages = [
  { href: '/', text: 'Home', icon: 'home' },
  { href: '/rozgrywki', text: 'Rozgrywki', icon: 'network-wired', rotation: 90 },
  { href: '/ranking', text: 'Ranking', icon: 'arrow-up-1-9' }
];
async function logOut() {
  await $fetch(`${config.public.BACKEND_API}/auth/logout`, { method: 'POST', credentials: 'include' });
  authStatus.value = {
    loggedIn: false,
    username: '',
    applicationUserId: -1,
    roles: [],
    permissions: []
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

    let duration = 0.5 + Math.sqrt((targetNumber - currentNumber) / 720) * 0.5;
    tween = gsap.to(tweened, { duration, number: targetNumber });
    tween.ratio = currentNumber / targetNumber;
  }
}

const expanded = ref(false);

const currPage = computed(() => {
  const routeName = route.name?.toString();
  if (routeName?.startsWith('rozgrywki')) return 1;
  if (routeName?.startsWith('mecz')) return 1;
  if (routeName?.startsWith('ranking')) return 2;
  return 0;
});
</script>
<template>
  <div class="flex min-h-screen flex-col overflow-x-hidden font-montserrat">
    <div class="flex h-12 items-center border-b-2 bg-olive-600">
      <NuxtLink to="/" class="pl-2 pr-1 max-md:hidden" @click="onTennisBallClick" aria-label="Home - ikona piłki">
        <TennisBall :size="36" :style="`transform: rotate(${tweened.number - 20}deg)`" />
      </NuxtLink>
      <button
        @click="expanded = !expanded"
        class="flex h-full items-center justify-center px-4 text-white md:hidden"
        aria-label="menu"
      >
        <Transition mode="out-in">
          <font-awesome-icon v-if="expanded" :icon="['fas', 'xmark']" size="2xl" />
          <font-awesome-icon v-else :icon="['fas', 'bars']" size="2xl" />
        </Transition>
      </button>
      <nav class="mr-3 flex h-full flex-1 items-center text-white">
        <ul
          :class="[
            'z-50 flex border-olive-600',
            'max-md:absolute max-md:left-0 max-md:top-12 max-md:min-h-[calc(100vh-3rem)] max-md:w-60 max-md:flex-col max-md:border-r-4 max-md:bg-white max-md:text-black',
            'md:flex',
            { hidden: !expanded }
          ]"
        >
          <li v-for="(page, index) in pages" class="w-full text-lg">
            <NuxtLink
              :to="page.href"
              :class="[
                'before:bg-primary relative before:absolute before:bottom-1 before:left-0 before:mx-2 before:h-[0.1875rem] before:w-0 before:content-[\'test\'] md:before:hover:w-[calc(100%_-_1rem)] md:before:hover:bg-white',
                'md:ease-[cubic-bezier(0.215,_0.61,_0.355,_1]  md:before:transition-[width] md:before:duration-[250ms] md:before:ease-in-out',
                currPage == index ? 'md:before:w-[calc(100%_-_1rem)]  md:before:bg-white' : '',
                'block w-full px-2 py-1.5 font-semibold ',
                currPage == index
                  ? 'max-md:bg-olive-400 max-md:hover:bg-olive-500 max-md:active:bg-olive-600'
                  : 'max-md:hover:bg-olive-400 max-md:active:bg-olive-600'
              ]"
            >
              <font-awesome-icon :icon="['fas', page.icon]" :rotation="page.rotation" class="md:hidden" />
              {{ page.text }}
            </NuxtLink>
          </li>
        </ul>
        <div class="flex-1"></div>
        <div v-if="!authStatus.loggedIn" class="flex h-full items-center">
          <NuxtLink to="/logowanie" class="pr-2">Zaloguj się</NuxtLink>
          <NuxtLink to="/rejestracja" class="rounded-md border-4 border-white px-2 py-1"> Zarejestruj się </NuxtLink>
        </div>
        <div v-else class="">
          <span class="mr-2 font-bold overflow-ellipsis"> {{ authStatus.username }}</span>
          <button class="rounded-md border-4 border-white px-2 py-1" @click="logOut">Wyloguj się</button>
        </div>
        <NuxtLink to="/konto" aria-label="Konto">
          <font-awesome-icon icon="fa-solid fa-circle-user" size="2x" transform="" class="pl-2" />
        </NuxtLink>
      </nav>
    </div>
    <main class="flex w-full flex-1 flex-col">
      <NuxtPage />
    </main>
  </div>
</template>

<style>
.v-enter-active {
  transition: transform 150ms cubic-bezier(0.5, 1, 0.89, 1); /* easeOutQuad */
}
.v-leave-active {
  transition: transform 150ms cubic-bezier(0.11, 0, 0.5, 0); /* easeInQuad */
}

.v-enter-from {
  transform: rotate(-180deg);
}

.v-leave-to {
  transform: rotate(180deg);
}
</style>
