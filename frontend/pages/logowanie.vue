<script setup lang="ts">
import axios from 'axios';

const config = useRuntimeConfig();

const username = ref('');
const password = ref('');
async function onLogin() {
  const res = await axios.post(
    `${config.public.BACKEND_API}/auth/login`,
    {
      username: username.value,
      password: password.value
    },
    { withCredentials: true }
  );
}
</script>

<template>
  <div class="flex flex-1 flex-col items-center">
    <div class="flex-[3]"></div>
    <div class="flex w-96 min-w-[30%] flex-col items-stretch justify-center">
      <h1 class="mb-3 mt-2 text-center text-3xl font-bold">Zaloguj się</h1>

      <LoginInput v-model="username" placeholder="Nazwa użytkownika"></LoginInput>
      <LoginInput v-model="password" placeholder="Hasło" type="password"></LoginInput>

      <button
        @click="onLogin()"
        class="my-1 h-12 rounded-lg bg-atlantis-600 text-lg font-bold text-white outline-none ring-atlantis-800 hover:bg-atlantis-700 focus-visible:ring-2 active:bg-atlantis-800"
      >
        Zaloguj się
      </button>
    </div>
    <div class="flex-[5]"></div>
  </div>
</template>
