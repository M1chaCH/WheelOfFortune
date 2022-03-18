package ch.bbbaden.m151.wheeloffortune.game.candidate;

import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.entity.EntityAlreadyExistsException;
import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.entity.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class CandidateServiceTest {

    @Autowired
    CandidateService service;

    @MockBean
    CandidateRepo repoMock;

    @Test
    void getById_foundTest(){
        Candidate candidate = new Candidate("username");
        candidate.setId(1);

        when(repoMock.findById(1)).thenReturn(Optional.of(candidate));

        assertEquals(candidate, service.getById(1));
    }

    @Test
    void getById_notFoundTest(){
        when(repoMock.findById(any())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.getById(1));
    }

    @Test
    void getByUsername_foundTest() {
        String username = "username";
        when(repoMock.findCandidateByUsername(username)).thenReturn(Optional.of(new Candidate(username)));

        assertEquals(new CandidateDTO(username), service.getByUsername(username));
    }

    @Test
    void getByUsername_notFoundTest() {
        String username = "username";
        when(repoMock.findCandidateByUsername(username)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.getByUsername(username));
    }

    @Test
    void saveCandidate_successTest() {
        CandidateDTO dto = new CandidateDTO("username");
        Candidate entity = new Candidate(dto.getUsername());

        when(repoMock.findCandidateByUsername(dto.getUsername())).thenReturn(Optional.empty());
        when(repoMock.save(any())).thenReturn(entity);

        assertEquals(dto.getUsername(), service.saveCandidate(dto).getUsername());
    }

    @Test
    void saveCandidate_existsTest() {
        CandidateDTO dto = new CandidateDTO("username");
        Candidate entity = new Candidate(dto.getUsername());

        when(repoMock.save(entity)).thenReturn(entity);
        when(repoMock.findCandidateByUsername(dto.getUsername())).thenReturn(Optional.of(entity));

        assertThrows(EntityAlreadyExistsException.class, () -> service.saveCandidate(dto));
    }
}