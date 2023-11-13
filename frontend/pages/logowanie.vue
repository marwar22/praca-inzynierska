<script setup lang="ts">
import axios from 'axios';
import type { ApiError } from '~/types/apierrror';

const config = useRuntimeConfig();
const authStatus = useAuthStatus();

const username = ref('');
const password = ref('');
const apiError = ref(null as ApiError | null);
const pending = ref(false);
const incorrectUsernameOrPassword = ref(false);

async function onLogin() {
  if (pending.value) return;
  pending.value = true;
  try {
    const res = await axios.post(
      `${config.public.BACKEND_API}/auth/login`,
      {
        username: username.value,
        password: password.value
      },
      { withCredentials: true }
    );
    authStatus.value = await res.data;
    apiError.value = null;
    incorrectUsernameOrPassword.value = false;
    await navigateTo('/konto');
  } catch (error) {
    apiError.value = await errorToApiError(error);
    if (apiError.value.errors['auth'] === 'Username or password is incorrect') {
      apiError.value = null;
      incorrectUsernameOrPassword.value = true;
    }
  }
  pending.value = false;
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
        <font-awesome-icon v-if="pending" class="invisible mr-2" icon="fa-solid fa-arrows-rotate" />
        Zaloguj się
        <font-awesome-icon v-if="pending" class="ml-2" icon="fa-solid fa-arrows-rotate" spin />
      </button>
      <span v-if="incorrectUsernameOrPassword" class="text-left text-red-500">
        Niepoprawna nazwa użytkownika, lub hasło
      </span>
      <ApiError :api-error="apiError"></ApiError>
    </div>
    <div class="flex-[5]"></div>
  </div>
</template>
