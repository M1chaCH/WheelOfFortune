package ch.bbbaden.m151.wheeloffortune.auth.api;

import ch.bbbaden.m151.wheeloffortune.auth.token.SecurityTokenDTO;
import ch.bbbaden.m151.wheeloffortune.config.CustomHTTPHeaders;
import ch.bbbaden.m151.wheeloffortune.util.BasicResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping(path = "/auth")
@Tag(name = "Authentication")
public class AuthController {

    private final AuthService authService;

    /**
     * searches for the username that is coupled to the given token
     * @param token the token of the logged-in user
     * @return the username that belongs to the token
     */
    @Operation(summary = "Get username by token")
    @GetMapping()
    public ResponseEntity<BasicResponseDTO> loggedInUsername(@RequestHeader(CustomHTTPHeaders.AUTH) String token){
        return authService.getUsernameByToken(token);
    }

    /**
     * checks if the give token is valid
     * @param token the token to check
     * @return true: the token is valid
     */
    @Operation(summary = "check if token is valid")
    @PostMapping("/token")
    public ResponseEntity<BasicResponseDTO> isTokenValid(@RequestBody String token){
        return authService.isTokenValid(token);
    }

    /**
     * login with username & password
     * @param loginRequestDTO a DTO with the username and the password
     * @return the created access token
     */
    @Operation(summary = "login with username & password")
    @PostMapping()
    public ResponseEntity<SecurityTokenDTO> login(@RequestBody LoginRequestDTO loginRequestDTO){
        return authService.loginAdmin(loginRequestDTO.getUsername(), loginRequestDTO.getPassword());
    }

    /**
     * refresh the given token
     * @param token the token to refresh
     * @return the new token
     */
    @Operation(summary = "refresh the given token")
    @PutMapping("/token")
    public ResponseEntity<SecurityTokenDTO> refreshToken(@RequestBody String token){
        return authService.refreshToken(token);
    }

    /**
     * logout (deletes the token)
     * @param token the token that is currently in use
     * @return a message describing the result
     */
    @Operation(summary = "logout (deletes the token)")
    @DeleteMapping()
    public ResponseEntity<BasicResponseDTO> logout(@RequestBody String token){
        return authService.logout(token);
    }
}
