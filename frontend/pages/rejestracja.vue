<script setup lang="ts">
import axios from 'axios';
import type { ApiError } from '~/types/apierrror';

const config = useRuntimeConfig();

const pending = ref(false);
const apiError = ref(null as ApiError | null);
const username = ref('');
const email = ref('');
const firstName = ref('');
const lastName = ref('');
const password = ref('');
const passwordRepeated = ref('');

const arePasswordsMatching = computed(() => {
  return password.value === passwordRepeated.value;
});
const isEveryFieldCorrect = computed(() => {
  return username.value && email.value && password.value && arePasswordsMatching.value;
});
async function onRegister() {
  if (!isEveryFieldCorrect || !arePasswordsMatching) return;
  try {
    const { data, error, status } = await useFetch(`${config.public.BACKEND_API}/auth/register`, {
      method: 'POST',
      body: {
        username: username.value,
        email: email.value,
        firstName: firstName.value,
        lastName: lastName.value,
        password: password.value
      }
    });
    if (status.value === 'error') {
      apiError.value = fetchErrorToApiError(error.value);
      return;
    }
    apiError.value = null;

    navigateTo('/logowanie');
  } catch (error) {
    apiError.value = fetchErrorToApiError(error);
  }
}
</script>
<template>
  <div class="flex flex-1 flex-col items-center">
    <div class="flex-[3]"></div>
    <div class="flex w-96 min-w-[30%] flex-col items-stretch justify-center">
      <h1 class="mb-3 mt-2 text-center text-3xl font-bold">Zarejestruj się</h1>

      <LoginInput v-model="username" placeholder="Nazwa użytkownika" label="Nazwa użytkownika" />
      <LoginInput v-model="email" placeholder="Email" type="email" label="Email" />
      <div class="flex justify-stretch">
        <LoginInput v-model="firstName" placeholder="Imię" label="Imię" />
        <div class="w-10"></div>
        <LoginInput v-model="lastName" placeholder="Nazwisko" label="Nazwisko" />
      </div>
      <LoginInput v-model="password" placeholder="Hasło" type="password" label="Hasło" />
      <LoginInput
        v-model="passwordRepeated"
        placeholder="Powtórz hasło"
        type="password"
        label="Powtórz hasło"
        :error="passwordRepeated && !arePasswordsMatching ? 'Hasła się nie zgadzają' : ''"
      />
      <button
        @click="onRegister()"
        class="mt-3 h-12 rounded-lg bg-atlantis-600 text-lg font-bold text-white outline-none ring-atlantis-800 hover:bg-atlantis-700 focus-visible:ring-2 active:bg-atlantis-800"
      >
        <font-awesome-icon v-if="pending" class="invisible mr-2" icon="fa-solid fa-arrows-rotate" />
        Zarejestruj się
        <font-awesome-icon v-if="pending" class="ml-2" icon="fa-solid fa-arrows-rotate" spin />
      </button>
      <ApiError :api-error="apiError" />
    </div>
    <div class="flex-[5]"></div>
  </div>
</template>
