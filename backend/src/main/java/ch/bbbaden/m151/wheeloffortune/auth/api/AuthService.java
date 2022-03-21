package ch.bbbaden.m151.wheeloffortune.auth.api;

import ch.bbbaden.m151.wheeloffortune.auth.token.SecurityToken;
import ch.bbbaden.m151.wheeloffortune.auth.token.SecurityTokenDTO;
import ch.bbbaden.m151.wheeloffortune.auth.token.SecurityTokenService;
import ch.bbbaden.m151.wheeloffortune.auth.user.AdminService;
import ch.bbbaden.m151.wheeloffortune.auth.user.AdminUser;
import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.auth.AccountNotFoundException;
import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.auth.SecurityTokenNotFoundException;
import ch.bbbaden.m151.wheeloffortune.util.BasicResponseDTO;
import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.auth.BadCredentialsException;
import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.auth.InvalidatedSecurityTokenException;
import ch.bbbaden.m151.wheeloffortune.util.EncodingUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final SecurityTokenService securityTokenService;
    private final AdminService adminService;

    /**
     * @param tokenString the token
     * @return the username of the admin owning the token
     * @throws SecurityTokenNotFoundException
     * when the token was not found
     */
    public ResponseEntity<BasicResponseDTO> getUsernameByToken(String tokenString){
        SecurityToken token = securityTokenService.getByToken(tokenString);
        return ResponseEntity.ok(new BasicResponseDTO(token.getAdmin().getUsername()));
    }

    /**
     * @param username the username of the Admin
     * @param password the plain password of the Admin
     * @return {@link SecurityTokenDTO} with the new token
     * @throws BadCredentialsException when the password was incorrect
     * @throws AccountNotFoundException when the username was incorrect
     */
    public ResponseEntity<SecurityTokenDTO> loginAdmin(String username, String password){
        AdminUser admin = authenticateAdmin(username, password);
        if(admin == null)
            throw new BadCredentialsException(username);

        SecurityToken securityToken = securityTokenService.generateTokenForAdmin(admin);
        return ResponseEntity.ok(SecurityTokenDTO.fromToken(securityToken));
    }

    /**
     * @param tokenString the token
     * @return when valid: OK response with message
     * @throws InvalidatedSecurityTokenException when the token is invalid
     * @throws SecurityTokenNotFoundException when the token does not exist
     */
    public ResponseEntity<BasicResponseDTO> isTokenValid(String tokenString){
        if(!securityTokenService.isTokenValid(tokenString))
            throw new InvalidatedSecurityTokenException(tokenString);
        return ResponseEntity.ok(new BasicResponseDTO("Token is valid"));
    }

    /**
     * @param tokenString the valid token to refresh
     * @return a new {@link SecurityTokenDTO} that contains the new SecurityToken
     * @throws InvalidatedSecurityTokenException when the token is invalid
     * @throws SecurityTokenNotFoundException when the token does not exist
     */
    public ResponseEntity<SecurityTokenDTO> refreshToken(String tokenString){
        SecurityToken securityTokenToRefresh = securityTokenService.getByToken(tokenString);

        if(!securityTokenService.isTokenNotInvalidated(securityTokenToRefresh) || !securityTokenService.isTokenNewestOfAdmin(
                securityTokenToRefresh))
            throw new InvalidatedSecurityTokenException(tokenString);

        securityTokenService.deleteToken(securityTokenToRefresh);
        SecurityToken securityToken = securityTokenService.generateTokenForAdmin(securityTokenToRefresh.getAdmin());
        return ResponseEntity.ok(SecurityTokenDTO.fromToken(securityToken));
    }

    /**
     * @param tokenString the token to delete
     * @return when token found: OK with message
     * @throws SecurityTokenNotFoundException when token does not exist
     */
    public ResponseEntity<BasicResponseDTO> logout(String tokenString){
        securityTokenService.deleteToken(securityTokenService.getByToken(tokenString));
        return ResponseEntity.ok(new BasicResponseDTO("successfully deleted token"));
    }

    /**
     * checks if an Admin with this username & password exists
     * @param username the username of the admin
     * @param password the plain password of the admin
     * @return the admin that matches with these credentials
     * @throws AccountNotFoundException when the username does not exist
     */
    public AdminUser authenticateAdmin(String username, String password){
        AdminUser admin = adminService.getAdminByUsername(username);
        String encodedPassword = EncodingUtil.hashString(password, admin.getPasswordSalt());

        if(encodedPassword.equals(admin.getPassword()))
            return admin;
        return null;
    }
}
