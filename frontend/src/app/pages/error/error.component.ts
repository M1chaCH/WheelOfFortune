import {Component, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: "error-page",
  templateUrl: "error.component.html"
})
export class ErrorComponent implements OnInit{

  errorCode: number = -1;
  errorMessage: string = "";
  requestPayload: any;

  constructor(
    private route: ActivatedRoute,
  ) { }

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.errorCode = params["code"];
      this.errorMessage = params["message"];
      this.requestPayload = params["payload"];
    })
  }
}
