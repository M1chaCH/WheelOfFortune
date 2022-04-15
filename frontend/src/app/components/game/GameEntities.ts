import {Categroy} from "../categroy-editor/categroy";

export const enum GameStateType{
  PLAY = "PLAY",
  END = "END",
  FORCED = "FORCED",
  NOT_STARTED = "NOT_STARTED"
}

export const enum GameStateTask{
  SPIN = "SPIN",
  GUESS_CONSONANT = "GUESS_CONSONANT",
  BUY_VOWEL = "BUY_VOWEL",
  SOLVE_PUZZLE = "SOLVE_PUZZLE",
  LEAVE = "LEAVE",
  RISK = "RISK",
  BANKRUPT = "BANKRUPT",
  HP_DEATH = "HP_DEATH",
  REPLAY = "REPLAY",
  DELETE = "DELETE",
  MESSAGE = "MESSAGE",
  SENTENCE_COMPLETED = "SENTENCE_COMPLETED",
  WIN = "WIN",
}

export interface GameField{
  sentenceLength: number,
  revealedCharacters: string,
}

export interface GameState{
  state: GameStateType,
  availableTasks: GameStateTask[],
  taskParameters: TaskParameter[],
}

export interface TaskParameter{
  key: GameStateTask,
  value: any,
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
  hp: number,

  gameField: GameField,
  gameState: GameState,
  currentWheelOfFortuneField: WheelOfFortuneField | undefined,

  consonantLeftToGuess: string[],
  vowelsLeftToGuess: string[],
}

export interface StartGameRequest{
  username: string,
  category: Categroy,
}
