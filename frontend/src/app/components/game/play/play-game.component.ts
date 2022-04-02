import {Component} from "@angular/core";
import {GameService, GameServiceListener} from "../game.service";
import {Game, GameStateTask, WheelOfFortuneField} from "../GameEntities";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: "play-game",
  templateUrl: "play-game.component.html",
  styleUrls: ["play-game.component.scss"]
})
export class PlayGameComponent implements GameServiceListener{
  gameFieldContent: string[] = [];
  currentWheelOfFortuneFieldId: number = -1;
  private openSnackBar: any;
  private wheelOfFortune: WheelOfFortuneField[] = [];

  constructor(
    private gameService: GameService,
    private snackBar: MatSnackBar,
  ) {
    this.update(gameService.attach(this));
  }

  update(game: Game): void {
    this.gameFieldContent = game.gameField.revealedCharacters.split("");
    this.wheelOfFortune = game.wheelOfFortune;
    const currentFieldId: number = this.gameService.getTaskParameterValue(GameStateTask.SPIN);
    if(currentFieldId >= -1)
      this.currentWheelOfFortuneFieldId = currentFieldId;
  }

  spin(){
    this.gameService.spin();
  }

  quit(){
    this.openSnackBar = this.snackBar.open("🛑 Quit game: You will lose all progress.", "YES", { duration: 10 * 1000 });

    this.openSnackBar.onAction().subscribe(() => this.gameService.quitGame());
  }

  canGuessConsonant(): boolean{
    return this.gameService.isTaskAvailable(GameStateTask.GUESS_CONSONANT);
  }

  canBuyVowel(): boolean{
    return this.gameService.isTaskAvailable(GameStateTask.BUY_VOWEL);
  }

  canSolvePuzzle(): boolean{
    return this.gameService.isTaskAvailable(GameStateTask.SOLVE_PUZZLE);
  }

  canSpin(): boolean{
    return this.gameService.isTaskAvailable(GameStateTask.SPIN);
  }

  currentWheelOfFortuneTask(): string{
    return this.wheelOfFortune[this.currentWheelOfFortuneFieldId]?.task?.valueOf();
  }

  currentWheelOfFortuneReward(): string{
    let reward: number = this.wheelOfFortune[this.currentWheelOfFortuneFieldId]?.reward;
    let rewardString: string = "";
    //don't ask why!! (only "reward >= -1" did not work neither did "reward >== -1 work") 🤷
    if(reward >= -1 && reward !== -1)
        rewardString += reward;


    return rewardString;
  }
}
