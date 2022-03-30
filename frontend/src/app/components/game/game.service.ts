import {Injectable} from "@angular/core";
import {GameState} from "../../config/gameConfig";

@Injectable({
  providedIn: "root"
})
export class GameService{
  gameState: GameState = -1;

  startGame(username: string){

  }
}
