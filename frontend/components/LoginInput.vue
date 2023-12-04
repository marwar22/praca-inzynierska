<script setup lang="ts">
defineProps<{
  modelValue?: string;
  type?: string;
  placeholder?: string;
  label?: string;
  error?: string;
}>();
const emit = defineEmits<{
  (e: 'update:modelValue', modelValue: string): void;
  (e: 'enter'): void;
}>();

function onKeyPress(e: KeyboardEvent) {
  if (e.key === 'Enter') emit('enter');
}
const input = ref<HTMLInputElement>();
defineExpose({ input });
</script>
<template>
  <label class="flex flex-col">
    <span class="pl-0.5"> {{ label }} </span>
    <input
      class="mb-1 h-12 w-full rounded-lg border-4 border-olive-500 px-2 py-1 outline-none ring-olive-600 focus:ring-2"
      :class="error ? 'border-red-500 ring-red-600' : ''"
      :value="modelValue"
      @input="$emit('update:modelValue', ($event.target as HTMLInputElement).value)"
      @keypress="onKeyPress"
      :placeholder="placeholder"
      :type="type"
      ref="input"
    />
    <span v-if="error" class="font-semibold text-red-500">{{ error }}</span>
  </label>
</template>
