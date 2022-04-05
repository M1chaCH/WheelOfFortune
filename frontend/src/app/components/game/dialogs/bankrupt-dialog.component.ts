import {Component} from "@angular/core";
import {MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: "bankrupt-dialog",
  template: '<form>' +
    '    <h1>You spinned bankrupt 😑</h1>' +
    '    <button mat-raised-button color="warn" (click)="continue()">Continue</button>' +
    '</form>',
  styles: ['']
})
export class BankruptDialogComponent{

  constructor(
    private _dialogRef: MatDialogRef<BankruptDialogComponent>,
  ) { }

  continue(){
    this._dialogRef.close();
  }
}
