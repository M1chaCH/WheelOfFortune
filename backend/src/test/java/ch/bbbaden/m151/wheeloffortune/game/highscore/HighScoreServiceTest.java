package ch.bbbaden.m151.wheeloffortune.game.highscore;

import ch.bbbaden.m151.wheeloffortune.auth.token.SecurityTokenService;
import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.auth.InvalidatedSecurityTokenException;
import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.auth.SecurityTokenNotFoundException;
import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.entity.EntityNotFoundException;
import ch.bbbaden.m151.wheeloffortune.game.candidate.CandidateService;
import ch.bbbaden.m151.wheeloffortune.util.LocalDateTimeParser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class HighScoreServiceTest {

    @Autowired
    HighScoreService service;

    @MockBean
    HighScoreRepo repoMock;
    @MockBean
    SecurityTokenService securityTokenServiceMock;

    @Test
    void addNew_successTest(){
        LocalDateTime achievedAt = LocalDateTime.now();
        HighScoreDTO toAdd = new HighScoreDTO(1, 1123, "test", "1");
        HighScore expected = new HighScore(1123, "test", achievedAt);
        expected.setId(45);

        when(repoMock.save(any())).thenReturn(expected);

        HighScoreDTO expectedDTO = new HighScoreDTO(expected.getId(),
                expected.getScore(),
                expected.getUsername(),
                LocalDateTimeParser.dateToString(expected.getAchievedAt()));
        assertEquals(expectedDTO, service.addNew(toAdd));
    }

    @Test
    void edit_successTest(){
        String token = "somevalidtoken";
        int id = 12;
        when(securityTokenServiceMock.isTokenValid(token)).thenReturn(true);
        when(repoMock.findById(id)).thenReturn(Optional.of(new HighScore()));

        service.edit(token, new HighScoreDTO(id, 0, "",
                LocalDateTimeParser.dateToString(LocalDateTime.now())));
        assertTrue(true);
    }

    @Test
    void edit_deletedEntityTest(){
        String token = "someinvalidtoken";
        int id = 12;
        when(securityTokenServiceMock.isTokenValid(token)).thenReturn(true);
        when(repoMock.findById(id)).thenReturn(Optional.empty());

        HighScoreDTO toEdit =
                new HighScoreDTO(id, 0, "", LocalDateTimeParser.dateToString(LocalDateTime.now()));
        assertThrows(EntityNotFoundException.class, () -> service.edit(token, toEdit));
    }

    @Test
    void edit_invalidTokenTest(){
        String token = "someinvalidtoken";
        int id = 12;
        when(securityTokenServiceMock.isTokenValid(token)).thenThrow(InvalidatedSecurityTokenException.class);
        when(repoMock.findById(id)).thenReturn(Optional.of(new HighScore()));

        HighScoreDTO toEdit =
                new HighScoreDTO(id, 0, "", LocalDateTimeParser.dateToString(LocalDateTime.now()));
        assertThrows(InvalidatedSecurityTokenException.class, () -> service.edit(token, toEdit));
    }

    @Test
    void edit_deletedTokenTest(){
        String token = "somedeletedtoken";
        int id = 12;
        when(securityTokenServiceMock.isTokenValid(token)).thenThrow(SecurityTokenNotFoundException.class);
        when(repoMock.findById(id)).thenReturn(Optional.of(new HighScore()));

        HighScoreDTO toEdit =
                new HighScoreDTO(id, 0, "", LocalDateTimeParser.dateToString(LocalDateTime.now()));
        assertThrows(SecurityTokenNotFoundException.class, () -> service.edit(token, toEdit));
    }

    @Test
    void delete_successTest(){
        String token = "somevalidtoken";
        int id = 12;
        when(securityTokenServiceMock.isTokenValid(token)).thenReturn(true);
        when(repoMock.findById(id)).thenReturn(Optional.of(new HighScore()));

        service.delete(token, id);
        assertTrue(true);
    }
}