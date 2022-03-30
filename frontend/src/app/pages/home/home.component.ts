import {Component} from "@angular/core";
import {FormBuilder} from "@angular/forms";
import {Router} from "@angular/router";
import {AppRout} from "../../config/appRout";
import {AuthService} from "../../auth/auth.service";

@Component({
  selector: "home",
  templateUrl: "home.component.html",
  styleUrls: ["home.component.scss"]
})
export class HomeComponent{
  isLoggedIn: boolean = false;

  constructor(
    private builder: FormBuilder,
    private router: Router,
    public auth: AuthService,
  ) {

    auth.isLoggedIn().then(value => this.isLoggedIn = value).catch(() => this.isLoggedIn = false);
  }

  login(){
    this.router.navigate([AppRout.LOGIN]);
  }

  goToAdmin(){
    this.router.navigate([AppRout.ADMIN]);
  }
}
