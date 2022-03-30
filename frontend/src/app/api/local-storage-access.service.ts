import {Injectable} from "@angular/core";

@Injectable({ providedIn: "root" })
export class LocalStorageAccessService {

  SEC_TOKEN: string = "ST";
  SEC_TOKEN_EXPIRED: string = "STE";
  GAME_ID: string = "GID";

  constructor() { }

  /**
   * @param securityToken an object with a token and an expired field
   */
  setSecurityToken(securityToken: any){
    localStorage.setItem(this.SEC_TOKEN, securityToken.token);
    localStorage.setItem(this.SEC_TOKEN_EXPIRED, securityToken.expiresAt);
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

  setGameId(id: string){
    localStorage.setItem(this.GAME_ID, id);
  }

  getGameId(): string{
    return localStorage.getItem(this.GAME_ID) as string;
  }
}
