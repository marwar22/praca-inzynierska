<script setup lang="ts">
import type { Match } from '~/types/tournament';

const authStatus = useAuthStatus();

const props = defineProps<{
  firstPlayerId: number;
  secondPlayerId: number;
  matches: Map<string, Match>;
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
</script>

<template>
  <div class="flex flex-col items-center justify-center">
    <div v-if="match.result">
      <div v-if="!match.inverse" class="flex flex-col items-center justify-start">
        <span class="text-xl font-bold">{{ matchScore[0] }}:{{ matchScore[1] }}</span>
        <span class="text-center text-sm">
          <span v-for="(setResult, setNumber) in match.result.setResults" class="[&:last-child>*:last-child]:hidden">
            <span>({{ setResult.firstPlayerScore }}:{{ setResult.secondPlayerScore }})</span><span>&nbsp;</span>
            <wbr v-if="setNumber === 2" />
          </span>
        </span>
      </div>
      <div v-else class="flex flex-col items-center justify-center">
        <span class="text-xl font-bold">{{ matchScore[1] }}:{{ matchScore[0] }}</span>
        <span class="text-center text-sm">
          <span v-for="(setResult, setNumber) in match.result.setResults" class="[&:last-child>*:last-child]:hidden">
            <span>({{ setResult.secondPlayerScore }}:{{ setResult.firstPlayerScore }})</span><span>,&nbsp;</span>
            <wbr v-if="setNumber === 2" />
          </span>
        </span>
      </div>
    </div>
    <div v-else>Brak wyniku</div>
    <NuxtLink
      :to="`/mecz/${match.id}`"
      v-if="[firstPlayerId, secondPlayerId].includes(authStatus.applicationUserId)"
      class="absolute right-1.5 hover:text-atlantis-700 active:text-atlantis-400"
      title="Edytuj"
    >
      <font-awesome-icon icon="fa-solid fa-file-pen" size="xl" class="pl-2" />
    </NuxtLink>
    <!-- <span class="text-xl font-bold">2:0</span>
<span>(6:7), (6:2)</span> -->
  </div>
</template>
