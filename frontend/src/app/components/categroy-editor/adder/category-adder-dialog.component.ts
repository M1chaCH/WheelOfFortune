import {Component} from "@angular/core";
import {MatDialogRef} from "@angular/material/dialog";
import {WheelOfFortuneApiService} from "../../../api/wheel-of-fortune-api.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ApiEndpoint} from "../../../config/apiEndpoint";
import {ApiHttpMethods} from "../../../config/apiHttpMethods";

@Component({
  selector: "category-adder-dialog",
  templateUrl: "category-adder-dialog.component.html",
})
export class CategoryAdderDialogComponent{

  categoryForm: FormGroup;

  constructor(
    private dialogRef: MatDialogRef<CategoryAdderDialogComponent>,
    private formBuilder: FormBuilder,
    private api: WheelOfFortuneApiService,
  ) {
    this.categoryForm = formBuilder.group({
      categoryName: ["", Validators.compose([
        Validators.minLength(2),
        Validators.maxLength(20),
        Validators.required,
      ])],
    })
    dialogRef.afterClosed().subscribe(() => this.categoryForm.reset());
  }

  create(){
    const categoryName = this.categoryForm.value.categoryName;

    if(!!categoryName && this.categoryForm.valid){
      this.api.callHandled(ApiEndpoint.CATEGORY, {id: 0, name: categoryName}, ApiHttpMethods.POST, true)
        .subscribe(() => {
          this.dialogRef.close();
          location.reload();
        });
    }
  }

  cancel(){
    this.dialogRef.close();
  }
}
