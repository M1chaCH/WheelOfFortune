/**
 * "[!]" should be replaced with some PathVariable
 */
export const enum ApiEndpoint{
  AUTH = "/auth",
  TOKEN = "/auth/token",
  CANDIDATE = "/game/candidate",
  CATEGORY = "/game/data/category",
  QUESTION = "/game/data/question",
  SENTENCE = "/game/data/sentence",
  HIGHSCORE = "/game/highscore",
  NEW_GAME = "/game",
  GAME = "/game/[!]",
  SPIN = "/game/[!]/spin",
  CONSONANT = "/game/[!]/consonant/[!]",
  QUIT = "/game/[!]/quit",
  RISK = "/game/[!]/risk",
  VOWEL = "/game/[!]/vowel/[!]",
  BANKRUPT = "/game/[!]/bankrupt",
  HP_DEATH = "/game/[!]/hpdeath",
  SOLVE_PUZZLE = "/game/[!]/solve/[!]",
  NEXT_SENTENCE = "/game/[!]/next",
}
