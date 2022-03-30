import {Injectable} from "@angular/core";
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {LocalStorageAccessService} from "./local-storage-access.service";
import {Observable} from "rxjs";

/**
 * Intercepts every http request and attaches SecurityToken to header if available
 */
@Injectable()
export class AuthInterceptor implements HttpInterceptor{

  constructor(
    private localStorageAccess: LocalStorageAccessService,
  ) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const securityToken = this.localStorageAccess.getSecurityToken();
    if(!!securityToken) {
      req = req.clone({ setHeaders: { 'auth': securityToken } });
    }

    return next.handle(req);
  }
}
