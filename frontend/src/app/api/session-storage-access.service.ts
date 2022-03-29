import {Injectable} from "@angular/core";

@Injectable({ providedIn: "root" })
export class SessionStorageAccessService {

  SEC_TOKEN: string = "ST";
  SEC_TOKEN_EXPIRED: string = "STE";

  constructor() { }

  /**
   * @param securityToken an object with a token and an expired field
   */
  setSecurityToken(securityToken: any){
    sessionStorage.setItem(this.SEC_TOKEN, securityToken.token);
    sessionStorage.setItem(this.SEC_TOKEN_EXPIRED, securityToken.expiresAt);
  }

  removeSecurityToken(){
    sessionStorage.removeItem(this.SEC_TOKEN);
    sessionStorage.removeItem(this.SEC_TOKEN_EXPIRED);
  }

  getSecurityToken() {
    return sessionStorage.getItem(this.SEC_TOKEN);
  }

  getSecurityExpired() {
    return sessionStorage.getItem(this.SEC_TOKEN_EXPIRED);
  }
}
