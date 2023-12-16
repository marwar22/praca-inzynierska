<script setup lang="ts">
import type LoginInputVue from '~/components/LoginInput.vue';
import type { ApiError } from '~/types/apierrror';
import type { AuthStatus } from '~/types/auth';

const config = useRuntimeConfig();

const email = ref('');
const apiError = ref(null as ApiError | null);
const pending = ref(false);
const sentEmail = ref<string | null>(null);
async function onRequestResetPassword() {
  if (pending.value) return;
  pending.value = true;
  try {
    await $fetch(`${config.public.BACKEND_API}/auth/reset-password`, {
      method: 'POST',
      body: {
        email: email.value
      },
      credentials: 'include'
    });

    apiError.value = null;
    sentEmail.value = email.value;
  } catch (error) {
    apiError.value = fetchErrorToApiError(error);
    sentEmail.value = null;
  }
  pending.value = false;
}

const emailInput = ref<InstanceType<typeof LoginInputVue>>();
onMounted(() => emailInput.value?.input?.focus());
</script>

<template>
  <div class="flex flex-1 flex-col items-center">
    <div class="flex-[3]"></div>
    <div
      class="flex w-96 min-w-[30%] flex-col items-stretch justify-center rounded-xl px-4 pb-8 pt-8 shadow-[0_2px_6px_0px_rgba(0,0,0,0.15),0_0_4px_-1px_rgba(0,0,0,0.4)]"
    >
      <h1 class="mb-3 mt-2 text-center text-3xl font-bold">Zmiana hasła</h1>
      <LoginInput v-model="email" @enter="onRequestResetPassword" placeholder="Email" label="Email" ref="emailInput" />

      <button
        @click="onRequestResetPassword"
        class="mt-8 h-12 rounded-lg bg-olive-600 text-lg font-bold text-white outline-none ring-olive-800 hover:bg-olive-700 focus-visible:ring-2 active:bg-olive-800"
      >
        <font-awesome-icon v-if="pending" class="invisible mr-2" icon="fa-solid fa-arrows-rotate" />
        Wyślij link do zmiany hasła
        <font-awesome-icon v-if="pending" class="ml-2" icon="fa-solid fa-arrows-rotate" spin />
      </button>
      <span v-if="sentEmail"
        >Wysłano link do zmiany hasła na adres <b>{{ sentEmail }}</b>
      </span>
      <ApiError :api-error="apiError" />
    </div>
    <div class="flex-[5]"></div>
  </div>
</template>
