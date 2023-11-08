<script setup lang="ts">
import axios from 'axios';

const config = useRuntimeConfig();

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
  if (!isEveryFieldCorrect) return false;
  const res = await axios.post(`${config.public.BACKEND_API}/auth/register`, {
    username: username.value,
    email: email.value,
    firstName: firstName.value,
    lastName: lastName.value,
    password: password.value
  });
  console.log(res.data);
}
</script>
<template>
  <div class="flex flex-1 flex-col items-center">
    <div class="flex-[3]"></div>
    <div class="flex w-96 min-w-[30%] flex-col items-stretch justify-center">
      <h1 class="mb-3 mt-2 text-center text-3xl font-bold">Zarejestruj się</h1>

      <LoginInput v-model="username" placeholder="Nazwa użytkownika" />
      <LoginInput v-model="email" placeholder="Email" type="email" />
      <div class="flex justify-stretch">
        <LoginInput v-model="firstName" placeholder="Imię" />
        <div class="w-10"></div>
        <LoginInput v-model="lastName" placeholder="Nazwisko" />
      </div>
      <LoginInput v-model="password" placeholder="Hasło" type="password" />
      <LoginInput
        v-model="passwordRepeated"
        placeholder="Powtórz hasło"
        type="password"
        :error="passwordRepeated && !arePasswordsMatching ? 'Hasła się nie zgadzają' : ''"
      />
      <button
        @click="onRegister()"
        class="my-1 h-12 rounded-lg bg-atlantis-600 text-lg font-bold text-white outline-none ring-atlantis-800 hover:bg-atlantis-700 focus-visible:ring-2 active:bg-atlantis-800"
      >
        Zarejestruj
      </button>
    </div>
    <div class="flex-[5]"></div>
  </div>
</template>
