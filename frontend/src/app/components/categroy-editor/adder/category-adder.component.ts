import {Component} from "@angular/core";
import {MatDialog} from "@angular/material/dialog";
import {CategoryAdderDialogComponent} from "./category-adder-dialog.component";

@Component({
  selector: "category-adder",
  template: '<button mat-raised-button color="basic" (click)="open()">New Category</button>'
})
export class CategoryAdderComponent {

  constructor(private dialog: MatDialog) { }

  open(){
    this.dialog.open(CategoryAdderDialogComponent);
  }
}
