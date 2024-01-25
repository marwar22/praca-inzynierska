<script setup lang="ts">
const props = defineProps<{
  modelValue?: string | number;
  modelModifiers?: any;
  type?: string;
  placeholder?: string;
  error?: string | null;
  min?: number;
  max?: number;
}>();

const emit = defineEmits<{
  (e: 'update:modelValue', modelValue: string | number): void;
}>();
function emitValue(event: Event) {
  const eventTarget = event.target as HTMLInputElement;
  let value: string | number = eventTarget.value;
  if (props.modelModifiers.number) {
    let newValue = Number.parseFloat(value);
    if (!isNaN(newValue)) {
      if (props.max != null && newValue > props.max) newValue = props.max;
      if (props.min != null && newValue < props.min) newValue = props.min;

      value = newValue;
      if (props.modelValue === newValue) {
        eventTarget.value = newValue.toString();
      }
    }
  }
  emit('update:modelValue', value);
}
const input = ref<HTMLInputElement>();
</script>

<template>
  <input
    ref="input"
    class="my-1 h-12 rounded-lg border-4 border-olive-500 px-2 py-1 outline-none ring-olive-600 focus:ring-2"
    :class="error ? 'border-red-500 ring-red-600' : ''"
    v-bind="$attrs"
    :value="modelValue"
    @input="emitValue"
    :placeholder="placeholder"
    :type="type"
    :min="min"
    :max="max"
  />
  <span v-if="error" class="font-semibold text-red-500">{{ error }}</span>
</template>
