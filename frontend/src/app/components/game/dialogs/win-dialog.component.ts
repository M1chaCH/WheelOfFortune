import {Component} from "@angular/core";
import {MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: "win-dialog",
  template: '<form>' +
    '    <h1>You played better & longer then the game could take. You WIN 😯</h1>' +
    '    <button mat-raised-button color="primary" (click)="continue()">Continue</button>' +
    '</form>',
  styles: ['']
})
export class WinDialogComponent{

  constructor(
    private _dialogRef: MatDialogRef<WinDialogComponent>,
  ) { }

  continue(){
    this._dialogRef.close();
  }
}
