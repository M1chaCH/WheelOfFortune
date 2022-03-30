import {Categroy} from "../categroy-editor/categroy";

export const enum GameStateType{
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

export interface GameField{
  sentenceLength: number,
  revealedCharacters: string,
}

export interface GameState{
  state: GameStateType,
  availableTasks: GameStateTask[],
  taskParameters: Map<GameStateTask, any>,
}

export interface WheelOfFortuneField{
  id: number,
  task: GameStateTask,
  reward: number,
}

export interface Game{
  gameId: string,
  username: string,
  roundCount: number,
  budget: number,
  score: number,
  hp: number,

  gameField: GameField,
  gameState: GameState,

  consonantLeftToGuess: string[],
  vowelsLeftToGuess: string[],
}

export interface StartGameRequest{
  username: string,
  category: Categroy,
}
