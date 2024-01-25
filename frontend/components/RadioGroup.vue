<script setup lang="ts" generic="T">
const props = defineProps<{
  modelValue: T;
  values: T[];
}>();

const emit = defineEmits<{
  (e: 'update:modelValue', modelValue: T): void;
}>();
</script>

<template>
  <div class="flex rounded-2xl" v-bind="$attrs">
    <RadioButton
      v-for="value in values"
      :model-value="props.modelValue"
      @update:model-value="(newValue) => $emit('update:modelValue', newValue)"
      :value="value"
      class="[&:first-child]:rounded-l-xl [&:first-child]:border-l-4 [&:last-child]:rounded-r-xl [&:last-child]:border-r-4"
    />
  </div>
</template>
