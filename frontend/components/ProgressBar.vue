<script setup lang="ts">
const props = defineProps<{
  completed: number;
  all: number;
  class: string;
}>();
const className = props.class;
const fraction = computed(() => {
  return props.completed / props.all;
});
const progressBarColor = computed(() => {
  if (fraction.value <= 0.1) return 'lightcoral';
  if (fraction.value <= 0.3) return 'lightsalmon';
  if (fraction.value <= 0.7) return 'palegoldenrod';
  return '#C9E08A';
});
</script>
<template>
  <div
    :class="`border-2 border-champagne-600 px-2 ${className}`"
    :style="`background: linear-gradient(to right, ${progressBarColor} ${fraction * 100}%, transparent 0)`"
  >
    {{ completed }}/{{ all }}
  </div>
</template>
