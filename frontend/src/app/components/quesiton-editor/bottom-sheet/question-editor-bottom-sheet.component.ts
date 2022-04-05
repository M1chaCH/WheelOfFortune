import {Component, Inject} from "@angular/core";
import {MAT_BOTTOM_SHEET_DATA, MatBottomSheetRef} from "@angular/material/bottom-sheet";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {WheelOfFortuneApiService} from "../../../api/wheel-of-fortune-api.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {Categroy} from "../../categroy-editor/categroy";
import {Question} from "../question";
import {ApiHttpMethods} from "../../../config/apiHttpMethods";
import {ApiEndpoint} from "../../../config/apiEndpoint";
import {DeletedSnackComponent} from "../../deleted-snack.component";

@Component({
  selector: "question-editor-bottom-sheet",
  templateUrl: "question-editor-bottom-sheet.component.html",
  styleUrls: ["question-editor-bottom-sheet.component.scss"],
})
export class QuestionEditorBottomSheetComponent{

  questionForm: FormGroup;
  editMode: boolean;
  categories: Categroy[];
  question: Question;
  selectedAnswerOneCorrect: boolean;
  selectedCategoryId: number;

  constructor(
    @Inject(MAT_BOTTOM_SHEET_DATA) private data: any,
    private _bottomSheetRef: MatBottomSheetRef<QuestionEditorBottomSheetComponent>,
    private formBuilder: FormBuilder,
    private api: WheelOfFortuneApiService,
    private snackBar: MatSnackBar,
  ) {
    this.editMode = data.edit;
    this.categories = data.categories;
    if(this.editMode)
      this.question = data.element;
    else {
      this.question = {
        id: 0,
        question: "",
        answerOne: "",
        answerTwo: "",
        answerOneCorrect: true,
        categoryDTO: {
          id: 0,
          name: ""
        }
      }
    }
    this.selectedAnswerOneCorrect = this.question.answerOneCorrect;
    this.selectedCategoryId = this.question.categoryDTO.id;

    this.questionForm = formBuilder.group({
      question: [this.question.question, Validators.compose([
        Validators.minLength(10),
        Validators.maxLength(60),
        Validators.pattern('^.*[?]'),
        Validators.required
      ])],
      answerOne: [this.question.answerOne, Validators.compose([
        Validators.minLength(2),
        Validators.maxLength(30),
        Validators.required
      ])],
      answerTwo: [this.question.answerTwo, Validators.compose([
        Validators.minLength(2),
        Validators.maxLength(30),
        Validators.required
      ])],
    });
    _bottomSheetRef.afterDismissed().subscribe(() => this.questionForm.reset())
  }

  save(){
   if(this.questionForm.valid && this.didFormChange()){
     const newQuestion: string = this.questionForm.value.question;
     const answerOne: string = this.questionForm.value.answerOne;
     const answerTwo: string = this.questionForm.value.answerTwo;
     const answerOneCorrect: boolean = this.selectedAnswerOneCorrect;
     const category: Categroy = this.findCategoryById(this.selectedCategoryId);

     this.question.question = newQuestion;
     this.question.answerOne = answerOne;
     this.question.answerTwo = answerTwo;
     this.question.answerOneCorrect = answerOneCorrect;
     this.question.categoryDTO = category;

     const method: ApiHttpMethods = this.editMode ? ApiHttpMethods.PUT : ApiHttpMethods.POST;
     this.api.callHandled(ApiEndpoint.QUESTION, this.question, method, true)
       .subscribe(() => {
         this._bottomSheetRef.dismiss();
         if(method === ApiHttpMethods.PUT)
           this.snackBar.open("✅ Changes saved ✅", "", { duration: 3 * 1000 })
         else if(method === ApiHttpMethods.POST)
           this.snackBar.open("✅ Question added ✅", "", { duration: 3 * 1000 })
         location.reload();
       });
    }
  }

  cancel(){
    this._bottomSheetRef.dismiss();
  }

  delete(){
    this.api.callHandled(`${ApiEndpoint.QUESTION}/${this.question.id}`,
      {}, ApiHttpMethods.DELETE, true)
      .subscribe(() => {
        this._bottomSheetRef.dismiss();
        this.snackBar.openFromComponent(DeletedSnackComponent, { duration: 3 * 1000, data: "Question" })
        location.reload();
      });
  }

  private didFormChange(): boolean{

    return (this.questionForm.value.question !== this.question.question
      || this.questionForm.value.answerOne !== this.question.answerOne
      || this.questionForm.value.answerTwo !== this.question.answerTwo
      || this.selectedAnswerOneCorrect !== this.question.answerOneCorrect
      || this.selectedCategoryId !== this.question.categoryDTO.id
    );
  }

  private findCategoryById(id: number): Categroy{
    for (let category of this.categories) {
      if (category.id === id)
        return category;
    }

    return {id: -1, name: "unnamed"};
  }
}
