package ch.bbbaden.m151.wheeloffortune.auth.token;

import ch.bbbaden.m151.wheeloffortune.auth.user.AdminUser;
import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.InvalidatedSecurityTokenException;
import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.SecurityTokenNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class SecurityTokenServiceTest {

    @Autowired
    private SecurityTokenService service;

    @MockBean
    private SecurityTokenRepo repo;

    @Test
    void generateTokenForAdmin_Test() {
        AdminUser user = new AdminUser("admim", "admin", "saltysalt");
        LocalDateTime now = LocalDateTime.now();
        when(repo.save(any())).thenReturn(any());

        SecurityToken generatedToken = service.generateTokenForAdmin(user);
        assertEquals(user.getUsername(), generatedToken.getAdmin().getUsername());
        assertTrue(generatedToken.getExpiresAt().isAfter(now));
        assertTrue(generatedToken.getCreatedAt().isBefore(now.plusSeconds(1)));
    }

    @Test
    void getByToken_existingTest() {
        AdminUser user = new AdminUser("","","");
        String expectedTokenString = UUID.randomUUID().toString();
        SecurityToken expectedToken = new SecurityToken(expectedTokenString, LocalDateTime.now(), user);

        when(repo.findTokenByToken(expectedTokenString)).thenReturn(Optional.of(expectedToken));

        assertEquals(expectedToken, service.getByToken(expectedTokenString));
    }

    @Test
    void getByToken_unexcitingTest() {
        when(repo.findTokenByToken(anyString())).thenThrow(new SecurityTokenNotFoundException("unexcitingtoken"));
        assertThrows(SecurityTokenNotFoundException.class, () -> service.getByToken("unexcitingtoken"));
    }

    @Test
    void isTokenNewestOfAdmin_trueTest() {
        AdminUser admin = new AdminUser("","","");
        SecurityToken expectedToken = new SecurityToken("", LocalDateTime.now(), admin);
        List<SecurityToken> tokens = new ArrayList<>(Arrays.asList(
                new SecurityToken("", LocalDateTime.now().minusHours(3), admin),
                new SecurityToken("", LocalDateTime.now().minusHours(2), admin),
                new SecurityToken("", LocalDateTime.now().minusHours(1), admin),
                expectedToken));

        when(repo.findTokensByAdmin(admin)).thenReturn(tokens);

        assertTrue(service.isTokenNewestOfAdmin(expectedToken));
    }

    @Test
    void isTokenNewestOfAdmin_falseTest() {
        AdminUser admin = new AdminUser("","","");
        SecurityToken expectedToken = new SecurityToken("", LocalDateTime.now().minusHours(4), admin);
        List<SecurityToken> tokens = new ArrayList<>(Arrays.asList(
                new SecurityToken("", LocalDateTime.now().minusHours(3), admin),
                new SecurityToken("", LocalDateTime.now().minusHours(2), admin),
                new SecurityToken("", LocalDateTime.now().minusHours(1), admin),
                expectedToken));

        when(repo.findTokensByAdmin(admin)).thenReturn(tokens);

        assertThrows(InvalidatedSecurityTokenException.class, () -> service.isTokenNewestOfAdmin(expectedToken));
    }

    @Test
    void isTokenNotExpired_trueTest() {
        SecurityToken token = new SecurityToken("", LocalDateTime.now(), null);
        assertTrue(service.isTokenNotExpired(token));
    }

    @Test
    void isTokenNotExpired_falseTest() {
        SecurityToken token = new SecurityToken("", LocalDateTime.now().minusHours(5), null);
        assertFalse(service.isTokenNotExpired(token));
    }

    @Test
    void isTokenNotInvalidated_trueTest() {
        SecurityToken token = new SecurityToken("", LocalDateTime.now(), null);
        assertTrue(service.isTokenNotInvalidated(token));
    }

    @Test
    void isTokenNotInvalidated_falseTest() {
        SecurityToken token = new SecurityToken("", LocalDateTime.now().minusHours(10), null);
        assertFalse(service.isTokenNotInvalidated(token));
    }
}