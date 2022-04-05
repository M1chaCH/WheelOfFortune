import {Component, Inject} from "@angular/core";
import {MAT_BOTTOM_SHEET_DATA, MatBottomSheetRef} from "@angular/material/bottom-sheet";

@Component({
  selector: "char-selector",
  templateUrl: "char-selector.component.html",
  styleUrls: ["char-selector.component.scss"]
})
export class CharSelectorComponent{
  buyMode: boolean;
  type: string;
  price: number = 0;
  chars: string[];

  constructor(
    @Inject(MAT_BOTTOM_SHEET_DATA) private data: any,
    private _bottomSheetRef: MatBottomSheetRef<CharSelectorComponent>,
  ) {
    this.buyMode = data.buyMode;
    this.type = data.type;
    this.chars = data.chars;
    if(this.buyMode)
      this.price = data.price;
  }

  select(char: string){
    this._bottomSheetRef.dismiss(char);
  }
}
