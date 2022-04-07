import {Component, Input, OnInit} from "@angular/core";
import {Highscore} from "./highscore";
import {WheelOfFortuneApiService} from "../../api/wheel-of-fortune-api.service";
import {ApiEndpoint} from "../../config/apiEndpoint";
import {ApiHttpMethods} from "../../config/apiHttpMethods";
import {MatSnackBar} from "@angular/material/snack-bar";
import {DeletedSnackComponent} from "../deleted-snack.component";
import {GameService, GameServiceListener} from "../game/game.service";
import {Game, GameStateType} from "../game/GameEntities";
import {GenericSearchService} from "../generic-search.service";

@Component({
  selector: "high-score-list",
  templateUrl: "high-score-list.component.html",
  styleUrls: ["high-score-list.component.scss"]
})
export class HighScoreListComponent implements OnInit, GameServiceListener{

  @Input() admin: boolean = false;
  @Input() searchQuery: string | undefined;
  filteredHighScores: Highscore[] = [];

  private highScores: Highscore[] = [];

  constructor(
    private api: WheelOfFortuneApiService,
    private snackBar: MatSnackBar,
    private gameService: GameService,
    private search: GenericSearchService,
  ) { }

  ngOnInit(): void {
    this.loadHighScores();
  }

  update(game: Game): void {
    if(this.gameService.isInState(GameStateType.END))
      this.loadHighScores();
  }

  delete(highScore: Highscore){
    this.api.callHandled(`${ApiEndpoint.HIGHSCORE}/${highScore.id}`, {}, ApiHttpMethods.DELETE, true)
      .subscribe(() => {
        this.loadHighScores();
        this.snackBar.openFromComponent(DeletedSnackComponent, {
          duration: 6 * 1000,
          data: "Highscore"
        });
      });
  }

  private loadHighScores(){
    this.api.callHandled(ApiEndpoint.HIGHSCORE, {}, ApiHttpMethods.GET, false)
      .subscribe((response: Highscore[]) => {
        this.highScores = response;
        if(this.searchQuery === undefined)
          this.filteredHighScores = this.highScores;
        else
          this.filteredHighScores = this.search.search(this.searchQuery, this.highScores);
      });
  }
}
