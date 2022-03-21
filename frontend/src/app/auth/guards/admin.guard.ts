import {Injectable} from "@angular/core";
import {CanActivate, Router} from "@angular/router";
import {AuthService} from "../auth.service";
import {Observable, of, tap} from "rxjs";
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
    let request = this.auth.isLoggedIn();
    return new Observable<boolean>(observer => {
      request.then(value => observer.next(value)).catch(() => {
        observer.next(false);
        this.router.navigate([AppRout.LOGIN]);
      });
    });
  }
}
