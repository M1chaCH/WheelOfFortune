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
        for (let category of categories) {
          this.api.callHandled(`${ApiEndpoint.QUESTION}/${category.id}`, {}, ApiHttpMethods.GET, false)
            .subscribe((questions: Question[]) => {
              this.questionsMap.set(category, questions)
              if(this.questionsMap.size === categories.length)
                this.filterQuestions(this.searchQuery);
            });
        }
      });
  }

  filterQuestions(query: string | undefined){
    if(query === "" || query === undefined){
      this.filteredQuestionsMap = this.questionsMap;
      return;
    }

    this.filteredQuestionsMap = new Map<Categroy, Question[]>();

    for (let category of this.questionsMap.keys()) {
      let questions: Question[] | undefined = this.questionsMap.get(category);
      
      if(questions !== undefined) {
        let filteredQuestions: Question[] = this.search.search(query, questions);
        this.filteredQuestionsMap.set(category, filteredQuestions);
      }
    }
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
