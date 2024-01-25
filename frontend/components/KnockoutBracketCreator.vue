<script setup lang="ts">
import type { ApplicationUserBasic } from '~/types/applicationuser';
import type { KnockoutBracket, Match } from '~/types/tournament';

const props = defineProps<{
  knockoutBracket: KnockoutBracket;
  numberOfPlayersInKnockoutBracket: number;
  setsToWin: number;
  selectedApplicationUsers: ApplicationUserBasic[];
}>();

const emit = defineEmits<{
  (e: 'update:knockoutBracket', groups: KnockoutBracket): void;
}>();

function doubleSeedPositions(seedPositions: number[]) {
  const length = seedPositions.length;
  const result = [];
  for (const sp of seedPositions) {
    result.push(sp);
    result.push(2 * length - 1 - sp);
  }
  return result;
}
const sortedPlayers = computed(() => {
  const players = props.selectedApplicationUsers.toSorted(
    (a, b) => (a.ranking ?? Number.MAX_VALUE) - (b.ranking ?? Number.MAX_VALUE)
  );
  return players.map((player, index) => {
    return { ...player, firstName: `${index + 1}. ${player.firstName}` };
  });
});
watch(
  [() => props.numberOfPlayersInKnockoutBracket, () => props.selectedApplicationUsers.length],
  () => {
    let seedPositions = [0, 1];
    while (seedPositions.length < props.numberOfPlayersInKnockoutBracket)
      seedPositions = doubleSeedPositions(seedPositions);
    props.knockoutBracket.matches = [];
    let stage = 0;
    let lastId = -1;
    for (let stageSize = props.numberOfPlayersInKnockoutBracket / 2; stageSize >= 1; stageSize /= 2) {
      for (let i = 0; i < stageSize; i++) {
        const playerIds: Match['playerIds'] = [null, null];
        if (stage == 0) {
          playerIds[0] = sortedPlayers.value[seedPositions[2 * i]]?.id ?? null;
          playerIds[1] = sortedPlayers.value[seedPositions[2 * i + 1]]?.id ?? null;
        }
        props.knockoutBracket.matches.push({
          id: --lastId,
          match: {
            id: -1,
            firstPlayerId: -1,
            secondPlayerId: -1,
            playerIds: playerIds,
            result: null,
            lastModifiedById: null,
            createdDateTime: '',
            updatedDateTime: ''
          },
          nextMatchInKnockoutBracketId: lastId / 2,
          stage
        });
      }
      stage++;
    }
    emit('update:knockoutBracket', props.knockoutBracket);
  },
  { immediate: true }
);

const players = computed(() => {
  const mp = new Map<number, ApplicationUserBasic>();
  for (const player of sortedPlayers.value) {
    mp.set(player.id, player);
  }
  return mp;
});
</script>

<template>
  <h2 class="text-2xl font-bold mt-4">Drabinka fazy pucharowej</h2>
  <KnockoutBracket :knockout-bracket="knockoutBracket" :sets-to-win="setsToWin" :players="players" :show-edit="false" />
</template>
