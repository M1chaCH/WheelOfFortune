import {Injectable} from "@angular/core";
import {Router} from "@angular/router";
import {AppRout} from "../config/appRout";
import {HttpErrorResponse} from "@angular/common/http";

@Injectable({
  providedIn: "root"
})
export class ErrorHandlingService {

  constructor(
    private router: Router,
  ) { }

  /**
   * if error status is 401 redirects to log in page else initiates the error page
   * @param error the error that was thrown
   * @param requestPayload the payload of the request that cause the error
   */
  handleError(error: HttpErrorResponse, requestPayload: any, pathVariables: any[] = []){
    if(error?.status === 401) {
      console.log("caught 401 -> redirect login")
      this.redirectLogin();
    }
    //else if ("other error code that has special handling")
    else {
      console.warn("caught unexpected -> redirecting to error-page");
      console.log(JSON.stringify(error));
      this.router.navigate([AppRout.ERROR], { queryParams: {
          code: error.status,
          message: error.error.message,
          payload: "Body: " + JSON.stringify(requestPayload) + "   PathVariables: " + pathVariables,
        } });
    }
  }

  private redirectLogin(){
    this.router.navigate([AppRout.LOGIN], { queryParams: {
      message: "Login failed, please try again."
      }
    });
  }
}
