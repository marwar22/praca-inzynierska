<script setup lang="ts">
import type { Match } from '~/types/tournament';

const props = defineProps<{
  firstPlayerId: number;
  secondPlayerId: number;
  organizerId: number;
  matches: Map<string, Match>;
  hoveredPlayerId: number;
}>();

const match = computed(() => {
  let match = props.matches.get(`${props.firstPlayerId}|${props.secondPlayerId}`);
  let inverse = false;
  if (!match) {
    match = props.matches.get(`${props.secondPlayerId}|${props.firstPlayerId}`)!;
    inverse = true;
  }
  return { ...match, inverse };
});
const matchScore = computed(() => {
  return (match.value?.result?.setResults ?? []).reduce(
    (matchResult, setResult) => [
      matchResult[0] + (setResult.firstPlayerScore > setResult.secondPlayerScore ? 1 : 0),
      matchResult[1] + (setResult.secondPlayerScore > setResult.firstPlayerScore ? 1 : 0)
    ],
    [0, 0]
  );
});

const hov1 = computed(() => props.hoveredPlayerId === props.firstPlayerId);
const hov2 = computed(() => props.hoveredPlayerId === props.secondPlayerId && false);
</script>

<template>
  <NuxtLink :to="`/mecz/${match.id}`" class="flex flex-col items-center justify-center px-1.5 py-1">
    <div v-if="match.result">
      <div v-if="!match.inverse" class="flex flex-col items-center justify-start">
        <span class="text-xl font-bold">
          <span :class="[{ [`text-atlantis-700`]: hov1 }]">{{ matchScore[0] }}</span
          >:<span :class="[{ [`text-atlantis-700`]: hov2 }]">{{ matchScore[1] }}</span>
        </span>
        <span class="text-center text-sm">
          <span v-for="(setResult, setNumber) in match.result.setResults" class="[&:last-child>*:nth-child(2)]:hidden">
            <span
              >(<span :class="[{ [`text-atlantis-700`]: hov1 }]">{{ setResult.firstPlayerScore }}</span
              >:<span :class="[{ [`text-atlantis-700`]: hov2 }]">{{ setResult.secondPlayerScore }}</span
              >)</span
            ><span>,&nbsp;</span>
            <br v-if="setNumber === 2" />
          </span>
        </span>
      </div>
      <div v-else class="flex flex-col items-center justify-center">
        <span class="text-xl font-bold">
          <span :class="[{ [`text-atlantis-700`]: hov1 }]">{{ matchScore[1] }}</span
          >:<span :class="[{ [`text-atlantis-700`]: hov2 }]">{{ matchScore[0] }}</span>
        </span>
        <span class="text-center text-sm">
          <span
            v-for="(setResult, setNumber) in match.result.setResults"
            class="[&:last-child>*:nth-child(2)]:hidden [&>span]:inline-block"
          >
            <span
              >(<span :class="[{ [`text-atlantis-700`]: hov1 }]">{{ setResult.secondPlayerScore }}</span
              >:<span :class="[{ [`text-atlantis-700`]: hov2 }]">{{ setResult.firstPlayerScore }}</span
              >)</span
            ><span>,&nbsp;</span>
            <br v-if="setNumber === 2" />
          </span>
        </span>
      </div>
    </div>
    <span v-else>Brak wyniku</span>
  </NuxtLink>
</template>
