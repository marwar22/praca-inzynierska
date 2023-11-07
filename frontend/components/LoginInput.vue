import type { stringify } from 'postcss';
<script setup lang="ts">
defineProps<{
  modelValue?: string;
  type?: string;
  placeholder?: string;
  error?: string;
}>();
const emit = defineEmits<{
  (e: 'update', modelValue: string): void;
}>();
</script>
<template>
  <input
    class="border-atlantis-500 ring-atlantis-600 my-1 h-12 rounded-lg border-4 px-2 py-1 outline-none focus:ring-2"
    :class="error ? 'border-red-500 ring-red-600' : ''"
    :value="modelValue"
    @input="$emit('update:modelValue', $event.target.value)"
    :placeholder="placeholder"
    :type="type"
  />
  <span v-if="error" class="text-red-500 font-semibold">{{error}}</span>
</template>
