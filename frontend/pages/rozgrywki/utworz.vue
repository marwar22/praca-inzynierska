<script setup lang="ts">
import { ref } from 'vue';

const MIN_NUMBER_OF_PLAYERS = 1;
const MAX_NUMBER_OF_PLAYERS = 128;

const MIN_NUMBER_OF_GROUPS = 1;
const MAX_NUMBER_OF_GROUPS = 24;

const name = ref('');
const numberOfPlayers = ref(16);
const isNumberOfPlayersInRange = computed(
  () => MIN_NUMBER_OF_PLAYERS <= numberOfPlayers.value && numberOfPlayers.value <= MAX_NUMBER_OF_PLAYERS
);
const numberOfGroups = ref(4);
const isNumberOfGroupsInRange = computed(
  () => MIN_NUMBER_OF_GROUPS <= numberOfGroups.value && numberOfGroups.value <= MAX_NUMBER_OF_GROUPS
);

const playersInGroups = computed(() => {
  const res = [];
  let players = numberOfPlayers.value;
  let groups = numberOfGroups.value;
  for (; groups > 0; groups--) {
    let playersInGroup = Math.ceil(players / groups);
    players -= playersInGroup;
    res.push(playersInGroup);
  }
  return res;
});
function onNumberOfPlayersInput() {}
</script>
<template>
  <div class="container__margin flex flex-col">
    <h1 class="mt-5 text-3xl font-bold">Tworzenie nowej rozgrywki</h1>
    <div class="flex">
      <label class="flex flex-1 flex-col">
        Nazwa
        <ContestCreateInput
          placeholder="Nazwa rozgrywki"
          v-model.number="name"
          @input="onNumberOfPlayersInput"
          :error="''"
        >
        </ContestCreateInput>
      </label>

      <label class="mx-5 flex w-64 flex-col">
        Liczba zawodników
        <ContestCreateInput
          placeholder="Liczba grup"
          type="number"
          v-model.number="numberOfPlayers"
          @input="onNumberOfPlayersInput"
          :error="
            !isNumberOfPlayersInRange
              ? `Liczba graczy musi być w przedziale od ${MIN_NUMBER_OF_PLAYERS} do ${MAX_NUMBER_OF_PLAYERS}`
              : ''
          "
        >
        </ContestCreateInput>
      </label>

      <label class="flex w-64 flex-col">
        Liczba grup
        <ContestCreateInput
          placeholder="Liczba grup"
          type="number"
          v-model.number="numberOfGroups"
          @input="onNumberOfPlayersInput"
          :error="
            !isNumberOfGroupsInRange
              ? `Liczba grup musi być w przedziale od ${MIN_NUMBER_OF_GROUPS} do ${MAX_NUMBER_OF_GROUPS}`
              : ''
          "
        >
        </ContestCreateInput>
      </label>
    </div>
    <PlayerAdder :numberOfPlayers="numberOfPlayers" />
    <GroupsCreator :numberOfGroups="numberOfGroups" :playersInGroups="playersInGroups"></GroupsCreator>
  </div>
</template>

<style>
.container__margin {
  margin: 0 clamp(8px, calc((100% - 1100px) * 0.35), 16%);
}
</style>
