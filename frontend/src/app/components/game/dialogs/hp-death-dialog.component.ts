import {Component} from "@angular/core";
import {MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: "hp-death-dialog",
  template: '<form>' +
    '    <h1>You ran out of HP 💘</h1>' +
    '    <button mat-raised-button color="warn" (click)="continue()">Continue</button>' +
    '</form>',
  styles: ['']
})
export class HpDeathDialogComponent{

  constructor(
    private _dialogRef: MatDialogRef<HpDeathDialogComponent>,
  ) { }

  continue(){
    this._dialogRef.close();
  }
}
