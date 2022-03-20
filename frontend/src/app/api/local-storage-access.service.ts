import {Injectable} from "@angular/core";

@Injectable({ providedIn: "root" })
export class LocalStorageAccessService {

  SEC_TOKEN: string = "ST";
  SEC_TOKEN_EXPIRED: string = "STE";

  constructor() { }

  /**
   * @param securityToken an object with a token and an expired field
   */
  setSecurityToken(securityToken: any){
    localStorage.setItem(this.SEC_TOKEN, securityToken.token);
    localStorage.setItem(this.SEC_TOKEN_EXPIRED, securityToken.expired);
  }

  removeSecurityToken(){
    localStorage.removeItem(this.SEC_TOKEN);
    localStorage.removeItem(this.SEC_TOKEN_EXPIRED);
  }

  getSecurityToken() {
    return localStorage.getItem(this.SEC_TOKEN);
  }

  getSecurityExpired() {
    return localStorage.getItem(this.SEC_TOKEN_EXPIRED);
  }
}
