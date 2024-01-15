<script setup lang="ts">
import type { ApiError } from '~/types/apierrror';

useSeoMeta({
  title: 'Rejestracja',
  description: 'Zarejestruj się się do rozgrywkitenisa.pl'
});

const config = useRuntimeConfig();

const pending = ref(false);
const apiError = ref(null as ApiError | null);
const username = ref('');
const email = ref('');
const firstName = ref('');
const lastName = ref('');
const password = ref('');
const passwordRepeated = ref('');

const showEmptyError = ref(false);

const isUsernameCorrect = computed(() => isBetween(4, username.value.length, 20));
const isEmailCorrect = computed(() =>
  /^[_A-Za-z0-9-+]+([.][_A-Za-z0-9-]+)*@[A-Za-z0-9-]+([.][A-Za-z0-9]+)*([.][A-Za-z]{2,})$/.test(email.value)
);
const isPasswordCorrect = computed(() => password.value.length > 0);
const arePasswordsMatching = computed(() => password.value === passwordRepeated.value);
const isEveryFieldCorrect = computed(() => {
  return username.value && isEmailCorrect.value && password.value && arePasswordsMatching.value;
});

const usernameError = computed(() => {
  if (username.value.trim().length == 0) return showEmptyError.value ? 'Nazwa użytkownika jest pusta' : '';
  return isBetween(4, username.value.length, 20) ? '' : 'Nazwa użytkownika musi mieć długość od 4 do 20 znaków';
});

const emailError = computed(() => {
  if (email.value.trim().length == 0) return showEmptyError.value ? 'Email jest pusty' : '';
  return /^[_A-Za-z0-9-+]+([.][_A-Za-z0-9-]+)*@[A-Za-z0-9-]+([.][A-Za-z0-9]+)*([.][A-Za-z]{2,})$/.test(email.value)
    ? ''
    : 'Email jest niepoprawny';
});

const firstNameError = computed(() => {
  if (firstName.value.trim().length == 0) return showEmptyError.value ? 'Imię jest puste' : '';
  return '';
});

const lastNameError = computed(() => {
  if (lastName.value.trim().length == 0) return showEmptyError.value ? 'Nazwisko jest puste' : '';
  return '';
});

const passwordError = computed(() => {
  if (password.value.trim().length == 0) return showEmptyError.value ? 'Hasło jest puste' : '';
  return '';
});
const passwordRepeatedError = computed(() => {
  if (passwordRepeated.value.trim().length == 0) return showEmptyError.value ? 'Powtórzone hasło jest puste' : '';
  return arePasswordsMatching.value ? '' : 'Hasła się nie zgadzają';
});

async function onRegister() {
  if (
    !username.value ||
    !email.value ||
    !firstName.value ||
    !lastName.value ||
    !password.value ||
    !passwordRepeated.value
  ) {
    showEmptyError.value = true;
    return;
  } else {
    showEmptyError.value = false;
  }
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

function errorMessage(field: string, emptyMessage: string, correctness: boolean, incorrectMessage: string) {
  if (field.trim().length == 0) return showEmptyError ? emptyMessage : '';
  return correctness ? '' : incorrectMessage;
}
</script>
<template>
  <div class="flex flex-1 flex-col items-center">
    <div class="flex-[3]"></div>
    <div
      class="flex w-96 min-w-[30%] flex-col items-stretch justify-center rounded-xl px-4 pb-8 pt-8 shadow-[0_2px_6px_0px_rgba(0,0,0,0.15),0_0_4px_-1px_rgba(0,0,0,0.4)]"
    >
      <h1 class="mb-3 mt-2 text-center text-3xl font-bold">Zarejestruj się</h1>

      <LoginInput v-model="username" placeholder="Nazwa użytkownika" label="Nazwa użytkownika" :error="usernameError" />
      <LoginInput v-model="email" placeholder="Email" type="email" label="Email" :error="emailError" />
      <div class="flex justify-stretch">
        <LoginInput v-model="firstName" placeholder="Imię" label="Imię" :error="firstNameError" />
        <div class="w-10"></div>
        <LoginInput v-model="lastName" placeholder="Nazwisko" label="Nazwisko" :error="lastNameError" />
      </div>
      <LoginInput v-model="password" placeholder="Hasło" type="password" label="Hasło" :error="passwordError" />
      <LoginInput
        v-model="passwordRepeated"
        placeholder="Powtórz hasło"
        type="password"
        label="Powtórz hasło"
        :error="passwordRepeatedError"
      />
      <button
        @click="onRegister()"
        class="mt-3 h-12 rounded-lg bg-olive-600 text-lg font-bold text-white outline-none ring-olive-800 hover:bg-olive-700 focus-visible:ring-2 active:bg-olive-800"
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
