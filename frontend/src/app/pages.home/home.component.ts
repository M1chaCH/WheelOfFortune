import {Component} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

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
}
