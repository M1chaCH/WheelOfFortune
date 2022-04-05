import {Component} from "@angular/core";
import {MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: "hp-death-dialog",
  template: '<div>' +
    '    <h1>Congratulations! You completed the Sentence. 👏</h1>' +
    '    <h2>Would you like to continue or leave?</h2>' +
    '    <form style="display: grid; grid-template-columns: 50% 50%; justify-items: center; align-items: center;">' +
    '        <button mat-raised-button style="width: 100px; height: 40px;" color="accent" (click)="submit(true)">Continue</button>' +
    '        <button mat-raised-button style="width: 100px; height: 40px;" color="warn" (click)="submit(false)">Leave</button>' +
    '    </form>' +
    '</div>',
  styles: ['']
})
export class SentenceCompleteDialogComponent{

  constructor(
    private _dialogRef: MatDialogRef<SentenceCompleteDialogComponent>,
  ) { }

  submit(nextSentence: boolean){
    this._dialogRef.close(nextSentence);
  }
}
