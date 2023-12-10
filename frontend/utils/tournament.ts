import type { Match, MatchResult, SetResult } from '~/types/tournament';

export function isSetResultCorrect(matchResult: MatchResult, setIndex: number, tieBreakerSetIndex: number) {
  const setResult = matchResult.setResults[setIndex];
  const isLastSet = matchResult.setResults.length - 1 === setIndex;
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
  const sets = result.setResults.length;
  if (sets === 0) return -1;
  let firstPlayerScore = result.setsScored[0];
  let secondPlayerScore = result.setsScored[1];

  const lastSetResult = result.setResults.at(-1)!.gamesScored;
  if (lastSetResult[0] > lastSetResult[1]) firstPlayerScore--;
  if (lastSetResult[0] < lastSetResult[1]) secondPlayerScore--;

  return firstPlayerScore === secondPlayerScore ? sets - 1 : -1;
}

function isWalkoverCorrect(result: MatchResult) {
  if (result.setResults.length <= 1) return false;

  if (result.setResults.every((sr) => sr.gamesScored[0] === 6 && sr.gamesScored[1] === 0)) return true;
  if (result.setResults.every((sr) => sr.gamesScored[0] === 0 && sr.gamesScored[1] === 6)) return true;

  return false;
}

export function isResultCorrect(match: Match, setsToWin: number) {
  if (!match.result) return false;
  const result = match.result;
  if (result.walkover) return isWalkoverCorrect(result);
  if (!between(MIN_SETS_IN_MATCH, result.setResults.length, MAX_SETS_IN_MATCH)) return false;

  const tieBreakerSetIndex = calculateTieBreakerSetIndex(result);
  if (!result.setResults.every((sr, index) => isSetResultCorrect(result, index, tieBreakerSetIndex))) return false;
  
  if (result.scratch) return true;

  const { setsScored, winnerId } = result;
  const lastSet = result.setResults.at(-1)!;
  if (setsScored[0] > setsScored[1] && winnerId === match.firstPlayerId)
    return lastSet.gamesScored[0] > lastSet.gamesScored[1] && setsScored[0] === setsToWin;
  if (setsScored[0] < setsScored[1] && winnerId === match.secondPlayerId)
    return lastSet.gamesScored[0] < lastSet.gamesScored[1] && setsScored[1] === setsToWin;
  return false;
}
