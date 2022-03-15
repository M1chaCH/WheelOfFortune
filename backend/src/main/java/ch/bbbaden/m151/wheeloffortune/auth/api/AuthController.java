package ch.bbbaden.m151.wheeloffortune.auth.api;

import ch.bbbaden.m151.wheeloffortune.auth.token.SecurityTokenDTO;
import ch.bbbaden.m151.wheeloffortune.config.CustomHTTPHeaders;
import ch.bbbaden.m151.wheeloffortune.util.BasicResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping(path = "/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * searches for the username that is coupled to the given token
     * @param token the token of the logged-in user
     * @return the username that belongs to the token
     */
    @GetMapping()
    public ResponseEntity<BasicResponseDTO> loggedInUsername(@RequestHeader(CustomHTTPHeaders.AUTH) String token){
        return authService.getUsernameByToken(token);
    }

    /**
     * checks if the give token is valid
     * @param token the token to check
     * @return true: the token is valid
     */
    @PostMapping("/token")
    public ResponseEntity<BasicResponseDTO> isTokenValid(@RequestBody String token){
        return authService.isTokenValid(token);
    }

    /**
     * login with username & password
     * @param loginRequestDTO a DTO with the username and the password
     * @return the created access token
     */
    @PostMapping()
    public ResponseEntity<SecurityTokenDTO> login(@RequestBody LoginRequestDTO loginRequestDTO){
        return authService.loginAdmin(loginRequestDTO.getUsername(), loginRequestDTO.getPassword());
    }

    /**
     * refresh the given token
     * @param token the token to refresh
     * @return the new token
     */
    @PutMapping("/token")
    public ResponseEntity<SecurityTokenDTO> refreshToken(@RequestBody String token){
        return authService.refreshToken(token);
    }

    /**
     * logout (deletes the token)
     * @param token the token that is currently in use
     * @return a message describing the result
     */
    @DeleteMapping()
    public ResponseEntity<BasicResponseDTO> logout(@RequestBody String token){
        return authService.logout(token);
    }
}
