package ch.bbbaden.m151.wheeloffortune.game.highscore;

import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.entity.EntityAlreadyExistsException;
import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.entity.EntityNotFoundException;
import ch.bbbaden.m151.wheeloffortune.game.candidate.Candidate;
import ch.bbbaden.m151.wheeloffortune.game.candidate.CandidateDTO;
import ch.bbbaden.m151.wheeloffortune.game.candidate.CandidateService;
import ch.bbbaden.m151.wheeloffortune.game.data.GenericAuthenticatedEntityService;
import ch.bbbaden.m151.wheeloffortune.game.data.GenericAuthenticatedEntityServiceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class HighScoreServiceTest extends GenericAuthenticatedEntityServiceTest<Integer, HighScoreDTO, HighScore, HighScoreRepo> {

    @Autowired
    HighScoreService service;

    @MockBean
    HighScoreRepo repoMock;
    @MockBean
    CandidateService candidateServiceMock;

    @Test
    void getByCandidateSortedByDate_successTest(){
        List<HighScore> highScores = generateSomeEntities();
        Candidate c = new Candidate("username");

        when(candidateServiceMock.getById(1)).thenReturn(c);
        when(repoMock.findHighScoresByCandidateOrderByAchievedAt(c)).thenReturn(highScores);

        assertEquals(highScores, service.getByCandidateSortedByDate(1));
    }

    @Test
    void getByCandidateSortedByDate_deletedCandidateTest(){
        List<HighScore> highScores = generateSomeEntities();
        Candidate c = new Candidate("username");

        when(candidateServiceMock.getById(1)).thenThrow(EntityNotFoundException.class);
        when(repoMock.findHighScoresByCandidateOrderByAchievedAt(c)).thenReturn(highScores);

        assertThrows(EntityNotFoundException.class, () -> service.getByCandidateSortedByDate(1));
    }

    @Test
    void addNewEntityNoAuth_successTest() {
        HighScore entity = generateEntity();
        when(getRepoMock().findById(any())).thenReturn(Optional.empty());
        when(getRepoMock().save(entity)).thenReturn(entity);

        assertEquals(entity, service.addNew(entity));
    }

    @Test
    void addNewEntityNoAuth_alreadyExistsTest() {
        HighScore entity = generateEntity();
        when(getRepoMock().findById(any())).thenReturn(Optional.of(entity));
        when(getRepoMock().save(entity)).thenReturn(entity);

        assertThrows(EntityAlreadyExistsException.class, () -> service.addNew(entity));
    }

    @Test
    void addNewDTONoAuth_successTest() {
        HighScoreDTO dto = generateDTO();
        HighScore entity = parseToEntity(dto);
        when(getRepoMock().findById(any())).thenReturn(Optional.empty());
        when(getRepoMock().save(any())).thenReturn(entity);

        assertEquals(dto, service.addNew(dto));
    }

    @Test
    void addNewDTONoAuth_alreadyExistsTest() {
        HighScoreDTO dto = generateDTO();
        HighScore entity = dto.parseToEntity();
        when(getRepoMock().findById(any())).thenReturn(Optional.of(entity));
        when(getRepoMock().save(any())).thenReturn(entity);

        assertThrows(EntityAlreadyExistsException.class, () -> service.addNew(dto));
    }

    @Test //Override because parseEntity in Candidate ignores id -> leads to fail here
    void addNewDTO_successTest() {
        HighScoreDTO dto = generateDTO();
        HighScore entity = parseToEntity(dto);
        when(tokenServiceMock.isTokenValid(anyString())).thenReturn(true);
        when(getRepoMock().findById(any())).thenReturn(Optional.empty());
        when(getRepoMock().save(any())).thenReturn(entity);

        assertEquals(dto, getService().addNew("someexistingtoken", dto));
    }

    @Test //Override because parseEntity in Candidate ignores id -> leads to fail here
    void editDTO_successTest() {
        HighScoreDTO dto = generateDTO();
        HighScore entity = parseToEntity(dto);
        when(tokenServiceMock.isTokenValid(anyString())).thenReturn(true);
        when(getRepoMock().findById(any())).thenReturn(Optional.of(entity));
        when(getRepoMock().save(any())).thenReturn(entity);

        assertEquals(dto, getService().edit("someexistingtoken", dto));
    }

    @Override
    protected HighScoreRepo getRepoMock() {
        return repoMock;
    }

    @Override
    protected GenericAuthenticatedEntityService<Integer, HighScoreDTO, HighScore, HighScoreRepo> getService() {
        return service;
    }

    @Override
    protected HighScore generateEntity() {
        return generateEntity(1);
    }

    private HighScore generateEntity(Integer id) {
        HighScore h = new HighScore(100, LocalDateTime.now(), new Candidate("candidate"));
        h.setId(id);
        return h;
    }

    @Override
    protected List<HighScore> generateSomeEntities() {
        List<HighScore> highScores = new ArrayList<>();
        for (int i = 1; i < 5; i++)
            highScores.add(generateEntity(i));
        return highScores;
    }

    @Override
    protected HighScoreDTO generateDTO() {
        return new HighScoreDTO(1, 100, LocalDateTime.now(), new CandidateDTO(1, "candidate"));
    }

    @Override
    protected boolean doesDTOEqualEntity(HighScoreDTO dto, HighScore entity) {
        return dto.getId().equals(entity.getId()) &&
                dto.getScore() == entity.getScore() &&
                dto.getAchievedAt().equals(entity.getAchievedAt()) &&
                dto.getCandidateDTO().equals(entity.getCandidate().parseToDTO());
    }

    /**
     * not using WebDto.parseEntity() because parseEntity in Candidate ignores id -> leads to fail here
     * @param dto the dto to parse
     * @return the parsed entity
     */
    private HighScore parseToEntity(HighScoreDTO dto){
        HighScore h = dto.parseToEntity();
        Candidate c = new Candidate(dto.getCandidateDTO().getUsername());
        c.setId(dto.getCandidateDTO().getId());
        h.setCandidate(c);
        return h;
    }
}