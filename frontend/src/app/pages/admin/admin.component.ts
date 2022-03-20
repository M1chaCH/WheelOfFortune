import {Component} from "@angular/core";
import {Router} from "@angular/router";
import {AppRout} from "../../config/appRout";
import {AuthService} from "../../auth/auth.service";

@Component({
  selector: "admin",
  templateUrl: "admin.component.html",
  styleUrls: ["admin.component.scss"]
})
export class AdminComponent{

  constructor(
    private router: Router,
    private auth: AuthService,
  ) { }

  goHome(){
    this.router.navigate([AppRout.HOME]);
  }

  logout(){
    this.auth.logout();
  }
}
