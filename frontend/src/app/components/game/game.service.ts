import {Injectable} from "@angular/core";
import {Categroy} from "../categroy-editor/categroy";
import {WheelOfFortuneApiService} from "../../api/wheel-of-fortune-api.service";
import {LocalStorageAccessService} from "../../api/local-storage-access.service";
import {ApiEndpoint} from "../../config/apiEndpoint";
import {Game, GameStateTask, GameStateType, StartGameRequest} from "./GameEntities";
import {ApiHttpMethods} from "../../config/apiHttpMethods";

@Injectable({
  providedIn: "root"
})
export class GameService{
  game: Game;

  constructor(
    private api: WheelOfFortuneApiService,
    private localStorage: LocalStorageAccessService,
  ) {
    this.game = this.createEmptyGame();
    this.loadGameIfExists();
  }

  startGame(username: string, category: Categroy){
    if(this.game.gameState.state.valueOf() === -1) {
      let bodyPayload: StartGameRequest = { username, category };
      this.api.callHandled(ApiEndpoint.NEW_GAME, bodyPayload, ApiHttpMethods.POST, false)
        .subscribe((receivedGame: Game) => {
          this.game = receivedGame;
          this.game.gameState.state = GameStateType.PLAY;
          console.log(this.game)
        });
    }else console.error("tried to start game when one is already started");
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
        state: -1,
        availableTasks: [],
        taskParameters: new Map<GameStateTask, any>(),
      },

      consonantLeftToGuess: [],
      vowelsLeftToGuess: [],
    };
  }

  private loadGameIfExists(){
    const savedGameId: string = this.localStorage.getGameId();
    if(!!savedGameId) {
      this.api.callHandled(ApiEndpoint.GAME, {}, ApiHttpMethods.GET, false, [savedGameId])
        .subscribe((game: Game) => {
            this.game = game;
          },
          () => {});
    }
  }
}
