import {MAT_SNACK_BAR_DATA} from "@angular/material/snack-bar";
import {Component, Inject} from "@angular/core";

/**
 * displays a normal message starting with "🗑️ Successfully deleted " and then the given data
 */
@Component({
  selector: "error-snack",
  template: "<div style='display: flex; align-items: center; justify-content: center'>" +
    "    <span>🗑️ Successfully deleted {{data}}</span>" +
    "</div>"
})
export class DeletedSnackComponent{

  constructor(
    @Inject(MAT_SNACK_BAR_DATA) public data: string
  ) { }
}
