import {Component, Input} from "@angular/core";
import {Categroy} from "./categroy";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatSnackBar} from "@angular/material/snack-bar";
import {WheelOfFortuneApiService} from "../../api/wheel-of-fortune-api.service";
import {ApiEndpoint} from "../../config/apiEndpoint";
import {ApiHttpMethods} from "../../config/apiHttpMethods";

@Component({
  selector: "category-editor",
  templateUrl: "category-editor.component.html",
  styleUrls: ["category-editor.component.scss"]
})
export class CategoryEditorComponent{

  @Input() category: Categroy = {id: -1, name: "unnamed"};
  editMode: boolean = false;
  editForm: FormGroup;
  private openSnackBar: any;

  constructor(
    private formBuilder: FormBuilder,
    private snackBar: MatSnackBar,
    private api: WheelOfFortuneApiService,
  ) {
    this.editForm = this.formBuilder.group({
      newName: ["", Validators.compose([
        Validators.minLength(2),
        Validators.maxLength(20),
      ])],
    })
  }

  save(){
    if(this.category.name === this.editForm.value.newName) {
      this.editMode = false
      return;
    }

    if(this.editForm.valid){
      this.category.name = this.editForm.value.newName;
      this.api.callHandled(ApiEndpoint.CATEGORY, this.category, ApiHttpMethods.PUT, true)
        .subscribe(() => {
          this.editMode = false;
          location.reload();
        });
    }
  }

  cancel(){
    this.openSnackBar?.dismiss();
    this.editMode = false;
  }

  delete(){
    this.openSnackBar = this.snackBar.open("Delete Category [ " + this.category.name +" ]? " +
      "DELETES ALL ASSOCIATED SENTENCES & QUESTIONS.", "YES", { duration: 10 * 1000 });

    this.openSnackBar.onAction().subscribe(() => {
      this.api.callHandled(`${ApiEndpoint.CATEGORY}/${this.category.id}`, {}, ApiHttpMethods.DELETE, true)
        .subscribe(() => {
          this.snackBar.open("Category deleted", "", {duration: 5 * 1000});
          this.editMode = false;
          location.reload();
        });
    });
  }
}
