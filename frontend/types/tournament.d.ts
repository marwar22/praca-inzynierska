import type { ApplicationUser } from './applicationuser';

export type SetResult = {
  gamesScored: [number, number];
};

type MatchResult = {
  id: number;
  winnerId: number;
  scratch: boolean;
  walkover: boolean;
  setsScored: [number, number];
  playedSetResults: SetResult[];
  setResults: SetResult[];
};

export type Match = {
  id: number;
  result: MatchResult | null;
  firstPlayerId: number | null;
  secondPlayerId: number | null;
  playerIds: [number | null, number | null];
  lastModifiedById: number | null;
  tournamentGroupId?: number;
  tournamentId?: number;
  createdDateTime: string;
  updatedDateTime: string;
};

type TournamentGroup = {
  id: number;
  playerIds: number[];
  matches: Match[];
};

export type KnockoutBracketMatch = {
  id: number;
  stage: number;
  match: Match;
  nextMatchInKnockoutBracketId: number;
};

type KnockoutBracket = {
  id: number;
  matches: KnockoutBracketMatch[];
};

export type TournamentScoring = {
  groupPointsForWin: number;
  groupPointsForLoss: number;
  groupPointsForWalkover: number;
  ratingForTournamentParticipation: number;
  ratingForMatchWin: number;
  ratingForMatchLoss: number;
  ratingForMatchWalkover: number;
  ratingForTournamentWin: number;
  ratingForKnockoutStageParticipation: number[];
}

export type Tournament = {
  id: number;
  name: string;
  numberOfPlayers: number;
  hasGroupStage: boolean;
  numberOfGroups: number;
  setsToWin: number;
  scoring: TournamentScoring;
  players: ApplicationUser[];
  groups: TournamentGroup[];
  knockoutBracket: KnockoutBracket | null;
  organizerId: number;
  startDate: string;
  endDate: string;
  createdDateTime: string;
  updatedDateTime: string;
};

export type TournamentBasic = {
  id: number;
  name: string;
  numberOfPlayers: number;
  numberOfGroups: number;
  organizerId: number;
  startDate: string;
  endDate: string;
  createdDateTime: string;
  updatedDateTime: string;
};
