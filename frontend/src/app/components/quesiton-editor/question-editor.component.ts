import {Component, Input, OnInit} from "@angular/core";
import {Categroy} from "../categroy-editor/categroy";
import {Question} from "./question";
import {WheelOfFortuneApiService} from "../../api/wheel-of-fortune-api.service";
import {MatBottomSheet} from "@angular/material/bottom-sheet";
import {ApiEndpoint} from "../../config/apiEndpoint";
import {ApiHttpMethods} from "../../config/apiHttpMethods";
import {QuestionEditorBottomSheetComponent} from "./bottom-sheet/question-editor-bottom-sheet.component";
import {GenericSearchService} from "../generic-search.service";

@Component({
  selector: "question-editor",
  templateUrl: "question-editor.component.html",
  styleUrls: ["question-editor.component.scss"],
})
export class QuestionEditorComponent implements OnInit{

  @Input() searchQuery: string | undefined;
  filteredQuestionsMap: Map<Categroy, Question[]> = new Map<Categroy, Question[]>();
  private questionsMap: Map<Categroy, Question[]> = new Map<Categroy, Question[]>();

  constructor(
    private api: WheelOfFortuneApiService,
    private bottomSheet: MatBottomSheet,
    private search: GenericSearchService,
  ) { }

  ngOnInit(): void {
    this.api.callHandled(ApiEndpoint.CATEGORY, {}, ApiHttpMethods.GET, false)
      .subscribe((categories: Categroy[]) => {
        let filteredCategories: Categroy[];
        if(this.searchQuery === undefined)
          filteredCategories = categories;
        else {
          filteredCategories = this.search.search(this.searchQuery, categories);
          if(filteredCategories.length === 0)
            filteredCategories = categories;
        }

        for (let category of filteredCategories) {
          this.api.callHandled(`${ApiEndpoint.QUESTION}/${category.id}`, {}, ApiHttpMethods.GET, false)
            .subscribe((questions: Question[]) => {
              this.questionsMap.set(category, questions)
              if(this.searchQuery === undefined) {
                this.filteredQuestionsMap.set(category, questions);
              } else {
                let filteredQuestions: Question[] = this.search.search(this.searchQuery, questions);
                this.filteredQuestionsMap.set(category, filteredQuestions);
              }
            });
        }
      });
  }

  create(){
    this.bottomSheet.open(QuestionEditorBottomSheetComponent, {
      data: {
        element: {},
        categories: Array.from(this.questionsMap.keys()),
        edit: false
      }
    });
  }

  edit(question: Question){
    this.bottomSheet.open(QuestionEditorBottomSheetComponent, {
      data: {
        element: question,
        categories: Array.from(this.questionsMap.keys()),
        edit: true
      }
    });
  }
}
