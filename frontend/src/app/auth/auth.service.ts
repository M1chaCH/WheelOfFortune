import {Injectable} from "@angular/core";
import {ApiEndpoint} from "../config/apiEndpoint";
import {LocalStorageAccessService} from "../api/local-storage-access.service";
import {Router} from "@angular/router";
import {AppRout} from "../config/appRout";
import {ApiHttpMethods} from "../config/apiHttpMethods";
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

  async isLoggedIn(): Promise<boolean> {
    let token = this.localStorageAccess.getSecurityToken();
    if(!token)
      return new Promise<boolean>((resolve, reject) => reject("security token not found"));

    const response = this.api.callHandled(ApiEndpoint.TOKEN, token, ApiHttpMethods.POST, true);
    return new Promise<boolean>((resolve, reject) => {
      response.subscribe(() => resolve(true),
        () => reject("invalid token"));
    });
  }
}
