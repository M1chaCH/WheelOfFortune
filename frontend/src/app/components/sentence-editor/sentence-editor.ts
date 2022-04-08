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
        for (let category of categories) {
          this.api.callHandled(`${ApiEndpoint.SENTENCE}/${category.id}`, {}, ApiHttpMethods.GET, false)
            .subscribe((sentences: Sentence[]) => {
              this.sentencesMap.set(category, sentences);
              if(this.sentencesMap.size === categories.length) //if the last iteration
                this.filterSentences(this.searchQuery);
            });
        }
      });
  }

  filterSentences(query: string | undefined){
    if(query === "" || query === undefined){
      this.filteredSentencesMap = this.sentencesMap;
      return;
    }

    this.filteredSentencesMap = new Map<Categroy, Sentence[]>();
    
    for (let category of this.sentencesMap.keys()) {
      let sentences: Sentence[] | undefined = this.sentencesMap.get(category);
      
      if(sentences !== undefined){
        let filteredSentences: Sentence[] = this.search.search(query, sentences);
        this.filteredSentencesMap.set(category, filteredSentences);
      }
    }
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
