import {Injectable} from "@angular/core";
import {ApiEndpoint} from "../config/apiEndpoint";
import {LocalStorageAccessService} from "../api/local-storage-access.service";
import {Router} from "@angular/router";
import {AppRout} from "../config/appRout";
import {ApiHttpMethods} from "../config/apiHttpMethods";
import {Observable, of} from "rxjs";
import {WheelOfFortuneApiService} from "../api/wheel-of-fortune-api.service";

@Injectable({ providedIn: "root" })
export class AuthService{

  constructor(
    private api: WheelOfFortuneApiService,
    private localStorageAccess: LocalStorageAccessService,
    private router: Router,
  ) { }

  login(username: string, password: string) {
    this.api.callHandled(ApiEndpoint.AUTH, { username: username, password: password }, ApiHttpMethods.POST, false)
      .subscribe((token: any) => {
        this.localStorageAccess.setSecurityToken(token);
        this.router.navigate([AppRout.ADMIN])
      });
  }

  /**
   * deletes the token and navigates to the home rout
   */
  logout(){
    this.api.delete(ApiEndpoint.AUTH, this.localStorageAccess.getSecurityToken()).subscribe(_ => {
      this.localStorageAccess.removeSecurityToken();
      this.router.navigate([AppRout.HOME]);
    });
  }

  isLoggedIn(): Observable<boolean>{
    let token = this.localStorageAccess.getSecurityToken();
    if(!token)
      return of(false);

    const response = this.api.post(ApiEndpoint.TOKEN, token);
    return new Observable<boolean>(observer =>
      response.subscribe(() => observer.next(true),
        () => observer.next(false)));
  }
}
