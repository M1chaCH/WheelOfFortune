package ch.bbbaden.m151.wheeloffortune.auth.auth;

import ch.bbbaden.m151.wheeloffortune.auth.api.AuthService;
import ch.bbbaden.m151.wheeloffortune.auth.token.SecurityToken;
import ch.bbbaden.m151.wheeloffortune.auth.token.SecurityTokenDTO;
import ch.bbbaden.m151.wheeloffortune.auth.token.SecurityTokenService;
import ch.bbbaden.m151.wheeloffortune.auth.user.AdminService;
import ch.bbbaden.m151.wheeloffortune.auth.user.AdminUser;
import ch.bbbaden.m151.wheeloffortune.util.BasicResponseDTO;
import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.auth.AccountNotFoundException;
import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.auth.BadCredentialsException;
import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.auth.InvalidatedSecurityTokenException;
import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.auth.SecurityTokenNotFoundException;
import ch.bbbaden.m151.wheeloffortune.util.EncodingUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class AuthServiceTest {

    @MockBean
    private SecurityTokenService tokenService;

    @MockBean
    private AdminService adminService;

    @Autowired
    private AuthService service;

    @Test
    void getUsernameByToken_existingTest() {
        String expectedUsername = "username";
        ResponseEntity<BasicResponseDTO> expectedResponse = ResponseEntity.ok(new BasicResponseDTO(expectedUsername));
        AdminUser admin = new AdminUser(expectedUsername, "password", "salt");
        String tokenString = UUID.randomUUID().toString();
        SecurityToken token = new SecurityToken(tokenString, LocalDateTime.now(), admin);

        when(tokenService.getByToken(tokenString)).thenReturn(token);

        //testing the entire response because format is relevant for frontend
        assertEquals(expectedResponse.toString(), service.getUsernameByToken(tokenString).toString());
    }

    @Test
    void getUsernameByToken_deletedTest(){
        String someToken = "someToken";
        when(tokenService.getByToken(someToken)).thenThrow(SecurityTokenNotFoundException.class);
        assertThrows(SecurityTokenNotFoundException.class, () -> service.getUsernameByToken(someToken));
    }

    @Test
    void loginAdmin_successTest() {
        AdminUser admin = new AdminUser("test", EncodingUtil.hashString("test", "salt"), "salt");
        String expectedTokenString = UUID.randomUUID().toString();
        SecurityToken expectedToken = new SecurityToken(expectedTokenString, LocalDateTime.now(), admin);
        ResponseEntity<SecurityTokenDTO> expectedResponse = ResponseEntity.ok(SecurityTokenDTO.fromToken(expectedToken));

        when(tokenService.generateTokenForAdmin(admin)).thenReturn(expectedToken);
        when(adminService.getAdminByUsername(admin.getUsername())).thenReturn(admin);

        //testing the entire response because format is relevant for frontend
        assertEquals(expectedResponse.toString(), service.loginAdmin("test", "test").toString());
    }

    @Test
    void loginAdmin_wrongUsernameTest() {
        AdminUser admin = new AdminUser("test", EncodingUtil.hashString("test", "salt"), "salt");
        String expectedTokenString = UUID.randomUUID().toString();
        SecurityToken expectedToken = new SecurityToken(expectedTokenString, LocalDateTime.now(), admin);

        when(tokenService.generateTokenForAdmin(admin)).thenReturn(expectedToken);
        when(adminService.getAdminByUsername(admin.getUsername())).thenThrow(AccountNotFoundException.class);

        assertThrows(AccountNotFoundException.class, () -> service.loginAdmin("test", "test"));
    }

    @Test
    void loginAdmin_wrongPasswordTest() {
        AdminUser admin = new AdminUser("test", EncodingUtil.hashString("test", "salt"), "salt");
        String expectedTokenString = UUID.randomUUID().toString();
        SecurityToken expectedToken = new SecurityToken(expectedTokenString, LocalDateTime.now(), admin);

        when(tokenService.generateTokenForAdmin(admin)).thenReturn(expectedToken);
        when(adminService.getAdminByUsername(admin.getUsername())).thenReturn(admin);

        assertThrows(BadCredentialsException.class, () -> service.loginAdmin("test", "fail"));
    }

    @Test
    void isTokenValid_trueTest() {
        String tokenString = UUID.randomUUID().toString();

        when(tokenService.isTokenValid(tokenString)).thenReturn(true);

        //only testing the response code because the body is an irrelevant message
        ResponseEntity<?> response = service.isTokenValid(tokenString);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void isTokenValid_invalidTest() {
        String tokenString = UUID.randomUUID().toString();

        when(tokenService.isTokenValid(tokenString)).thenReturn(false);

        assertThrows(InvalidatedSecurityTokenException.class, () -> service.isTokenValid(tokenString));
    }

    @Test
    void isTokenValid_deletedTest() {
        String tokenString = UUID.randomUUID().toString();

        when(tokenService.isTokenValid(tokenString)).thenThrow(SecurityTokenNotFoundException.class);

        assertThrows(SecurityTokenNotFoundException.class, () -> service.isTokenValid(tokenString));
    }

    @Test
    void refreshToken_validTest() {
        String oldTokenString = UUID.randomUUID().toString();
        AdminUser admin = new AdminUser();
        SecurityToken oldToken = new SecurityToken(oldTokenString, LocalDateTime.now(), admin);
        SecurityToken expectedToken = new SecurityToken("", LocalDateTime.now(), admin);
        ResponseEntity<SecurityTokenDTO> expectedResponse = ResponseEntity.ok(SecurityTokenDTO.fromToken(expectedToken));

        when(tokenService.getByToken(oldTokenString)).thenReturn(oldToken);
        when(tokenService.isTokenNotInvalidated(oldToken)).thenReturn(true);
        when(tokenService.isTokenNewestOfAdmin(oldToken)).thenReturn(true);
        when(tokenService.generateTokenForAdmin(admin)).thenReturn(expectedToken);

        //testing the entire response because format is relevant for frontend
        assertEquals(expectedResponse.toString(), service.refreshToken(oldTokenString).toString());
    }

    @Test
    void refreshToken_invalidatedTest() {
        String oldTokenString = UUID.randomUUID().toString();
        AdminUser admin = new AdminUser();
        SecurityToken oldToken = new SecurityToken(oldTokenString, LocalDateTime.now(), admin);
        SecurityToken expectedToken = new SecurityToken("", LocalDateTime.now(), admin);

        when(tokenService.getByToken(oldTokenString)).thenReturn(oldToken);
        when(tokenService.isTokenNotInvalidated(oldToken)).thenReturn(false);
        when(tokenService.isTokenNewestOfAdmin(oldToken)).thenReturn(true);
        when(tokenService.generateTokenForAdmin(admin)).thenReturn(expectedToken);

        assertThrows(InvalidatedSecurityTokenException.class, () -> service.refreshToken(oldTokenString));
    }

    @Test
    void refreshToken_oldTest() {
        String oldTokenString = UUID.randomUUID().toString();
        AdminUser admin = new AdminUser();
        SecurityToken oldToken = new SecurityToken(oldTokenString, LocalDateTime.now(), admin);
        SecurityToken expectedToken = new SecurityToken("", LocalDateTime.now(), admin);

        when(tokenService.getByToken(oldTokenString)).thenReturn(oldToken);
        when(tokenService.isTokenNotInvalidated(oldToken)).thenReturn(true);
        when(tokenService.isTokenNewestOfAdmin(oldToken)).thenReturn(false);
        when(tokenService.generateTokenForAdmin(admin)).thenReturn(expectedToken);

        assertThrows(InvalidatedSecurityTokenException.class, () -> service.refreshToken(oldTokenString));
    }

    @Test
    void refreshToken_deletedTest() {
        String oldTokenString = UUID.randomUUID().toString();
        AdminUser admin = new AdminUser();
        SecurityToken oldToken = new SecurityToken(oldTokenString, LocalDateTime.now(), admin);
        SecurityToken expectedToken = new SecurityToken("", LocalDateTime.now(), admin);

        when(tokenService.getByToken(oldTokenString)).thenThrow(SecurityTokenNotFoundException.class);
        when(tokenService.isTokenNotInvalidated(oldToken)).thenReturn(true);
        when(tokenService.isTokenNewestOfAdmin(oldToken)).thenReturn(true);
        when(tokenService.generateTokenForAdmin(admin)).thenReturn(expectedToken);

        assertThrows(SecurityTokenNotFoundException.class, () -> service.refreshToken(oldTokenString));
    }

    @Test
    void logout_successTest() {
        String tokenString = UUID.randomUUID().toString();
        SecurityToken token = new SecurityToken(tokenString, LocalDateTime.now(), new AdminUser());

        when(tokenService.getByToken(tokenString)).thenReturn(token);

        //only testing the response code because the body is an irrelevant message
        ResponseEntity<?> response = service.logout(tokenString);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void logout_expiredTest() {
        String tokenString = UUID.randomUUID().toString();
        SecurityToken token = new SecurityToken(tokenString, LocalDateTime.now().minusHours(5), new AdminUser());

        when(tokenService.getByToken(tokenString)).thenReturn(token);
        //only testing the response code because the body is an irrelevant message
        ResponseEntity<?> response = service.logout(tokenString);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void logout_deletedTest() {
        when(tokenService.getByToken(anyString())).thenThrow(SecurityTokenNotFoundException.class);
        assertThrows(SecurityTokenNotFoundException.class, () -> service.logout("deletedtoken"));
    }

    @Test
    void authenticateAdmin_successTest() {
        String givenUsername = "user";
        String givenPassword = "password";
        String salt = "salt";
        String hashedPassword = EncodingUtil.hashString(givenPassword, salt);
        AdminUser admin = new AdminUser(givenUsername, hashedPassword, salt);

        when(adminService.getAdminByUsername(givenUsername)).thenReturn(admin);

        assertEquals(admin, service.authenticateAdmin(givenUsername, givenPassword));
    }

    @Test
    void authenticateAdmin_wrongUsernameTest() {
        String givenUsername = "user";
        String givenPassword = "password";

        when(adminService.getAdminByUsername(anyString())).thenThrow(AccountNotFoundException.class);

        assertThrows(AccountNotFoundException.class, () -> service.authenticateAdmin(givenUsername, givenPassword));
    }

    @Test
    void authenticateAdmin_wrongPasswordTest() {
        String givenUsername = "user";
        String givenPassword = "password";
        String actualHashedPassword = EncodingUtil.hashString("somepass","somesalt");
        AdminUser admin = new AdminUser(givenUsername, actualHashedPassword, "salt");

        when(adminService.getAdminByUsername(givenUsername)).thenReturn(admin);

        assertNull(service.authenticateAdmin(givenUsername, givenPassword));
    }
}