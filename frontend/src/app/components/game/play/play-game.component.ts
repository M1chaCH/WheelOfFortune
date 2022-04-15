import {Component, ViewChild} from "@angular/core";
import {GameService, GameServiceListener} from "../game.service";
import {Game, GameStateTask, GameStateType, WheelOfFortuneField} from "../GameEntities";
import {MatSnackBar} from "@angular/material/snack-bar";
import {MatBottomSheet} from "@angular/material/bottom-sheet";
import {RiskComponent} from "./risk/risk.component";
import {CharSelectorComponent} from "./char-selector/char-selector.component";
import {MatDialog} from "@angular/material/dialog";
import {BankruptDialogComponent} from "../dialogs/bankrupt-dialog.component";
import {HpDeathDialogComponent} from "../dialogs/hp-death-dialog.component";
import {SolvePuzzleComponent} from "./solve-puzzle/solve-puzzle.component";
import {SentenceCompleteDialogComponent} from "../dialogs/sentence-complete-dialog.component";
import {WinDialogComponent} from "../dialogs/win-dialog.component";
import {WheelOfFortuneComponent} from "./wheel-of-fortune/wheel-of-fortune.component";

@Component({
  selector: "play-game",
  templateUrl: "play-game.component.html",
  styleUrls: ["play-game.component.scss"],
})
export class PlayGameComponent implements GameServiceListener{
  gameFieldContent: string[] = [];
  currentTaskMessage: string = "";
  spinPressed: boolean = false;
  showWheelOfFortuneWheel: boolean = true;
  currentWheelOfFortuneField: WheelOfFortuneField | undefined;
  private openSnackBar: any;
  private openBottomSheet: any;
  private openDialog: any;
  private consonantsToGuess: string[] = [];
  private vowelsToGuess: string[] = [];

  @ViewChild(WheelOfFortuneComponent) wheelOfFortuneCmp: WheelOfFortuneComponent | undefined;

  constructor(
    private gameService: GameService,
    private snackBar: MatSnackBar,
    private bottomSheet: MatBottomSheet,
    private dialog: MatDialog,
  ) {
    this.update(gameService.attach("play-game", this));
  }

  update(game: Game): void {
    this.currentWheelOfFortuneField = game.currentWheelOfFortuneField;
    this.showWheelOfFortuneWheel = this.currentWheelOfFortuneField === undefined || this.spinPressed;
    if(this.spinPressed){
      this.wheelOfFortuneCmp?.animate().subscribe((done) => {
        if(done) {
          this.spinPressed = false;
          this.update(game);
        }
      });
      return;
    }

    this.gameFieldContent = game.gameField.revealedCharacters.split("");
    this.consonantsToGuess = game.consonantLeftToGuess;
    this.vowelsToGuess = game.vowelsLeftToGuess;

    this.currentTaskMessage = this.gameService.getTaskParameterValue(GameStateTask.MESSAGE);

    if(this.gameService.isInState(GameStateType.FORCED)){
      this.executeRiskIfIsTask(game);
      this.handleBankruptIfExists();
      this.handleHpDeathIfExists();
      this.handleSentenceCompleteIfExists();
      this.handleWinIfExists();
    }
  }

  handleWinIfExists(){
    if(this.gameService.isTaskAvailable(GameStateTask.WIN)) {
      this.openDialog = this.dialog.open(WinDialogComponent, {disableClose: true});

      this.openDialog.afterClosed().subscribe(() => this.gameService.acceptWin());
    }
  }

  handleSentenceCompleteIfExists(){
    if(this.gameService.isTaskAvailable(GameStateTask.SENTENCE_COMPLETED)){
      this.openDialog = this.dialog.open(SentenceCompleteDialogComponent, {disableClose: true});

      this.openDialog.afterClosed().subscribe((nextSentence: boolean) => {
        if(nextSentence) this.gameService.nextSentence();
        else this.gameService.quitGame();
      });
    }
  }

  handleHpDeathIfExists(){
    if(this.gameService.isTaskAvailable(GameStateTask.HP_DEATH)){
      this.openDialog = this.dialog.open(HpDeathDialogComponent, {disableClose: true});

      this.openDialog.afterClosed().subscribe(() => this.gameService.acceptHpDeath());
    }
  }

  handleBankruptIfExists(){
    if(this.gameService.isTaskAvailable(GameStateTask.BANKRUPT)){
      this.openDialog = this.dialog.open(BankruptDialogComponent, {disableClose: true});

      this.openDialog.afterClosed().subscribe(() => this.gameService.acceptBankruptcy());
    }
  }

  executeRiskIfIsTask(game: Game){
    if(this.gameService.isTaskAvailable(GameStateTask.RISK)){
      let possibleRiskAmounts: number[] = [100];
      if(game.budget > 200) //😁
        possibleRiskAmounts.push(200)
        if (game.budget > 300)
          possibleRiskAmounts.push(300)
          if(game.budget > 500)
            possibleRiskAmounts.push(500);
            if(game.budget > 1000)
              possibleRiskAmounts.push(1000);
              if(game.budget > 2000)
                possibleRiskAmounts.push(2000);
                if(game.budget > 5000)
                  possibleRiskAmounts.push(5000);


      this.openBottomSheet = this.bottomSheet.open(RiskComponent, {
        data: { question: this.gameService.getTaskParameterValue(GameStateTask.RISK), possibleRiskAmounts },
        disableClose: true,
      });

      this.openBottomSheet.afterDismissed().subscribe((response: any) =>
        this.gameService.solveQuestion(response.answerOneSelected, response.amount));
    }
  }

  guessConsonant(){
    this.openBottomSheet = this.bottomSheet.open(CharSelectorComponent, {
      data: {
        chars: this.consonantsToGuess,
        buyMode: false,
        type: "consonant",
      }
    });

    this.openBottomSheet.afterDismissed().subscribe((guess: string | undefined) => {
      if(guess !== undefined)
        this.gameService.guessConsonant(guess);
    });
  }

  buyVowel(){
    this.openBottomSheet = this.bottomSheet.open(CharSelectorComponent, {
      data: {
        chars: this.vowelsToGuess,
        buyMode: true,
        price: 200,
        type: "vowel"
      }
    });

    this.openBottomSheet.afterDismissed().subscribe((boughtVowel: string | undefined) => {
      if(boughtVowel !== undefined)
        this.gameService.buyVowel(boughtVowel)
    });
  }

  spin(){
    this.spinPressed = true;
    this.gameService.spin();
  }

  solvePuzzle(){
    this.openBottomSheet = this.bottomSheet.open(SolvePuzzleComponent);
    this.openBottomSheet.afterDismissed().subscribe((solution: string) => {
      if(!!solution)
        this.gameService.solvePuzzle(solution);
    });
  }

  quit(){
    this.openSnackBar = this.snackBar.open("🛑 Quit game: You will lose all progress.", "YES", { duration: 10 * 1000 });

    this.openSnackBar.onAction().subscribe(() => this.gameService.quitGame());
  }

  canGuessConsonant(): boolean{
    return this.gameService.isTaskAvailable(GameStateTask.GUESS_CONSONANT) && !this.spinPressed;
  }

  canBuyVowel(): boolean{
    return this.gameService.isTaskAvailable(GameStateTask.BUY_VOWEL) && !this.spinPressed;
  }

  canSolvePuzzle(): boolean{
    return this.gameService.isTaskAvailable(GameStateTask.SOLVE_PUZZLE) && !this.spinPressed;
  }

  canSpin(): boolean{
    return this.gameService.isTaskAvailable(GameStateTask.SPIN) && !this.spinPressed;
  }
}
