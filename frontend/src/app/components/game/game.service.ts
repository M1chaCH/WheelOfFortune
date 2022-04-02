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
  private listeners: GameServiceListener[] = []

  constructor(
    private api: WheelOfFortuneApiService,
    private localStorage: LocalStorageAccessService,
  ) {
    this.game = this.createEmptyGame();
    this.loadGameIfExists();
  }

  attach(toAttach: GameServiceListener): Game{
    this.listeners.push(toAttach);
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
      score: -1,
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

  isTaskAvailable(task: string): boolean{
    let allowed: boolean = false;
    this.game.gameState.availableTasks.forEach(t => {
      if(t.valueOf() === task)
        allowed = true;
    });

    return allowed;
  }

  isInState(state: string): boolean{
    return this.game.gameState.state.valueOf() === state;
  }

  getTaskParameterValue(key: GameStateTask): any{
    for (let taskParameter of this.game.gameState.taskParameters) {
      if(taskParameter.key.valueOf() === key)
        return taskParameter.value;
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
    this.listeners.forEach(l => l.update(this.game));
  }
}
