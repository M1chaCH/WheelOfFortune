import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {ApiEndpoint} from "../config/apiEndpoint";
import {appConfig} from "../config/appConfig";
import {LocalStorageAccessService} from "./local-storage-access.service";
import {AppRout} from "../config/appRout";
import {Router} from "@angular/router";
import {ApiHttpMethods} from "../config/apiHttpMethods";
import {catchError, Observable, throwError} from "rxjs";
import {ErrorHandlingService} from "./error-handling.service";

@Injectable({providedIn: "root"})
export class WheelOfFortuneApiService {
  constructor(
    private http: HttpClient,
    private localStorageAccess: LocalStorageAccessService,
    private router: Router,
    private errorHandlingService: ErrorHandlingService,
  ) { }

  private static generateRequestUrl(endpoint: string, pathVariables: any[]): string{
    let url: string = appConfig.api_url + endpoint;

    let amountToReplace: any = (endpoint.match(/\[!]/g) || []).length;

    for (let i = 0; i < amountToReplace; i++)
      url = url.replace(/\[!]/, pathVariables[i]);
    console.log(url); //TODO: remove when releasing
    return url;
  }

  /**
   * sends get http request to endpoint with bodyPayload in the request body
   * @param endpoint the endpoint to send the request to
   * @param bodyPayload any object to be sent in the request body
   * @param pathVariables any[] that contains the pathVariables which will replace "[!]" in the endpoint
   * @returns the response
   */
  get(endpoint: string, bodyPayload: any, pathVariables: any[] = []) {
    return this.http.get<any>(WheelOfFortuneApiService.generateRequestUrl(endpoint, pathVariables), bodyPayload);
  }

  /**
   * sends post http request to endpoint with bodyPayload in the request body
   * @param endpoint the endpoint to send the request to
   * @param bodyPayload any object to be sent in the request body
   * @param pathVariables any[] that contains the pathVariables which will replace "[!]" in the endpoint
   * @returns the response
   */
  post(endpoint: string, bodyPayload: any, pathVariables: any[] = []) {
    return this.http.post<any>(WheelOfFortuneApiService.generateRequestUrl(endpoint, pathVariables), bodyPayload);
  }

  /**
   * sends put http request to endpoint with bodyPayload in the request body
   * @param endpoint the endpoint to send the request to
   * @param bodyPayload any object to be sent in the request body
   * @param pathVariables any[] that contains the pathVariables which will replace "[!]" in the endpoint
   * @returns the response
   */
  put(endpoint: string, bodyPayload: any, pathVariables: any[] = []) {
    return this.http.put<any>(WheelOfFortuneApiService.generateRequestUrl(endpoint, pathVariables), bodyPayload);
  }

  /**
   * sends delete http request to endpoint with bodyPayload in the request body
   * @param endpoint the endpoint to send the request to
   * @param bodyPayload any object to be sent in the request body
   * @param pathVariables any[] that contains the pathVariables which will replace "[!]" in the endpoint
   * @returns the response
   */
  delete(endpoint: string, bodyPayload: any, pathVariables: any[] = []) {
    return this.http.delete<any>(WheelOfFortuneApiService.generateRequestUrl(endpoint, pathVariables),
      { body: bodyPayload });
  }

  /**
   * sends a http request as the given method to the given endpoint with the bodyPayload in the request body
   * & handles the error if one occurs
   * @param endpoint the endpoint to send the request to
   * @param bodyPayload any object to be sent in the request body
   * @param method the HTTP method by which to send the request
   * @param checkToken true: refresh token if needed
   * @param pathVariables any[] that contains the pathVariables which will replace "[!]" in the endpoint
   */
  callHandled(endpoint: string, bodyPayload: any, method: ApiHttpMethods, checkToken: boolean, pathVariables: any[] = []){
    if(checkToken) {
      return new Observable<any>(observer => {
        this.refreshSecurityTokenIfExpired().subscribe(success => {
          if(success)
            this.sendAndHandleRequest(endpoint, bodyPayload, method, pathVariables).subscribe((response: any) => observer.next(response));
          else this.router.navigate([AppRout.LOGIN]);
        },() => this.router.navigate([AppRout.LOGIN]));
      })
    }else{
      return this.sendAndHandleRequest(endpoint, bodyPayload, method, pathVariables);
    }
  }

  private sendAndHandleRequest(endpoint: string, bodyPayload: any, method: ApiHttpMethods, pathVariables: any[] = []){
    let request: any;
    switch (method){
      case ApiHttpMethods.GET:
        request = this.get(endpoint, bodyPayload, pathVariables);
        break;
      case ApiHttpMethods.POST:
        request = this.post(endpoint, bodyPayload, pathVariables);
        break;
      case ApiHttpMethods.PUT:
        request = this.put(endpoint, bodyPayload, pathVariables);
        break;
      case ApiHttpMethods.DELETE:
        request = this.delete(endpoint, bodyPayload, pathVariables);
        break;
    }

    return this.handleError(request, bodyPayload, pathVariables);
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

  private handleError(request: any, bodyPayload: any, pathVariables: any){
    return request.pipe(catchError(e => {
      console.log("caught error");
      this.errorHandlingService.handleError(e, bodyPayload, pathVariables);
      return throwError(e);
    }));
  }
}
