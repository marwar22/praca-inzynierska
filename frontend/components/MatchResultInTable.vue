<script setup lang="ts">
import type { Match } from '~/types/tournament';

const authStatus = useAuthStatus();

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

const canEdit = computed(() => {
  return [props.firstPlayerId, props.secondPlayerId, props.organizerId].includes(authStatus.value.applicationUserId);
});

const hov1 = computed(() => props.hoveredPlayerId === props.firstPlayerId);
const hov2 = computed(() => props.hoveredPlayerId === props.secondPlayerId);
</script>

<template>
  <div class="flex flex-col items-center justify-center">
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
            <wbr v-if="setNumber === 2" />
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
            <wbr v-if="setNumber === 2" />
          </span>
        </span>
      </div>
    </div>
    <div v-else>Brak wyniku</div>
    <NuxtLink
      :to="`/mecz/${match.id}`"
      v-if="canEdit"
      class="absolute right-1.5 top-1.5 hover:text-atlantis-700 active:text-atlantis-400"
      title="Edytuj"
    >
      <font-awesome-icon icon="fa-solid fa-file-pen" size="xl" class="pl-2" />
    </NuxtLink>
    <div
      v-else-if="authStatus.loggedIn"
      title="Edytować mogą tylko zawodnicy, lub organizator"
      class="absolute right-1.5 top-1.5 cursor-not-allowed text-gray-300"
    >
      <font-awesome-icon icon="fa-solid fa-file-pen" size="xl" class="pl-2" />
    </div>
  </div>
</template>
