package ch.bbbaden.m151.wheeloffortune.auth.token;

import ch.bbbaden.m151.wheeloffortune.auth.user.AdminUser;
import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.InvalidatedSecurityTokenException;
import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.SecurityTokenNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SecurityTokenService {
    /** 60 minutes after the createdAt time the token has to be refreshed **/
    public static final int TOKEN_EXPIRE_TIME_MIN = 60;
    /** 120 minutes after the expiredAt time the token can't be refreshed (invalidates) **/
    public static final int TOKEN_INVALIDATE_TIME_MIN = 120;

    private final SecurityTokenRepo repo;

    /**
     * saves a newly generated token
     * @param admin - the admin to generate the new token for
     */
    public SecurityToken generateTokenForAdmin(AdminUser admin){
        SecurityToken securityToken = new SecurityToken(
                UUID.randomUUID().toString(),
                LocalDateTime.now(),
                admin);

        repo.save(securityToken);
        return securityToken;
    }

    /**
     * @param token the token to search
     * @return the found token
     * @throws SecurityTokenNotFoundException when token does not exist
     */
    public SecurityToken getByToken(String token){
        return repo.findTokenByToken(token).orElseThrow(() -> new SecurityTokenNotFoundException(token));
    }

    /**
     * @param tokenString the token to check
     * @return true: token is not expired & is newest of associated admin
     * @throws SecurityTokenNotFoundException when token does not exist
     */
    public boolean isTokenValid(String tokenString){
        SecurityToken securityToken = getByToken(tokenString);
        return isTokenNotExpired(securityToken)
                && isTokenNewestOfAdmin(securityToken);
    }

    public Collection<SecurityToken> getByAdmin(AdminUser admin){
        return repo.findTokensByAdmin(admin);
    }

    public void deleteToken(SecurityToken securityTokenToDelete){
        repo.delete(securityTokenToDelete);
    }

    /**
     * check if the admin witch the token belongs to already refreshed the token
     * --> token can't be refreshed twice
     * --> client has to log in again
     * @param securityToken the token to check
     * @return true: the token ist the most recent of the user (never returns false)
     * @throws InvalidatedSecurityTokenException when token is not newest
     */
    public boolean isTokenNewestOfAdmin(SecurityToken securityToken){
        Collection<SecurityToken> tokensOfAdmin = getByAdmin(securityToken.getAdmin());
        tokensOfAdmin.remove(securityToken);
        for (SecurityToken securityTokenOfAdmin : tokensOfAdmin) {
            if(securityTokenOfAdmin.getCreatedAt().isAfter(securityToken.getCreatedAt()))
                throw new InvalidatedSecurityTokenException(securityToken.getToken());
        }
        return true;
    }

    /**
     * @param securityToken the token to check
     * @return true: the tokens expiredAt time is in the future
     */
    public boolean isTokenNotExpired(SecurityToken securityToken){
        return securityToken.getExpiresAt().isAfter(LocalDateTime.now());
    }

    /**
     * @param securityToken the token to check
     * @return true: the tokens expiredAt time is less than {@value TOKEN_INVALIDATE_TIME_MIN} minutes ago
     */
    public boolean isTokenNotInvalidated(SecurityToken securityToken){
        return securityToken.getExpiresAt().plusMinutes(SecurityTokenService.TOKEN_INVALIDATE_TIME_MIN).isAfter(LocalDateTime.now());
    }
}
