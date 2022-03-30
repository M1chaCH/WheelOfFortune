import {Component, Inject} from "@angular/core";
import {MAT_BOTTOM_SHEET_DATA, MatBottomSheetRef} from "@angular/material/bottom-sheet";
import {Sentence} from "../sentence";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Categroy} from "../../categroy-editor/categroy";
import {WheelOfFortuneApiService} from "../../../api/wheel-of-fortune-api.service";
import {ApiEndpoint} from "../../../config/apiEndpoint";
import {ApiHttpMethods} from "../../../config/apiHttpMethods";
import {MatSnackBar} from "@angular/material/snack-bar";
import {DeletedSnackComponent} from "../../deleted-snack.component";

@Component({
  selector: "sentence-editor-bottom-sheet",
  templateUrl: "sentence-editor-bottom-sheet.component.html",
  styleUrls: ["sentence-editor-bottom-sheet.component.scss"],
})
export class SentenceEditorBottomSheetComponent{

  sentenceForm: FormGroup;
  sentence: Sentence;
  categories: Categroy[];
  selectedCategoryId: number;
  editMode: boolean;

  constructor(
    @Inject(MAT_BOTTOM_SHEET_DATA) private data: any,
    private _bottomSheetRef: MatBottomSheetRef<SentenceEditorBottomSheetComponent>,
    private formBuilder: FormBuilder,
    private api: WheelOfFortuneApiService,
    private snackBar: MatSnackBar,
  ) {
    this.editMode = data.edit;
    if(this.editMode) this.sentence = data.element;
    else this.sentence = {id: 0, sentence: "", categoryDTO: { id: 0, name: "" }};

    this.categories = data.categories;
    this.selectedCategoryId = this.sentence.categoryDTO.id;

    this.sentenceForm = formBuilder.group({
      //TODO: implement char validated (regex)
      newSentence: [this.sentence.sentence, Validators.compose([
        Validators.minLength(10),
        Validators.maxLength(35),
      ])],
    })
  }

  save(){
    const newSentence: string = this.sentenceForm.value.newSentence;
    const newCategory: Categroy = this.findCategoryById(this.selectedCategoryId);

    if(this.sentenceForm.valid
      && (newSentence !== this.sentence.sentence || newCategory !== this.sentence.categoryDTO)
      && !!newCategory){
      this.sentence.sentence = newSentence;
      this.sentence.categoryDTO = newCategory;
      let method: ApiHttpMethods = this.editMode ? ApiHttpMethods.PUT : ApiHttpMethods.POST;
      this.api.callHandled(ApiEndpoint.SENTENCE, this.sentence, method, true)
        .subscribe(() => {
          this._bottomSheetRef.dismiss();
          if(method === ApiHttpMethods.PUT)
            this.snackBar.open("✅ Changes saved ✅", "", { duration: 3 * 1000 })
          else if(method === ApiHttpMethods.POST)
            this.snackBar.open("✅ Sentence added ✅", "", { duration: 3 * 1000 })
          location.reload();
      });
    }
  }

  cancel(){
    this.sentenceForm.reset();
    this._bottomSheetRef.dismiss();
  }

  delete(){
    this.api.callHandled(`${ApiEndpoint.SENTENCE}/${this.sentence.id}`,
      {}, ApiHttpMethods.DELETE, true)
      .subscribe(() => {
        this._bottomSheetRef.dismiss();
        this.snackBar.openFromComponent(DeletedSnackComponent, { duration: 3 * 1000, data: "Sentence" })
        location.reload();
      });
  }

  private findCategoryById(id: number): Categroy{
    for (let category of this.categories) {
      if (category.id === id)
        return category;
    }

    return {id: -1, name: "unnamed"};
  }
}
