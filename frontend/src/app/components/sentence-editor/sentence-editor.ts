import {Component, OnInit} from "@angular/core";
import {Categroy} from "../categroy-editor/categroy";
import {Sentence} from "./sentence";
import {WheelOfFortuneApiService} from "../../api/wheel-of-fortune-api.service";
import {ApiEndpoint} from "../../config/apiEndpoint";
import {ApiHttpMethods} from "../../config/apiHttpMethods";
import {MatBottomSheet} from "@angular/material/bottom-sheet";
import {SentenceEditorBottomSheetComponent} from "./bottom-sheet/sentence-editor-bottom-sheet.component";

@Component({
  selector: "sentence-editor",
  templateUrl: "sentence-editor.html",
  styleUrls: ["sentence-editor.scss"],
})
export class SentenceEditor implements OnInit{

  sentences: Map<Categroy, Sentence[]> = new Map<Categroy, Sentence[]>();

  constructor(
    private api: WheelOfFortuneApiService,
    private bottomSheet: MatBottomSheet,
  ) { }

  ngOnInit() {
    this.api.callHandled(ApiEndpoint.CATEGORY, {}, ApiHttpMethods.GET, false)
      .subscribe((categories: Categroy[]) => {
        for (let category of categories) {
          this.api.callHandled(`${ApiEndpoint.SENTENCE}/${category.id}`, {}, ApiHttpMethods.GET, false)
            .subscribe((sentences: Sentence[]) => this.sentences.set(category, sentences));
        }
      });
  }

  create(){
    this.bottomSheet.open(SentenceEditorBottomSheetComponent, {
      data: {
        element: {},
        categories: Array.from(this.sentences.keys()),
        edit: false,
      }
    });
  }

  edit(sentence: Sentence){
    this.bottomSheet.open(SentenceEditorBottomSheetComponent, {
      data: {
        element: sentence,
        categories: Array.from(this.sentences.keys()),
        edit: true,
      }
    });
  }
}
