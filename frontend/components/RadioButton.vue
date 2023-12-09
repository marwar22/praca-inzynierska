<script setup lang="ts" generic="T">
const props = defineProps<{
  modelValue: T;
  value: T;
}>();

const emit = defineEmits<{
  (e: 'update:modelValue', modelValue: T): void;
}>();
function selectValue() {
  emit('update:modelValue', props.value);
}

function onClick() {
  selectValue();
}

function onKeyPress(e: KeyboardEvent) {
  if (e.key === ' ') selectValue();
}
</script>
<template>
  <div
    tabindex="0"
    type="radio"
    @click="onClick"
    @keypress="onKeyPress"
    class="flex w-10 h-10 cursor-pointer items-center justify-center border-x-2 border-y-4 border-olive-600 px-2 py-2 text-white outline-none ring-olive-700 focus-visible:z-[1] focus-visible:ring-2"
    :class="[
      modelValue === value
        ? 'bg-olive-600 font-semibold active:bg-olive-700'
        : 'bg-olive-400 hover:bg-olive-500 active:bg-olive-700'
    ]"
  >
    {{ value }}
  </div>
</template>
