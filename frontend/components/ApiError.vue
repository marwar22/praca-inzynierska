<script setup lang="ts">
import type { ApiError } from '~/types/apierrror';

defineProps<{
  apiError: ApiError | null;
}>();
</script>

<template>
  <div v-if="apiError" class="rounded-xl border-4 border-red-500 p-2 text-red-500 my-2">
    <span class="font-bold">Error status: </span>{{ apiError.status }}
    <ul class="list-inside list-disc">
      <li v-for="(value, key) in apiError.errors">
        <span class="font-bold">{{ key }}:</span>
        {{ value }}
      </li>
    </ul>
  </div>
</template>
