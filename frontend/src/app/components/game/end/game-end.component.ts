import {Component} from "@angular/core";
import {GameService, GameServiceListener} from "../game.service";
import {Game, GameStateTask} from "../GameEntities";

@Component({
  selector: "end-game",
  templateUrl: "game-end.component.html",
  styleUrls: ["game-end.component.scss"],
})
export class GameEndComponent implements GameServiceListener{
  results: string[] = [];

  constructor(
    private gameService: GameService,
  ) {
    this.update(this.gameService.attach("end-game", this));
  }

  update(game: Game): void {
    const budget = game.budget;
    const hp = game.hp;
    const roundsPlayed = game.roundCount;
    const message = this.gameService.getTaskParameterValue(GameStateTask.LEAVE);

    this.results[1] = "Winn 🤑: " + budget;
    this.results[2] = "Health Points 💕: " + hp;
    this.results[3] = "Played Rounds 🎡: " + roundsPlayed;
    this.results[4] = message;
  }

  leave(){
    this.gameService.deleteGame();
  }
}
