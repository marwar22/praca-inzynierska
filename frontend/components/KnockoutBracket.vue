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
</script>
<template>
  <div class="flex overflow-x-auto overflow-y-hidden">
    <div class="flex shrink-0 flex-col" v-for="stage in stages">
      <KnockoutMatch
        :match="kbMatch.match"
        :players="players"
        :stage="kbMatch.stage"
        v-for="(kbMatch, n) in stage"
        :bottom="n % 2 == 0"
        :last="kbMatch.id == knockoutBracket?.matches?.at(-1)?.id"
      />
    </div>
  </div>
</template>
