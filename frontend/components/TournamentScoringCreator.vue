<script setup lang="ts">
import type { TournamentScoring } from '~/types/tournament';

const props = defineProps<{ scoring: TournamentScoring }>();

const inputs: { [key in Exclude<keyof TournamentScoring, 'rankingForKnockoutStageParticipation'>]: { label: string } } =
  {
    groupPointsForWin: { label: 'Zwycięstwo' },
    groupPointsForLoss: { label: 'Przegrana (w tym krecz)' },
    groupPointsForWalkover: { label: 'Walkover' },
    rankingForMatchWin: { label: 'Wygrana' },
    rankingForMatchLoss: { label: 'Przegrana (w tym krecz)' },
    rankingForMatchWalkover: { label: 'Walkover' },
    rankingForTournamentParticipation: { label: 'Udział w rozgrywkach' },
    rankingForTournamentWin: { label: 'Zwycięstwo w całych rozgrywkach' }
  };

type Entries<T> = {
  [K in keyof T]: [K, T[K]];
}[keyof T][];

const inputsList = (Object.entries(inputs) as Entries<typeof inputs>).map(([field, input]) => {
  return { field, ...input };
});

const rankingInputs = inputsList.filter((input) => input.field.startsWith('ranking'));
const groupInputs = inputsList.filter((input) => !input.field.startsWith('ranking'));
</script>

<template>
  <h3 class="text-xl font-semibold">Punkty w grupie</h3>
  <div class="flex w-full flex-wrap max-md:flex-col">
    <label v-for="{ field, label } in groupInputs" class="flex w-max flex-col mr-2">
      {{ label }}
      <ContestCreateInput :placeholder="label" type="number" v-model.number="scoring[field]" :min="0" :max="99" />
    </label>
  </div>
  <h3 class="text-xl font-semibold">Punkty do rankingu ogólnego</h3>
  <div class="flex w-full flex-wrap max-md:flex-col">
    <label v-for="{ field, label } in rankingInputs" class="flex w-64 flex-col mr-2">
      {{ label }}
      <ContestCreateInput :placeholder="label" type="number" v-model.number="scoring[field]" :min="0" :max="999" />
    </label>
  </div>
</template>
