<script setup lang="ts">
import type LoginInputVue from '~/components/LoginInput.vue';
import type { ApiError } from '~/types/apierrror';
import type { AuthStatus } from '~/types/auth';

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
    const res = await $fetch<AuthStatus>(`${config.public.BACKEND_API}/auth/login`, {
      method: 'POST',
      body: {
        username: username.value,
        password: password.value
      },
      credentials: 'include'
    });
    authStatus.value = res;
    apiError.value = null;
    incorrectUsernameOrPassword.value = false;
    await navigateTo('/konto', { replace: true });
  } catch (error) {
    apiError.value = fetchErrorToApiError(error);
    if (apiError.value.errors['auth'] === 'Username or password is incorrect') {
      apiError.value = null;
      incorrectUsernameOrPassword.value = true;
    }
  }
  pending.value = false;
}

const loginInput = ref<InstanceType<typeof LoginInputVue>>();
onMounted(() => loginInput.value?.input?.focus());
</script>

<template>
  <div class="flex flex-1 flex-col items-center">
    <div class="flex-[3]"></div>
    <div
      class="flex w-96 min-w-[30%] flex-col items-stretch justify-center rounded-xl px-4 pb-8 pt-8 shadow-[0_2px_6px_0px_rgba(0,0,0,0.15),0_0_4px_-1px_rgba(0,0,0,0.4)]"
    >
      <h1 class="mb-3 mt-2 text-center text-3xl font-bold">Zaloguj się</h1>
      <LoginInput
        v-model="username"
        placeholder="Nazwa użytkownika, lub email"
        label="Nazwa użytkownika, lub email"
        ref="loginInput"
      />
      <LoginInput v-model="password" placeholder="Hasło" type="password" @enter="onLogin" label="Hasło" />

      <button
        @click="onLogin"
        class="mt-8 h-12 rounded-lg bg-olive-600 text-lg font-bold text-white outline-none ring-olive-800 hover:bg-olive-700 focus-visible:ring-2 active:bg-olive-800"
      >
        <font-awesome-icon v-if="pending" class="invisible mr-2" icon="fa-solid fa-arrows-rotate" />
        Zaloguj się
        <font-awesome-icon v-if="pending" class="ml-2" icon="fa-solid fa-arrows-rotate" spin />
      </button>
      <span v-if="incorrectUsernameOrPassword" class="text-left text-red-500">
        Niepoprawna nazwa użytkownika, lub hasło
      </span>
      <NuxtLink
        to="/reset-hasla"
        class="cursor-pointer pt-1 text-right underline-offset-2 hover:text-olive-700 hover:underline active:text-olive-600"
      >
        Zresetuj hasło
      </NuxtLink>
      <ApiError :api-error="apiError" />
    </div>
    <div class="flex-[5]"></div>
  </div>
</template>
