export const enum GameState{
  PLAY,
  END,
  FORCED,
}

export const enum GameStateTask{
  SPIN,
  GUESS_CONSONANT,
  BUY_VOWEL,
  SOLVE_PUZZLE,
  LEAVE,
  RISK,
  BANKRUPT,
  HP_DEATH,
  REPLAY
}
