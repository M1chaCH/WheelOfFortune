import {Component} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {AppRout} from "../../config/appRout";
import {AuthService} from "../../auth/auth.service";

@Component({
  selector: "home",
  templateUrl: "home.component.html",
  styleUrls: ["home.component.scss"]
})
export class HomeComponent{
  sample: FormGroup;
  isLoggedIn: boolean = false;
  random: string = "";
  slider: number = 0;

  constructor(
    private builder: FormBuilder,
    private router: Router,
    public auth: AuthService,
  ) {
    this.sample = builder.group({
      random: ["", Validators.required],
      slider: 50,
    });

    auth.isLoggedIn().then(value => this.isLoggedIn = value).catch(() => this.isLoggedIn = false);
  }

  sendForm(){
    this.random = this.sample.value.random;
    this.slider = this.sample.value.slider;
  }

  login(){
    this.router.navigate([AppRout.LOGIN]);
  }

  goToAdmin(){
    this.router.navigate([AppRout.ADMIN]);
  }
}
