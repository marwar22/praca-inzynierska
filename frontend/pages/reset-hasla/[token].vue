<script setup lang="ts">
import type { ApiError } from '~/types/apierrror';

const route = useRoute();
const config = useRuntimeConfig();

const password = ref('');
const passwordRepeated = ref('');

const apiError = ref(null as ApiError | null);
const pending = ref(false);

const showEmptyError = ref(false);

const arePasswordsMatching = computed(() => password.value === passwordRepeated.value);
const passwordError = computed(() => {
  if (password.value.length == 0) return showEmptyError.value ? 'Hasło jest puste' : '';
  if (!/[a-z]/.test(password.value)) return 'Hasło powinno zawierać małą literę';
  if (!/[A-Z]/.test(password.value)) return 'Hasło powinno zawierać dużą literę';
  if (!/[!@#$%^&*()_\-+=\[\]{};:'"\\|,<\.>\/?]/.test(password.value))
    return 'Hasło powinno zawierać znak specjalny';
  if (!/\d/.test(password.value)) return 'Hasło powinno zawierać cyfrę';
  if (password.value.length < 9) return 'Hasło powinno składać się z co najmniej 9 znaków';
  return '';
});
const passwordRepeatedError = computed(() => {
  if (passwordRepeated.value.trim().length == 0) return showEmptyError.value ? 'Powtórzone hasło jest puste' : '';
  return arePasswordsMatching.value ? '' : 'Hasła się nie zgadzają';
});
async function onResetPassword() {
  if (pending.value) return;
  pending.value = true;
  try {
    await $fetch(`${config.public.BACKEND_API}/auth/reset-password/${route.params.token}`, {
      method: 'POST',
      body: {
        password: password.value
      },
      credentials: 'include'
    });
    apiError.value = null;
    await navigateTo('/logowanie', { replace: true });
  } catch (error) {
    apiError.value = fetchErrorToApiError(error);
  }
  pending.value = false;
}
</script>
<template>
  <div class="flex flex-1 flex-col items-center">
    <div class="flex-[3]"></div>
    <div
      class="flex w-96 min-w-[30%] flex-col items-stretch justify-center rounded-xl px-4 pb-8 pt-8 shadow-[0_2px_6px_0px_rgba(0,0,0,0.15),0_0_4px_-1px_rgba(0,0,0,0.4)]"
    >
      <h1 class="mb-3 mt-2 text-center text-3xl font-bold">Reset hasła</h1>
      <LoginInput v-model="password" @enter="onResetPassword()" placeholder="Hasło" type="password" label="Hasło" :error="passwordError" />
      <LoginInput
        v-model="passwordRepeated"
        @enter="onResetPassword()"
        placeholder="Powtórz hasło"
        type="password"
        label="Powtórz hasło"
        :error="passwordRepeatedError"
      />
      <button
        @click="onResetPassword()"
        class="mt-3 h-12 rounded-lg bg-olive-600 text-lg font-bold text-white outline-none ring-olive-800 hover:bg-olive-700 focus-visible:ring-2 active:bg-olive-800"
      >
        <font-awesome-icon v-if="pending" class="invisible mr-2" icon="fa-solid fa-arrows-rotate" />
        Ustaw nowe hasło
        <font-awesome-icon v-if="pending" class="ml-2" icon="fa-solid fa-arrows-rotate" spin />
      </button>
      <ApiError :api-error="apiError" />
    </div>
    <div class="flex-[5]"></div>
  </div>
</template>
