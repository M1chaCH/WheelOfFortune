import {Component, OnInit, ViewChild} from "@angular/core";
import {ActivatedRoute, Router} from "@angular/router";
import {AppRout} from "../../config/appRout";
import {AuthService} from "../../auth/auth.service";
import {WheelOfFortuneApiService} from "../../api/wheel-of-fortune-api.service";
import {ApiEndpoint} from "../../config/apiEndpoint";
import {ApiHttpMethods} from "../../config/apiHttpMethods";
import {FormBuilder, FormGroup} from "@angular/forms";
import { HighScoreListComponent } from "src/app/components/high-score/high-score-list.component";
import { SentenceEditor } from "src/app/components/sentence-editor/sentence-editor";
import { QuestionEditorComponent } from "src/app/components/quesiton-editor/question-editor.component";

@Component({
  selector: "admin",
  templateUrl: "admin.component.html",
  styleUrls: ["admin.component.scss"]
})
export class AdminComponent implements OnInit{

  username: string = "";
  searchQuery: string | undefined;
  searchForm: FormGroup;

  @ViewChild(HighScoreListComponent) highScoreList: HighScoreListComponent | undefined;
  @ViewChild(SentenceEditor) sentenceEditor: SentenceEditor | undefined;
  @ViewChild(QuestionEditorComponent) questionEditor: QuestionEditorComponent | undefined;

  constructor(
    private router: Router,
    private auth: AuthService,
    private api: WheelOfFortuneApiService,
    private route: ActivatedRoute,
    private builder: FormBuilder,
  ) {
    this.searchForm = builder.group({
      search: "",
    });
  }

  ngOnInit(): void {
    this.api.callHandled(ApiEndpoint.AUTH, {}, ApiHttpMethods.GET, true)
      .subscribe((name: any) => this.username = name.response);

    this.route.queryParams.subscribe(params => {
      this.searchQuery = params["search"];
      this.searchForm.patchValue({
        search: params["search"],
      })
    })
  }

  search(){
    this.searchQuery = this.searchForm.value.search;
    this.router.navigate([AppRout.ADMIN], { queryParams: { search: this.searchQuery } })
      .then(() => this.reloadFilters());
  }

  revertSearch() {
    this.searchQuery = ""; 
    this.router.navigate([AppRout.ADMIN], {queryParams: {}})
      .then(() => this.reloadFilters());
  }

  private reloadFilters(){
    if(this.highScoreList)
      this.highScoreList.filterHighScores(this.searchQuery ? this.searchQuery : "");
    if(this.sentenceEditor)
      this.sentenceEditor.filterSentences(this.searchQuery ? this.searchQuery : "");
    if(this.questionEditor)
      this.questionEditor.filterQuestions(this.searchQuery ? this.searchQuery : "");
  }

  goHome(){
    this.router.navigate([AppRout.HOME]);
  }

  logout(){
    this.auth.logout();
  }
}
