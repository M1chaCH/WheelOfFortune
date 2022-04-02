import {Component, Inject} from "@angular/core";
import {MAT_BOTTOM_SHEET_DATA, MatBottomSheetRef} from "@angular/material/bottom-sheet";
import {Question} from "../../../quesiton-editor/question";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: "risk",
  templateUrl: "risk.component.html",
  styleUrls: ["risk.component.scss"],
})
export class RiskComponent{
  questionForm: FormGroup;
  question: string;
  answerOne: string;
  answerTwo: string;
  possibleRiskAmounts: number[] = [];

  constructor(
    @Inject(MAT_BOTTOM_SHEET_DATA) private data: any,
    private _bottomSheetRef: MatBottomSheetRef<RiskComponent>,
    private formBuilder: FormBuilder,
  ) {
    const q: Question = data.question;
    this.question = q.question;
    this.answerOne = q.answerOne;
    this.answerTwo = q.answerTwo;
    this.possibleRiskAmounts = data.possibleRiskAmounts;

    this.questionForm = formBuilder.group({
      selectedAnswer: ["", Validators.required],
      possibleAmount: ["", Validators.required]
    });
  }

  submit(){
    const selectedAnswerOneCorrect: boolean = this.questionForm.value.selectedAnswer;
    const selectedAmount: number = this.questionForm.value.possibleAmount;
    console.log(selectedAmount)
    this._bottomSheetRef.dismiss({ answerOneSelected: selectedAnswerOneCorrect, amount: selectedAmount });
  }
}
