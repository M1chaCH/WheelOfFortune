import {Component, Input, OnInit} from "@angular/core";
import {Categroy} from "../categroy-editor/categroy";
import {Sentence} from "./sentence";
import {WheelOfFortuneApiService} from "../../api/wheel-of-fortune-api.service";
import {ApiEndpoint} from "../../config/apiEndpoint";
import {ApiHttpMethods} from "../../config/apiHttpMethods";
import {MatBottomSheet} from "@angular/material/bottom-sheet";
import {SentenceEditorBottomSheetComponent} from "./bottom-sheet/sentence-editor-bottom-sheet.component";
import {GenericSearchService} from "../generic-search.service";

@Component({
  selector: "sentence-editor",
  templateUrl: "sentence-editor.html",
  styleUrls: ["sentence-editor.scss"],
})
export class SentenceEditor implements OnInit{

  @Input() searchQuery: string | undefined;
  filteredSentencesMap: Map<Categroy, Sentence[]> = new Map<Categroy, Sentence[]>();
  private sentencesMap: Map<Categroy, Sentence[]> = new Map<Categroy, Sentence[]>();

  constructor(
    private api: WheelOfFortuneApiService,
    private bottomSheet: MatBottomSheet,
    private search: GenericSearchService,
  ) { }

  ngOnInit() {
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
          this.api.callHandled(`${ApiEndpoint.SENTENCE}/${category.id}`, {}, ApiHttpMethods.GET, false)
            .subscribe((sentences: Sentence[]) => {
              this.sentencesMap.set(category, sentences);
              if(this.searchQuery === undefined) {
                this.filteredSentencesMap.set(category, sentences);
              } else {
                let filteredSentences: Sentence[] = this.search.search(this.searchQuery, sentences);
                this.filteredSentencesMap.set(category, filteredSentences);
              }
            });
        }
      });
  }

  create(){
    this.bottomSheet.open(SentenceEditorBottomSheetComponent, {
      data: {
        element: {},
        categories: Array.from(this.sentencesMap.keys()),
        edit: false,
      }
    });
  }

  edit(sentence: Sentence){
    this.bottomSheet.open(SentenceEditorBottomSheetComponent, {
      data: {
        element: sentence,
        categories: Array.from(this.sentencesMap.keys()),
        edit: true,
      }
    });
  }
}
