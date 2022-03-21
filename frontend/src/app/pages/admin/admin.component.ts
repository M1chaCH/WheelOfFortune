import {Component, OnInit} from "@angular/core";
import {Router} from "@angular/router";
import {AppRout} from "../../config/appRout";
import {AuthService} from "../../auth/auth.service";
import {WheelOfFortuneApiService} from "../../api/wheel-of-fortune-api.service";
import {ApiEndpoint} from "../../config/apiEndpoint";
import {ApiHttpMethods} from "../../config/apiHttpMethods";

@Component({
  selector: "admin",
  templateUrl: "admin.component.html",
  styleUrls: ["admin.component.scss"]
})
export class AdminComponent implements OnInit{

  username: string = "";

  constructor(
    private router: Router,
    private auth: AuthService,
    private api: WheelOfFortuneApiService,
  ) { }

  ngOnInit(): void {
    this.api.callHandled(ApiEndpoint.AUTH, {}, ApiHttpMethods.GET, true)
      .subscribe((name: any) => this.username = name.response);
  }

  goHome(){
    this.router.navigate([AppRout.HOME]);
  }

  logout(){
    this.auth.logout();
  }
}
