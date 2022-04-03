import {Injectable} from "@angular/core";
import {Categroy} from "../categroy-editor/categroy";
import {WheelOfFortuneApiService} from "../../api/wheel-of-fortune-api.service";
import {LocalStorageAccessService} from "../../api/local-storage-access.service";
import {ApiEndpoint} from "../../config/apiEndpoint";
import {Game, GameStateTask, GameStateType, StartGameRequest} from "./GameEntities";
import {ApiHttpMethods} from "../../config/apiHttpMethods";
import {HttpEvent} from "@angular/common/http";

export interface GameServiceListener{
  update(game: Game): void;
}

@Injectable({
  providedIn: "root"
})
export class GameService{
  game: Game;
  private listeners: Map<string, GameServiceListener> = new Map<string, GameServiceListener>([]);

  constructor(
    private api: WheelOfFortuneApiService,
    private localStorage: LocalStorageAccessService,
  ) {
    this.game = this.createEmptyGame();
    this.loadGameIfExists();
  }

  attach(id: string, toAttach: GameServiceListener): Game{
    this.listeners.set(id, toAttach);
    return this.game;
  }

  startGame(username: string, category: Categroy): Promise<boolean>{
    if(this.game.gameState.state.valueOf() === GameStateType.NOT_STARTED) {

      let bodyPayload: StartGameRequest = { username, category };
      return new Promise<boolean>((resolve, reject) => {
        this.api.post(ApiEndpoint.NEW_GAME, bodyPayload)
          .subscribe((receivedGame: Game) => { this.setGame(receivedGame); resolve(true) },
            () => reject("username exists"));
      });
    }
    return new Promise<boolean>((resolve, reject) => reject("game already running"))
  }

  spin(){
    this.api.callHandled(ApiEndpoint.SPIN, {}, ApiHttpMethods.GET, false, [this.game.gameId])
      .subscribe((response: Game) => this.setGame(response));
  }

  guessConsonant(guess: string){
    this.api.callHandled(ApiEndpoint.CONSONANT, {}, ApiHttpMethods.POST, false,
      [this.game.gameId, guess]).subscribe((response: Game) => this.setGame(response));
  }

  buyVowel(vowel: string){
    this.api.callHandled(ApiEndpoint.VOWEL, {}, ApiHttpMethods.POST, false,
      [this.game.gameId, vowel]).subscribe((response: Game) => this.setGame(response));
  }

  solveQuestion(selectedAnswerOne: boolean, amount: number){
    this.api.callHandled(ApiEndpoint.RISK, { selectedAnswerOne, amount }, ApiHttpMethods.POST, false,
      [this.game.gameId]).subscribe((response: Game) => this.setGame(response));
  }

  acceptBankruptcy(){
    this.api.callHandled(ApiEndpoint.BANKRUPT, {}, ApiHttpMethods.POST, false, [this.game.gameId])
      .subscribe((response: Game) => this.setGame(response));
  }

  nextSentence(){
    this.api.callHandled(ApiEndpoint.NEXT_SENTENCE, {}, ApiHttpMethods.POST, false, [this.game.gameId])
      .subscribe((response: Game) => this.setGame(response));
  }

  solvePuzzle(solution: string){
    this.api.callHandled(ApiEndpoint.SOLVE_PUZZLE, {}, ApiHttpMethods.POST, false,
      [this.game.gameId, solution]).subscribe((response: Game) => this.setGame(response));
  }

  acceptHpDeath(){
    this.api.callHandled(ApiEndpoint.HP_DEATH, {}, ApiHttpMethods.POST, false, [this.game.gameId])
      .subscribe((response: Game) => this.setGame(response));
  }

  quitGame(){
    this.api.callHandled(ApiEndpoint.QUIT, {}, ApiHttpMethods.POST, false, [this.game.gameId])
      .subscribe((response: Game) => this.setGame(response));
  }

  restartGame(){
    this.api.callHandled(ApiEndpoint.QUIT, {}, ApiHttpMethods.PUT, false, [this.game.gameId])
      .subscribe((response: Game) => this.setGame(response));
  }

  deleteGame(){
    this.api.callHandled(ApiEndpoint.QUIT, {}, ApiHttpMethods.DELETE, false, [this.game.gameId])
      .subscribe(() => this.setGame(this.createEmptyGame()));
  }

  /**
   * Factory for Game interface
   */
  createEmptyGame(): Game{
    return {
      gameId: "",
      username: "",
      roundCount: -1,
      budget: -1,
      hp: -1,

      gameField: {
        sentenceLength: -1,
        revealedCharacters: "",
      },
      gameState: {
        state: GameStateType.NOT_STARTED,
        availableTasks: [],
        taskParameters: [],
      },

      consonantLeftToGuess: [],
      vowelsLeftToGuess: [],

      wheelOfFortune: [],
    };
  }

  isTaskAvailable(task: GameStateTask): boolean{
    let allowed: boolean = false;
    this.game.gameState.availableTasks.forEach(t => {
      if(t.valueOf() === task)
        allowed = true;
    });

    return allowed;
  }

  isInState(state: GameStateType): boolean{
    return this.game.gameState.state.valueOf() === state;
  }

  getTaskParameterValue(key: GameStateTask): any{
    if(this.game.gameState?.taskParameters?.length > 0){
      for (let taskParameter of this.game.gameState.taskParameters) {
        if(taskParameter.key.valueOf() === key)
          return taskParameter.value;
      }
    }
  }

  private loadGameIfExists(){
    const savedGameId: string = this.localStorage.getGameId();
    if(!!savedGameId) {
      this.api.get(ApiEndpoint.GAME, {}, [savedGameId])
        .subscribe((event: HttpEvent<Game>) => {
          this.setGame(JSON.parse(JSON.stringify(event))); //why is typescript like this 😖
        }, () => this.localStorage.removeGameId());
    }
  }

  private setGame(newGame: Game){
    this.game = newGame;
    this.localStorage.setGameId(this.game.gameId);
    this.listeners.forEach((v) => v.update(this.game));
  }
}
