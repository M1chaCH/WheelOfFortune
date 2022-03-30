import {Component} from "@angular/core";
import {FormBuilder} from "@angular/forms";
import {Router} from "@angular/router";
import {AppRout} from "../../config/appRout";
import {AuthService} from "../../auth/auth.service";
import {GameService} from "../../components/game/game.service";
import {GameStateType} from "../../components/game/GameEntities";

@Component({
  selector: "home",
  templateUrl: "home.component.html",
  styleUrls: ["home.component.scss"]
})
export class HomeComponent{
  isLoggedIn: boolean = false;
  username: string = "";

  constructor(
    private builder: FormBuilder,
    private router: Router,
    public auth: AuthService,
    private gameService: GameService,
  ) {

    auth.isLoggedIn().then(value => this.isLoggedIn = value).catch(() => this.isLoggedIn = false);
  }

  login(){
    this.router.navigate([AppRout.LOGIN]);
  }

  goToAdmin(){
    this.router.navigate([AppRout.ADMIN]);
  }

  showStart(): boolean{
    return this.gameService.game.gameState.state.valueOf() == -1;
  }

  showPlay(): boolean{
    return this.gameService.game.gameState.state === GameStateType.PLAY;
  }

  showEnd(): boolean{
    return this.gameService.game.gameState.state === GameStateType.END;
  }
}
