<script setup lang="ts">
import type { TournamentScoring } from '~/types/tournament';

const props = defineProps<{
  scoring: TournamentScoring;
  numberOfPlayersInKnockoutBracket: number;
}>();

const stages = computed(() => Array.from({ length: Math.log2(props.numberOfPlayersInKnockoutBracket) }, (_, i) => i));

watch(
  () => props.numberOfPlayersInKnockoutBracket,
  () => {
    const length = Math.log2(props.numberOfPlayersInKnockoutBracket);
    if (props.scoring.ratingForKnockoutStageParticipation.length > length)
      props.scoring.ratingForKnockoutStageParticipation.length = length;

    if (props.scoring.ratingForKnockoutStageParticipation.length == 0) {
      props.scoring.ratingForKnockoutStageParticipation.push(2);
      console.log('zero');
    }
    console.log(props.scoring.ratingForKnockoutStageParticipation);
    while (props.scoring.ratingForKnockoutStageParticipation.length < length)
      props.scoring.ratingForKnockoutStageParticipation.push(
        2 * props.scoring.ratingForKnockoutStageParticipation.at(-1)!
      );
  },
  { immediate: true }
);

function stageName(stage: number) {
  const stages = props.scoring.ratingForKnockoutStageParticipation.length;
  if (stage == stages - 1) return 'finał';
  if (stage == stages - 2) return 'półfinał';
  if (stage == stages - 3) return 'ćwierćfinał';
  return `${stage + 1} etap`;
}

const inputs: { [key in Exclude<keyof TournamentScoring, 'ratingForKnockoutStageParticipation'>]: { label: string } } =
  {
    groupPointsForWin: { label: 'Zwycięstwo' },
    groupPointsForLoss: { label: 'Przegrana (w tym krecz)' },
    groupPointsForWalkover: { label: 'Walkover' },
    ratingForMatchWin: { label: 'Wygrana' },
    ratingForMatchLoss: { label: 'Przegrana (w tym krecz)' },
    ratingForMatchWalkover: { label: 'Walkover' },
    ratingForTournamentParticipation: { label: 'Udział w rozgrywkach' },
    ratingForTournamentWin: { label: 'Zwycięstwo w rozgrywkach' }
  };

type Entries<T> = {
  [K in keyof T]: [K, T[K]];
}[keyof T][];

const inputsList = (Object.entries(inputs) as Entries<typeof inputs>).map(([field, input]) => {
  return { field, ...input };
});

const ratingInputs = inputsList.filter((input) => input.field.startsWith('rating'));
const groupInputs = inputsList.filter((input) => !input.field.startsWith('rating'));
</script>

<template>
  <h3 class="text-xl font-semibold">Punkty w grupie</h3>
  <div class="flex w-full flex-wrap max-md:flex-col">
    <label v-for="{ field, label } in groupInputs" class="mr-2 flex w-max flex-col">
      {{ label }}
      <ContestCreateInput :placeholder="label" type="number" v-model.number="scoring[field]" :min="0" :max="99" />
    </label>
  </div>
  <h3 class="text-xl font-semibold">Punkty do rankingu ogólnego</h3>
  <div class="flex w-full flex-wrap max-md:flex-col">
    <label v-for="{ field, label } in ratingInputs" class="mr-2 flex w-60 flex-col">
      {{ label }}
      <ContestCreateInput :placeholder="label" type="number" v-model.number="scoring[field]" :min="0" :max="999" />
    </label>
  </div>
  <!-- <label v-for="i in [stages]">{{ i }}</label>
    Udział w etapie -->
  <div class="mt-3 table__scrollbar table__scrollbar--olive  overflow-x-auto">
    <table class="w-max border-separate border-spacing-0 mb-1">
      <thead>
        <tr>
          <th class="rounded-tl-lg border-2 border-l-4 border-t-4 border-olive-500 px-3 py-1.5">Udział</th>
          <th
            class="bg-at border-2 border-t-4 border-olive-500 bg-champagne-300 px-3 py-1.5 last:rounded-tr-lg last:border-r-4"
            v-for="index in scoring.ratingForKnockoutStageParticipation.toReversed().keys()"
          >
            {{ stageName(index) }}
          </th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <th class="rounded-bl-lg border-2 border-b-4 border-l-4 border-olive-500 px-3 py-1.5">Etap</th>
          <td
            class="border-2 border-b-4 min-w-[7rem] border-olive-500 p-0 last:rounded-br-lg last:border-r-4"
            v-for="index in scoring.ratingForKnockoutStageParticipation.toReversed().keys()"
          >
            <ContestCreateInput
              v-model.number="scoring.ratingForKnockoutStageParticipation[index]"
              type="number"
              :placeholder="`Udział w ${stageName(index)}`"
              :min="1"
              :max="999"
              class="max-h-[2rem] w-full border-0 ring-transparent"
            />
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>
