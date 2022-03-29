import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {ApiEndpoint} from "../config/apiEndpoint";
import {appConfig} from "../config/appConfig";
import {SessionStorageAccessService} from "./session-storage-access.service";
import {AppRout} from "../config/appRout";
import {Router} from "@angular/router";
import {ApiHttpMethods} from "../config/apiHttpMethods";
import {catchError, Observable, throwError} from "rxjs";
import {ErrorHandlingService} from "./error-handling.service";

@Injectable({providedIn: "root"})
export class WheelOfFortuneApiService {
  constructor(
    private http: HttpClient,
    private localStorageAccess: SessionStorageAccessService,
    private router: Router,
    private errorHandlingService: ErrorHandlingService,
  ) { }

  /**
   * sends get http request to endpoint with payload in the request body
   * @param endpoint the endpoint to send the request to
   * @param payload any object to be sent in the request body
   * @returns the response
   */
  get(endpoint: string, payload: any) {
    return this.http.get<any>(`${appConfig.api_url}${endpoint}`, payload);
  }

  /**
   * sends post http request to endpoint with payload in the request body
   * @param endpoint the endpoint to send the request to
   * @param payload any object to be sent in the request body
   * @returns the response
   */
  post(endpoint: string, payload: any) {
    return this.http.post<any>(`${appConfig.api_url}${endpoint}`, payload);
  }

  /**
   * sends put http request to endpoint with payload in the request body
   * @param endpoint the endpoint to send the request to
   * @param payload any object to be sent in the request body
   * @returns the response
   */
  put(endpoint: string, payload: any) {
    return this.http.put<any>(`${appConfig.api_url}${endpoint}`, payload);
  }

  /**
   * sends delete http request to endpoint with payload in the request body
   * @param endpoint the endpoint to send the request to
   * @param payload any object to be sent in the request body
   * @returns the response
   */
  delete(endpoint: string, payload: any) {
    return this.http.delete<any>(`${appConfig.api_url}${endpoint}`, { body: payload });
  }

  /**
   * sends a http request as the given method to the given endpoint with the payload in the request body
   * & handles the error if one occurs
   * @param endpoint the endpoint to send the request to
   * @param payload any object to be sent in the request body
   * @param method the HTTP method by which to send the request
   * @param checkToken true: refresh token if needed
   */
  callHandled(endpoint: string, payload: any, method: ApiHttpMethods, checkToken: boolean){
    if(checkToken) {
      return new Observable<any>(observer => {
        this.refreshSecurityTokenIfExpired().subscribe(success => {
          if(success)
            this.sendAndHandleRequest(endpoint, payload, method).subscribe((response: any) => observer.next(response));
          else this.router.navigate([AppRout.LOGIN]);
        },() => this.router.navigate([AppRout.LOGIN]));
      })
    }else{
      return this.sendAndHandleRequest(endpoint, payload, method);
    }
  }

  private sendAndHandleRequest(endpoint: string, payload: any, method: ApiHttpMethods){
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

  private refreshSecurityTokenIfExpired(): Observable<boolean>{
    const expired = this.localStorageAccess.getSecurityExpired();
    const token = this.localStorageAccess.getSecurityToken();

    return new Observable<boolean>(observer => {
      if(!!expired && Date.parse(expired) < Date.now()){
        console.warn("token expired")

        this.put(ApiEndpoint.TOKEN, token).subscribe(secToken => {
          this.localStorageAccess.setSecurityToken(secToken);
          observer.next(true);
        }, () => observer.next(false))

      }else {
        observer.next(true);
      }
    });
  }

  private handleError(request: any, payload: any){
    return request.pipe(catchError(e => {
      console.log("caught error");
      this.errorHandlingService.handleError(e, payload);
      return throwError(e);
    }));
  }
}
