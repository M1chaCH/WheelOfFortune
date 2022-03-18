package ch.bbbaden.m151.wheeloffortune.game.candidate;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/game/candidate")
public class CandidateController {

    private final CandidateService service;

    @GetMapping
    public ResponseEntity<CandidateDTO> searchByUsername(@RequestBody CandidateDTO candidateDTO){
        return ResponseEntity.ok(service.getByUsername(candidateDTO.getUsername()).parseToDTO());
    }

    @PostMapping
    public ResponseEntity<CandidateDTO> createNew(@RequestBody CandidateDTO candidateDTO){
        return ResponseEntity.ok(service.saveCandidate(candidateDTO).parseToDTO());
    }
}
