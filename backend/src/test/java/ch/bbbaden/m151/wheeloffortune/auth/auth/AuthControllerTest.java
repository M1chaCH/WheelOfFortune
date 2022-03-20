package ch.bbbaden.m151.wheeloffortune.auth.auth;

import ch.bbbaden.m151.wheeloffortune.auth.api.AuthController;
import ch.bbbaden.m151.wheeloffortune.auth.api.AuthService;
import ch.bbbaden.m151.wheeloffortune.auth.api.LoginRequestDTO;
import ch.bbbaden.m151.wheeloffortune.auth.token.SecurityTokenDTO;
import ch.bbbaden.m151.wheeloffortune.config.CustomHTTPHeaders;
import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.auth.BadCredentialsException;
import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.auth.InvalidatedSecurityTokenException;
import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.auth.SecurityTokenNotFoundException;
import ch.bbbaden.m151.wheeloffortune.util.BasicResponseDTO;
import ch.bbbaden.m151.wheeloffortune.util.LocalDateTimeParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService service;

    private static ObjectWriter writer;

    @BeforeAll
    static void beforeAllTests(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        writer = mapper.writer().withDefaultPrettyPrinter();
    }

    @Test
    void loggedInUsername_loggedInTest() throws Exception {
        String token = UUID.randomUUID().toString();

        when(service.getUsernameByToken(token)).thenReturn(ResponseEntity.ok(new BasicResponseDTO("admin")));

        mockMvc.perform(get("/auth").header(CustomHTTPHeaders.AUTH, token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"response\": \"admin\"}"));
    }

    @Test
    void loggedInUsername_loggedOutTest() throws Exception {
        String token = "coolbutfaketoken";

        when(service.getUsernameByToken(anyString())).thenThrow(new SecurityTokenNotFoundException(anyString()));

        mockMvc.perform(get("/auth").header(CustomHTTPHeaders.AUTH, token))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void isTokenValid_trueTest() throws Exception {
        String token = UUID.randomUUID().toString();
        String expectedJson = writer.writeValueAsString(new BasicResponseDTO("Token is valid"));

        when(service.isTokenValid(token)).thenReturn(ResponseEntity.ok(new BasicResponseDTO("Token is valid")));
        mockMvc.perform(post("/auth/token").content(token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    void isTokenValid_falseTest() throws Exception {
        String token = "coolbutfaketoken";

        when(service.isTokenValid(token)).thenThrow(new InvalidatedSecurityTokenException(token));
        mockMvc.perform(post("/auth/token").content(token))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_successTest() throws Exception {
        String username = "admin";
        String password = "admin";
        String expectedToken = UUID.randomUUID().toString();
        String expectedExpiryDate = LocalDateTimeParser.dateToString(LocalDateTime.now());

        String loginRequestDTOJson = writer.writeValueAsString(new LoginRequestDTO(username, password));
        String secTokenResponseDTO =
                "{\"token\": \"" + expectedToken + "\", \"expiresAt\": \"" + expectedExpiryDate + "\"}";

        when(service.loginAdmin(username, password)).thenReturn(ResponseEntity.ok(
                new SecurityTokenDTO(expectedToken, expectedExpiryDate)));

        mockMvc.perform(post("/auth").contentType(APPLICATION_JSON_UTF8).content(loginRequestDTOJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(secTokenResponseDTO));
    }

    @Test
    void login_failedTest() throws Exception {
        String loginRequestDTOJson =
                writer.writeValueAsString(new LoginRequestDTO("fake-username", "fake-password"));

        when(service.loginAdmin(anyString(), anyString())).thenThrow(new BadCredentialsException("fake-username"));

        mockMvc.perform(post("/auth").contentType(APPLICATION_JSON_UTF8).content(loginRequestDTOJson))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test //token can be valid or expired (the API handles it the same way)
    void refreshToken_validTokenTest() throws Exception {
        String expectedToken = UUID.randomUUID().toString();
        String expectedExpiryDate = LocalDateTimeParser.dateToString(LocalDateTime.now());
        String secTokenResponseDTO =
                "{\"token\": \"" + expectedToken + "\", \"expiresAt\": \"" + expectedExpiryDate + "\"}";

        when(service.refreshToken(expectedToken)).thenReturn(ResponseEntity.ok(
                new SecurityTokenDTO(expectedToken, expectedExpiryDate)));

        mockMvc.perform(put("/auth/token").content(expectedToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(secTokenResponseDTO));
    }

    @Test
    void refreshToken_invalidatedTokenTest() throws Exception {
        when(service.refreshToken(anyString())).thenThrow(new InvalidatedSecurityTokenException(anyString()));

        mockMvc.perform(put("/auth/token").content("somefaketoken"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void logout_validTokenTest() throws Exception{
        String token = UUID.randomUUID().toString();
        String expectedJson = writer.writeValueAsString(new BasicResponseDTO("token deleted"));

        when(service.logout(token)).thenReturn(ResponseEntity.ok(new BasicResponseDTO("token deleted")));

        mockMvc.perform(delete("/auth").content(token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    void logout_invalidTokenTest() throws Exception {
        when(service.logout(anyString())).thenThrow(new SecurityTokenNotFoundException("somefaketoken"));
        mockMvc.perform(delete("/auth").content("somefaketoken"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}