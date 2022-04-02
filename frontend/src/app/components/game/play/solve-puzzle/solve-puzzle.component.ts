import {Component} from "@angular/core";
import {MatBottomSheetRef} from "@angular/material/bottom-sheet";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: "solve-puzzle",
  templateUrl: "solve-puzzle.component.html",
  styleUrls: ["solve-puzzle.component.scss"]
})
export class SolvePuzzleComponent{
  solutionForm: FormGroup;

  constructor(
    private _bottomSheetRef: MatBottomSheetRef<SolvePuzzleComponent>,
    private formBuilder: FormBuilder,
    ) {
    this.solutionForm = formBuilder.group({
      solution: ["", Validators.required],
    })
  }

  submit(){
    const solution = this.solutionForm.value.solution;
    this._bottomSheetRef.dismiss(solution);
  }

}
