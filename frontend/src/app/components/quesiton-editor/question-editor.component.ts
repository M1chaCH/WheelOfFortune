import {Component, OnInit} from "@angular/core";
import {Categroy} from "../categroy-editor/categroy";
import {Question} from "./question";
import {WheelOfFortuneApiService} from "../../api/wheel-of-fortune-api.service";
import {MatBottomSheet} from "@angular/material/bottom-sheet";
import {ApiEndpoint} from "../../config/apiEndpoint";
import {ApiHttpMethods} from "../../config/apiHttpMethods";
import {QuestionEditorBottomSheetComponent} from "./bottom-sheet/question-editor-bottom-sheet.component";

@Component({
  selector: "question-editor",
  templateUrl: "question-editor.component.html",
  styleUrls: ["question-editor.component.scss"],
})
export class QuestionEditorComponent implements OnInit{

  questions: Map<Categroy, Question[]> = new Map<Categroy, Question[]>();

  constructor(
    private api: WheelOfFortuneApiService,
    private bottomSheet: MatBottomSheet,
  ) { }

  ngOnInit(): void {
    this.api.callHandled(ApiEndpoint.CATEGORY, {}, ApiHttpMethods.GET, false)
      .subscribe((categories: Categroy[]) => {
        for (let category of categories) {
          this.api.callHandled(`${ApiEndpoint.QUESTION}/${category.id}`, {}, ApiHttpMethods.GET, false)
            .subscribe((questions: Question[]) => this.questions.set(category, questions));
        }
      });
  }

  create(){
    this.bottomSheet.open(QuestionEditorBottomSheetComponent, {
      data: {
        element: {},
        categories: Array.from(this.questions.keys()),
        edit: false
      }
    });
  }

  edit(question: Question){
    this.bottomSheet.open(QuestionEditorBottomSheetComponent, {
      data: {
        element: question,
        categories: Array.from(this.questions.keys()),
        edit: true
      }
    });
  }
}
