import type { Match, MatchResult, SetResult } from '~/types/tournament';

export function isSetResultCorrect(matchResult: MatchResult, setIndex: number, tieBreakerSetIndex: number) {
  const setResult = matchResult.setResults[setIndex];
  const isLastSet = matchResult.setResults.length - 1 === setIndex;
  const isTieBreaker = setIndex === tieBreakerSetIndex;
  if (setResult.firstPlayerScore < 0 || setResult.secondPlayerScore < 0) return false;

  const diff = Math.abs(setResult.firstPlayerScore - setResult.secondPlayerScore);
  const max = Math.max(setResult.firstPlayerScore, setResult.secondPlayerScore);
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
  const sets = result.setResults.length;
  if (sets === 0) return -1;
  let { firstPlayerScore, secondPlayerScore } = result;
  const { firstPlayerScore: last1PS, secondPlayerScore: last2PS } = result.setResults.at(-1)!;
  if (last1PS > last2PS) firstPlayerScore--;
  if (last1PS < last2PS) secondPlayerScore--;

  return firstPlayerScore === secondPlayerScore ? sets - 1 : -1;
}

function isWalkoverCorrect(result: MatchResult) {
  if (result.firstPlayerScore > 0 && result.secondPlayerScore > 0) return false;
  if (result.setResults.length <= 1) return false;

  const { firstPlayerScore, secondPlayerScore } = result.setResults[0];
  if (firstPlayerScore !== 6 && secondPlayerScore !== 6) return false;
  if (firstPlayerScore !== 0 && secondPlayerScore !== 0) return false;

  for (let i = 1; i < result.setResults.length; i++) {
    if (result.setResults[i - 1].firstPlayerScore !== result.setResults[i].firstPlayerScore) return false;
    if (result.setResults[i - 1].secondPlayerScore !== result.setResults[i].secondPlayerScore) return false;
  }

  return true;
}

export function isResultCorrect(match: Match, setsToWin: number) {
  if (!match.result) return false;
  const result = match.result;
  if (result.walkover) return isWalkoverCorrect(result);
  if (!between(MIN_SETS_IN_MATCH, result.setResults.length, MAX_SETS_IN_MATCH)) return false;

  const tieBreakerSetIndex = calculateTieBreakerSetIndex(result);
  if (!result.setResults.every((sr, index) => isSetResultCorrect(result, index, tieBreakerSetIndex))) return false;

  if (result.scratch) return true;

  const { firstPlayerScore, secondPlayerScore, winnerId } = result;
  const lastSet = result.setResults.at(-1)!;
  if (firstPlayerScore > secondPlayerScore && winnerId === match.firstPlayerId)
    return lastSet.firstPlayerScore > lastSet.secondPlayerScore && firstPlayerScore === setsToWin;
  if (firstPlayerScore < secondPlayerScore && winnerId === match.secondPlayerId)
    return lastSet.firstPlayerScore < lastSet.secondPlayerScore && secondPlayerScore === setsToWin;
  return false;
}
