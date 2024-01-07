<script setup lang="ts">
import type { ApplicationUserBasic } from '~/types/applicationuser';
import type { KnockoutBracket, KnockoutBracketMatch } from '~/types/tournament';

const props = defineProps<{
  knockoutBracket: KnockoutBracket | null;
  setsToWin: number;
  players: Map<number, ApplicationUserBasic>;
  showEdit: boolean;
}>();

const stages = computed(() => {
  const stages: KnockoutBracketMatch[][] = [];
  for (const kbMatch of props.knockoutBracket?.matches ?? []) {
    while (stages.length <= kbMatch.stage) stages.push([]);
    stages[kbMatch.stage].push(kbMatch);
  }
  return stages;
});

const knockoutBracketMatches = computed(() => {
  const map = new Map<number, KnockoutBracketMatch>();
  for (const kbMatch of props.knockoutBracket?.matches ?? []) {
    map.set(kbMatch.id, kbMatch);
  }
  return map;
});
</script>
<template>
  <div class="table__scrollbar table__scrollbar--champagne flex overflow-x-auto overflow-y-hidden">
    <div class="flex shrink-0 flex-col" v-for="stage in stages">
      <KnockoutMatch
        v-for="(kbMatch, n) in stage"
        :kbMatch="kbMatch"
        :knockoutBracketMatches="knockoutBracketMatches"
        :players="players"
        :stage="kbMatch.stage"
        :setsToWin="setsToWin"
        :bottom="n % 2 == 0"
        :last="kbMatch.id == knockoutBracket?.matches?.at(-1)?.id"
        :showEdit="showEdit"
      />
    </div>
  </div>
</template>
