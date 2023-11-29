import type { Match, SetResult } from '~/types/tournament';

export function isSetResultCorrect(setResult: SetResult, isTieBreaker: boolean) {
  if (setResult.firstPlayerScore < 0 || setResult.secondPlayerScore < 0) return false;
  const diff = Math.abs(setResult.firstPlayerScore - setResult.secondPlayerScore);
  const max = Math.max(setResult.firstPlayerScore, setResult.secondPlayerScore);

  if (max === 6 && diff >= 2) return true; // 6:4, 6:3, 6:2, 6:1, 6:0
  if (max === 7 && between(1, diff, 2)) return true; // 7:6, 7:5
  if (isTieBreaker && max == 10 && diff >= 2) return true; // 10:8, 10:7, 10:6, 10:5, 10:4, 10:3, 10:2, 10:1, 10:0
  if (isTieBreaker && max > 10 && diff == 2) return true; // { (n):(n-2) | n > 10 }
  return false;
}

export function calculateTieBreakerSetIndex(match: Match) {
  if (!match.result) return -1;
  const { firstPlayerScore, secondPlayerScore } = match.result;
  const min = Math.min(firstPlayerScore, secondPlayerScore);
  const max = Math.max(firstPlayerScore, secondPlayerScore);
  const sets = match.result.setResults.length;
  if (sets % 2 == 1 && max === Math.ceil(sets / 2) && min == Math.floor(sets / 2)) return sets - 1;
  return -1;
}
export function isResultCorrect(match: Match) {
  if (!match.result) return false;
  if (!between(MIN_SETS_IN_MATCH, match.result.setResults.length, MAX_SETS_IN_MATCH)) return false;
  const tieBreakerSetIndex = calculateTieBreakerSetIndex(match);
  if (!match?.result.setResults.every((sr, index) => isSetResultCorrect(sr, index === tieBreakerSetIndex)))
    return false;

  const { firstPlayerScore, secondPlayerScore, winnerId } = match.result;
  const lastSet = match.result.setResults.at(-1)!;
  if (firstPlayerScore > secondPlayerScore && winnerId === match.firstPlayerId)
    return lastSet.firstPlayerScore > lastSet.secondPlayerScore;
  if (firstPlayerScore < secondPlayerScore && winnerId === match.secondPlayerId)
    return lastSet.firstPlayerScore < lastSet.secondPlayerScore;

  return false;
}
