<script setup lang="ts">
import type { ApplicationUser } from '~/types/applicationuser';
import type { KnockoutBracketMatch, Match, Tournament } from '~/types/tournament';

const props = defineProps<{
  tournament: Tournament;
  players: Map<number, ApplicationUser>;
}>();

const knockoutBracket = computed(() => props.tournament.knockoutBracket);

const stages = computed(() => {
  const stages: KnockoutBracketMatch[][] = [];
  for (const kbMatch of knockoutBracket.value?.matches ?? []) {
    while (stages.length <= kbMatch.stage) stages.push([]);
    stages[kbMatch.stage].push(kbMatch);
  }
  return stages;
});

const knockoutBracketMatches = computed(() => {
  const map = new Map<number, KnockoutBracketMatch>();
  for (const kbMatch of props.tournament.knockoutBracket?.matches ?? []) {
    map.set(kbMatch.id, kbMatch);
  }
  return map;
});

</script>
<template>
  <div class="flex overflow-x-auto overflow-y-hidden table__scrollbar table__scrollbar--champagne">
    <div class="flex shrink-0 flex-col" v-for="stage in stages">
      <KnockoutMatch
      v-for="(kbMatch, n) in stage"
        :kbMatch="kbMatch"
        :knockoutBracketMatches="knockoutBracketMatches"
        :players="players"
        :stage="kbMatch.stage"     
        :setsToWin="tournament.setsToWin"   
        :bottom="n % 2 == 0"
        :last="kbMatch.id == knockoutBracket?.matches?.at(-1)?.id"
      />
    </div>
  </div>
</template>
