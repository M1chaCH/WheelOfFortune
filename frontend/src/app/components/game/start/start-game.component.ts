import {Component} from "@angular/core";
import {GameService} from "../game.service";

@Component({
  selector: "start-game",
  templateUrl: "start-game.component.html",
  styleUrls: ["start-game.component.scss"]
})
export class StartGameComponent{

  constructor(
    gameService: GameService,
  ) {

  }
}
