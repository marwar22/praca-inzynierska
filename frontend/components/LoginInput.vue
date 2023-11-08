<script setup lang="ts">
defineProps<{
  modelValue?: string;
  type?: string;
  placeholder?: string;
  error?: string;
}>();
const emit = defineEmits<{
  (e: 'update:modelValue', modelValue: string): void;
}>();
</script>
<template>
  <input
    class="my-1 h-12 rounded-lg border-4 border-atlantis-500 px-2 py-1 outline-none ring-atlantis-600 focus:ring-2 w-full"
    :class="error ? 'border-red-500 ring-red-600' : ''"
    :value="modelValue"
    @input="$emit('update:modelValue', ($event.target as HTMLInputElement).value)"
    :placeholder="placeholder"
    :type="type"
  />
  <span v-if="error" class="font-semibold text-red-500">{{ error }}</span>
</template>
