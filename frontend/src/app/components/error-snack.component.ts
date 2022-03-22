import {MAT_SNACK_BAR_DATA} from "@angular/material/snack-bar";
import {Component, Inject} from "@angular/core";

@Component({
  selector: "error-snack",
  template: "<div class='flex-center-content'>" +
    "    <span>🚫 {{data}} 😥</span>" +
    "</div>"
})
export class ErrorSnackComponent {

  constructor(
    @Inject(MAT_SNACK_BAR_DATA) public data: string
  ) { }
}
