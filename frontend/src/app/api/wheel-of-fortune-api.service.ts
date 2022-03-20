import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {ApiEndpoint} from "../config/apiEndpoint";
import {appConfig} from "../config/appConfig";
import {LocalStorageAccessService} from "./local-storage-access.service";
import {AppRout} from "../config/appRout";
import {Router} from "@angular/router";
import {ApiHttpMethods} from "../config/apiHttpMethods";
import {catchError, throwError} from "rxjs";
import {ErrorHandlingService} from "./error-handling.service";

@Injectable({providedIn: "root"})
export class WheelOfFortuneApiService {
  constructor(
    private http: HttpClient,
    private localStorageAccess: LocalStorageAccessService,
    private router: Router,
    private errorHandlingService: ErrorHandlingService,
  ) { }

  /**
   * sends get http request to endpoint with payload in the request body
   * @param endpoint the endpoint to send the request to
   * @param payload any object to be sent in the request body
   * @returns the response
   */
  get(endpoint: ApiEndpoint, payload: any) {
    return this.http.get<any>(`${appConfig.api_url}${endpoint}`, payload);
  }

  /**
   * sends post http request to endpoint with payload in the request body
   * @param endpoint the endpoint to send the request to
   * @param payload any object to be sent in the request body
   * @returns the response
   */
  post(endpoint: ApiEndpoint, payload: any) {
    return this.http.post<any>(`${appConfig.api_url}${endpoint}`, payload);
  }

  /**
   * sends put http request to endpoint with payload in the request body
   * @param endpoint the endpoint to send the request to
   * @param payload any object to be sent in the request body
   * @returns the response
   */
  put(endpoint: ApiEndpoint, payload: any) {
    return this.http.put<any>(`${appConfig.api_url}${endpoint}`, payload);
  }

  /**
   * sends delete http request to endpoint with payload in the request body
   * @param endpoint the endpoint to send the request to
   * @param payload any object to be sent in the request body
   * @returns the response
   */
  delete(endpoint: ApiEndpoint, payload: any) {
    return this.http.delete<any>(`${appConfig.api_url}${endpoint}`, { body: payload });
  }

  /**
   * sends a http request as the given method to the given endpoint with the payload in the request body
   * & handles the error if one occurs
   * @param endpoint the endpoint to send the request to
   * @param payload any object to be sent in the request body
   * @param method the HTTP method by witch to send the request
   * @param authorize true: refresh token if needed
   */
  callHandled(endpoint: ApiEndpoint, payload: any, method: ApiHttpMethods, authorize: boolean){
    if(authorize)
      this.refreshSecurityTokenIfExpired();

    let request: any;
    switch (method){
      case ApiHttpMethods.GET:
        request = this.get(endpoint, payload);
        break;
      case ApiHttpMethods.POST:
        request = this.post(endpoint, payload);
        break;
      case ApiHttpMethods.PUT:
        request = this.put(endpoint, payload);
        break;
      case ApiHttpMethods.DELETE:
        request = this.delete(endpoint, payload);
        break;
    }

    return this.handleError(request, payload);
  }

  private refreshSecurityTokenIfExpired() {
    console.log("checking if SecurityToken expired");
    const expired = this.localStorageAccess.getSecurityExpired();
    const token = this.localStorageAccess.getSecurityToken();
    if(!!expired && Date.parse(expired) < Date.now()){
      console.warn("token expired")
      //check if token valid -> false: login, true: update
      this.post(ApiEndpoint.TOKEN, token).subscribe(() => {
        console.log("token hasn't yet invalidated -> refreshing");
        this.put(ApiEndpoint.TOKEN, token).subscribe(secToken => {
          this.localStorageAccess.setSecurityToken(secToken);
        }, () => this.router.navigate([AppRout.LOGIN]),
          () => console.log("completed refreshing token"));

      }, () => this.router.navigate([AppRout.LOGIN]),
        () => console.log("completed token check"));
    }else {
      console.log("token NOT expired")
    }
  }

  private handleError(request: any, payload: any){
    return request.pipe(catchError(e => {
      console.log("caught error: " + JSON.stringify(e));
      this.errorHandlingService.handleError(e, payload);
      return throwError(e);
    }));
  }
}
