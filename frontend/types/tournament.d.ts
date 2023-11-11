import type { ApplicationUser } from './applicationuser';
type TournamentGroup = {
    playerIds: number[],
}
export type Tournament = {
  name: string;
  numberOfPlayers: number;
  numberOfGroups: number;
  players: ApplicationUser[];
  groups: TournamentGroup[];
  startDate: string;
  endDate: string;
};
