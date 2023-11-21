<script setup lang="ts">
import gsap from 'gsap';
import type { transform } from 'typescript';
import TennisBall from '~/components/TennisBall.vue';

const tweened = reactive({
  number: 0
});

function onTennisBallClick() {
  tweened.number = 0;
  gsap.to(tweened, { duration: 0.5, number: 360 });
}
</script>
<template>
  <!-- Generated with https://svgwave.in/ -->
  <!-- Wygenerowane za pomocą https://svgwave.in/ -->
  <div class="absolute left-0 right-0 top-12 -z-10 w-screen overflow-x-hidden">
    <div class="min-w-[107rem]">
      <svg
        width="100%"
        height="100%"
        id="svg"
        viewBox="0 0 1440 490"
        class="transition delay-150 duration-300 ease-in-out"
        version="1.1"
        xmlns="http://www.w3.org/2000/svg"
        xmlns:svg="http://www.w3.org/2000/svg"
      >
        <defs id="defs3" />
        <path
          d="m 1440,-1.0454814e-5 c 0,0 0,410.736500454814 0,410.736500454814 -53.8468,2.15146 -107.6936,4.30293 -175,-2.1906 -67.3064,-6.49352 -148.0725,-21.63202 -216,-12.04826 -67.92752,9.58375 -123.01649,43.88978 -193,56.95545 -69.98351,13.06569 -154.86156,4.89103 -222,-16.42946 C 566.86156,415.70313 517.46273,381.23683 439,383.35406 360.5373,385.47128 253.0106,424.17204 175,434.83303 96.9894,445.49402 48.4947,428.11526 0,410.73649 0,410.73649 0,-1.0454814e-5 0,-1.0454814e-5 Z"
          stroke="none"
          stroke-width="0"
          fill="#799f2d"
          fill-opacity="0.4"
          class="path-0 transition-all delay-150 duration-300 ease-in-out"
          id="path1"
        />
        <path
          d="m 1440,-1.9999999e-5 c 0,0 0,284.471439999999 0,284.471439999999 -59.754,6.80699 -119.5081,13.61397 -193,14.79252 -73.4919,1.17854 -160.7217,-3.27138 -231,4.55154 -70.27825,7.82292 -123.60495,27.91865 -180,26.17137 C 779.60495,328.23957 720.14153,304.64926 656,284.47142 591.85847,264.29359 523.03882,247.52825 438,259.43794 352.9612,271.34763 251.7032,311.93235 176,320.88376 100.2968,329.83518 50.1484,307.1533 0,284.47142 0,284.47142 0,-1.9999999e-5 0,-1.9999999e-5 Z"
          stroke="none"
          stroke-width="0"
          fill="#799f2d"
          fill-opacity="0.53"
          class="path-1 transition-all delay-150 duration-300 ease-in-out"
          id="path2"
        />
        <path
          d="m 1440,1e-5 c 0,0 0,160.89679 0,160.89679 -69.5088,-22.39886 -139.0175,-44.79772 -198,-33.46653 -58.9825,11.33121 -107.4387,56.39248 -181,51.48698 -73.56132,-4.9055 -172.22776,-59.77778 -255,-70.79459 -82.77224,-11.016811 -149.65029,21.82183 -209,28.31784 -59.34971,6.496 -111.17108,-13.35063 -179,-12.87175 -67.8289,0.47888 -151.6654,21.28326 -224,30.89219 C 121.6654,164.06986 60.8327,162.48335 0,160.8968 0,160.8968 0,1e-5 0,1e-5 Z"
          stroke="none"
          stroke-width="0"
          fill="#799f2d"
          fill-opacity="1"
          class="path-2 transition-all delay-150 duration-300 ease-in-out"
          id="path3"
        />
      </svg>
    </div>
  </div>
  <div class="page__padding flex-1">
    <div class="flex flex-col">
      <h1 class="mt-10 text-4xl font-bold text-white">Rozgrywki tenisa ziemnego</h1>
      <span class="mt-1 text-xl font-semibold text-white"
        >System zarządzania rozgrywkami, planowania meczów,<br />obliczania wyników i rankingu</span
      >
      <div class="relative mt-16 flex">
        <div class="flex">
          <NuxtLink
            to="/rozgrywki"
            class="mr-5 flex w-40 items-center justify-center rounded-lg bg-champagne-600 px-2 py-2.5 font-bold text-white hover:bg-champagne-700 active:bg-champagne-800"
            >Rozgrywki</NuxtLink
          >
          <NuxtLink
            to="/ranking"
            class="flex w-40 items-center justify-center rounded-lg bg-white px-2 py-2.5 font-bold text-champagne-700 hover:bg-neutral-100 active:bg-neutral-200 hover:text-champagne-800 active:text-champagne-900"
          >
            Ranking
          </NuxtLink>
        </div>
        <button
          class="absolute bottom-0 right-0 flex h-36 -rotate-[20deg] items-center justify-center outline-none"
          :style="`transform: rotate(${tweened.number - 20}deg)`"
          @click="onTennisBallClick"
        >
          <TennisBall />
        </button>
      </div>
    </div>
  </div>
</template>
