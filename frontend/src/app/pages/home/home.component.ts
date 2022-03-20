import {Component} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {AppRout} from "../../config/appRout";

@Component({
  selector: "home",
  templateUrl: "home.component.html",
  styleUrls: ["home.component.scss"]
})
export class HomeComponent{
  sample: FormGroup;
  random: string = "";
  slider: number = 0;

  constructor(
    private builder: FormBuilder,
    private router: Router,
  ) {
    this.sample = builder.group({
      random: ["", Validators.required],
      slider: 50,
    });
  }

  sendForm(){
    this.random = this.sample.value.random;
    this.slider = this.sample.value.slider;
  }

  login(){
    this.router.navigate([AppRout.LOGIN]);
  }
}
