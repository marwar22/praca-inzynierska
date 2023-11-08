<script setup lang="ts">
const props = defineProps<{
  modelValue?: string | number;
  modelModifiers?: any;
  type?: string;
  placeholder?: string;
  error?: string;
}>();
const emit = defineEmits<{
  (e: 'update:modelValue', modelValue: string | number): void;
}>();
function emitValue(event: Event) {
  let value: string | number = (event.target as HTMLInputElement).value;
  if (props.modelModifiers.number) {
    const newValue = Number.parseFloat(value);
    if (!isNaN(newValue)) value = newValue;
  }
  emit('update:modelValue', value);
}
</script>
<template>
  <input
    class="my-1 h-12 rounded-lg border-4 border-atlantis-500 px-2 py-1 outline-none ring-atlantis-600 focus:ring-2"
    :class="error ? 'border-red-500 ring-red-600' : ''"
    :value="modelValue"
    @input="emitValue"
    :placeholder="placeholder"
    :type="type"
  />
  <span v-if="error" class="font-semibold text-red-500">{{ error }}</span>
</template>
