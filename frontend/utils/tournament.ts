import type { Match, MatchResult, TournamentScoring } from '~/types/tournament';

export function stageName(stage: number, scoring: TournamentScoring) {
  const stages = scoring.ratingForKnockoutStageParticipation.length;
  if (stage == stages - 1) return 'finał';
  if (stage == stages - 2) return 'półfinał';
  if (stage == stages - 3) return 'ćwierćfinał';
  return `${stage + 1} etap`;
}

export function isSetResultCorrect(matchResult: MatchResult, setIndex: number, tieBreakerSetIndex: number) {
  const setResult = matchResult.playedSetResults[setIndex];
  const isLastSet = matchResult.playedSetResults.length - 1 === setIndex;
  const isTieBreaker = setIndex === tieBreakerSetIndex;
  if (setResult.gamesScored[0] < 0 || setResult.gamesScored[1] < 0) return false;

  const diff = Math.abs(setResult.gamesScored[0] - setResult.gamesScored[1]);
  const max = Math.max(setResult.gamesScored[0], setResult.gamesScored[1]);
  if (isLastSet && matchResult.scratch) {
    if (isTieBreaker) return max < 10 || diff < 2;
    if (max === 7) return false;
    return max < 6 || diff < 2;
  } else {
    if (max === 6 && diff >= 2) return true; // 6:4, 6:3, 6:2, 6:1, 6:0
    if (max === 7 && between(1, diff, 2)) return true; // 7:6, 7:5
    if (isTieBreaker && max == 10 && diff >= 2) return true; // 10:8, 10:7, 10:6, 10:5, 10:4, 10:3, 10:2, 10:1, 10:0
    if (isTieBreaker && max > 10 && diff == 2) return true; // { (n):(n-2) | n > 10 }
    return false;
  }
}

export function calculateTieBreakerSetIndex(result: MatchResult | null) {
  if (result == null) return -1;
  const sets = result.playedSetResults.length;
  if (sets === 0) return -1;
  let firstPlayerScore = result.setsScored[0];
  let secondPlayerScore = result.setsScored[1];

  const lastSetResult = result.playedSetResults.at(-1)!.gamesScored;
  if (lastSetResult[0] > lastSetResult[1]) firstPlayerScore--;
  if (lastSetResult[0] < lastSetResult[1]) secondPlayerScore--;

  return firstPlayerScore === secondPlayerScore ? sets - 1 : -1;
}

function isWalkoverCorrect(result: MatchResult) {
  return result.playedSetResults.length == 0;
  // if (result.playedSetResults.length <= 1) return false;

  // if (result.playedSetResults.every((sr) => sr.gamesScored[0] === 6 && sr.gamesScored[1] === 0)) return true;
  // if (result.playedSetResults.every((sr) => sr.gamesScored[0] === 0 && sr.gamesScored[1] === 6)) return true;

  // return false;
}

export function isResultCorrect(match: Match, setsToWin: number) {
  if (!match.result) return false;
  const result = match.result;
  if (result.walkover) return isWalkoverCorrect(result);
  if (!between(result.scratch ? 0 : setsToWin, result.playedSetResults.length, 2 * setsToWin - 1)) return false;

  const tieBreakerSetIndex = calculateTieBreakerSetIndex(result);
  if (!result.playedSetResults.every((sr, index) => isSetResultCorrect(result, index, tieBreakerSetIndex)))
    return false;

  if (result.scratch) return true;

  const { setsScored, winnerId } = result;
  const lastSet = result.playedSetResults.at(-1)!;
  if (setsScored[0] > setsScored[1] && winnerId === match.firstPlayerId)
    return lastSet.gamesScored[0] > lastSet.gamesScored[1] && setsScored[0] === setsToWin;
  if (setsScored[0] < setsScored[1] && winnerId === match.secondPlayerId)
    return lastSet.gamesScored[0] < lastSet.gamesScored[1] && setsScored[1] === setsToWin;
  return false;
}
