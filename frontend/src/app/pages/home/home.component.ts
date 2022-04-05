import {Component} from "@angular/core";
import {FormBuilder} from "@angular/forms";
import {Router} from "@angular/router";
import {AppRout} from "../../config/appRout";
import {AuthService} from "../../auth/auth.service";
import {GameService, GameServiceListener} from "../../components/game/game.service";
import {Game, GameStateType} from "../../components/game/GameEntities";

@Component({
  selector: "home",
  templateUrl: "home.component.html",
  styleUrls: ["home.component.scss"]
})
export class HomeComponent implements GameServiceListener{
  isLoggedIn: boolean = false;
  username: string = "";
  displayedBudget: string = "";
  displayedHp: string = "";

  constructor(
    private builder: FormBuilder,
    private router: Router,
    public auth: AuthService,
    private gameService: GameService,
  ) {

    auth.isLoggedIn().then(value => this.isLoggedIn = value).catch(() => this.isLoggedIn = false);
    this.update(this.gameService.attach("home", this));
  }

  update(game: Game): void {
    this.username = game.username;
    const budget: number = game.budget;
    const hp: number = game.hp;
    if(hp !== -1 && budget !== -1 && this.gameService.isInState(GameStateType.PLAY)
      || this.gameService.isInState(GameStateType.FORCED)){
      this.displayedBudget = "🤑 Budget: " + game.budget;
      this.displayedHp = "💕 HP: " + game.hp;
    }else{
      this.displayedBudget = "";
      this.displayedHp = "";
    }
  }

  login(){
    this.router.navigate([AppRout.LOGIN]);
  }

  goToAdmin(){
    this.router.navigate([AppRout.ADMIN]);
  }

  showStart(): boolean{
    return this.gameService.isInState(GameStateType.NOT_STARTED);
  }

  showPlay(): boolean{
    return this.gameService.isInState(GameStateType.PLAY) || this.gameService.isInState(GameStateType.FORCED);
  }

  showEnd(): boolean{
    return this.gameService.isInState(GameStateType.END);
  }
}
