package ch.bbbaden.m151.wheeloffortune.game.candidate;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/game/candidate")
public class CandidateController {

    private final CandidateService service;

    @GetMapping("/{username}")
    public ResponseEntity<CandidateDTO> searchByUsername(@PathVariable String username){
        return ResponseEntity.ok(service.getByUsername(username));
    }

    @PostMapping
    public ResponseEntity<CandidateDTO> createNew(@RequestBody CandidateDTO candidateDTO){
        return ResponseEntity.ok(service.saveCandidate(candidateDTO));
    }
}
