import type { ApplicationUser } from './applicationuser';

type SetResult = {
  firstPlayerScore: number;
  secondPlayerScore: number;
}

type MatchResult = {
  id: number;
  winnerId: number;
  firstPlayerScore: number;
  secondPlayerScore: number;
  setResults: SetResult[];  
}

export type Match = {
  id: number;
  result: MatchResult | null;
  firstPlayerId: number;
  secondPlayerId: number;
};

type TournamentGroup = {
  id: number;
  playerIds: number[];
  matches: Match[];
};

export type Tournament = {
  id: number;
  name: string;
  numberOfPlayers: number;
  numberOfGroups: number;
  players: ApplicationUser[];
  groups: TournamentGroup[];
  organizerId: number;
  startDate: string;
  endDate: string;
};
