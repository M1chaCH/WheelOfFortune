import {Component, OnInit} from "@angular/core";
import {ActivatedRoute, Router} from "@angular/router";
import {AppRout} from "../../config/appRout";
import {AuthService} from "../../auth/auth.service";
import {WheelOfFortuneApiService} from "../../api/wheel-of-fortune-api.service";
import {ApiEndpoint} from "../../config/apiEndpoint";
import {ApiHttpMethods} from "../../config/apiHttpMethods";
import {FormBuilder, FormGroup} from "@angular/forms";

@Component({
  selector: "admin",
  templateUrl: "admin.component.html",
  styleUrls: ["admin.component.scss"]
})
export class AdminComponent implements OnInit{

  username: string = "";
  searchQuery: string | undefined;
  searchForm: FormGroup;

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
    let query = this.searchForm.value.search;
    this.router.navigate([AppRout.ADMIN], { queryParams: { search: query } })
      .then(() => location.reload());
  }

  revertSearch() {
    this.router.navigate([AppRout.ADMIN], {queryParams: {}})
      .then(() => location.reload());
  }

  goHome(){
    this.router.navigate([AppRout.HOME]);
  }

  logout(){
    this.auth.logout();
  }
}
