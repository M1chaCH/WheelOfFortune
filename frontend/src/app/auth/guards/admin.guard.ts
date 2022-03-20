import {Injectable} from "@angular/core";
import {CanActivate, Router} from "@angular/router";
import {AuthService} from "../auth.service";
import {Observable, tap} from "rxjs";
import {AppRout} from "../../config/appRout";

@Injectable({ providedIn: "root" })
export class AdminGuard implements CanActivate {

  constructor(
    private auth: AuthService,
    private router: Router,
  ) { }

  /**
   * @returns true: when the client is logged in and has a valid toke
   */
  canActivate(): Observable<boolean>{
    const authenticated = this.auth.isLoggedIn();
    return authenticated.pipe(
      tap(authenticated => {
        if(!authenticated)
          this.router.navigate([AppRout.LOGIN]);
      }),
    );
  }
}
