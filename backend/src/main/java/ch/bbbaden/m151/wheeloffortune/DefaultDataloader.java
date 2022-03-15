package ch.bbbaden.m151.wheeloffortune;

import ch.bbbaden.m151.wheeloffortune.auth.user.AdminRepo;
import ch.bbbaden.m151.wheeloffortune.auth.user.AdminUser;
import ch.bbbaden.m151.wheeloffortune.game.candidate.Candidate;
import ch.bbbaden.m151.wheeloffortune.game.candidate.CandidateRepo;
import ch.bbbaden.m151.wheeloffortune.game.data.category.Category;
import ch.bbbaden.m151.wheeloffortune.game.data.category.CategoryRepo;
import ch.bbbaden.m151.wheeloffortune.game.data.question.Question;
import ch.bbbaden.m151.wheeloffortune.game.data.question.QuestionRepo;
import ch.bbbaden.m151.wheeloffortune.game.data.sentence.Sentence;
import ch.bbbaden.m151.wheeloffortune.game.data.sentence.SentenceRepo;
import ch.bbbaden.m151.wheeloffortune.game.highscore.HighScore;
import ch.bbbaden.m151.wheeloffortune.game.highscore.HighScoreRepo;
import ch.bbbaden.m151.wheeloffortune.util.EncodingUtil;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
public class DefaultDataloader {
    public static final Logger LOGGER = LoggerFactory.getLogger(DefaultDataloader.class);

    private final AdminRepo adminRepo;
    private final CandidateRepo candidateRepo;
    private final CategoryRepo categoryRepo;
    private final QuestionRepo questionRepo;
    private final SentenceRepo sentenceRepo;
    private final HighScoreRepo highScoreRepo;

    @EventListener(ApplicationReadyEvent.class)
    public void loadData(){
        if(adminRepo.count() == 0 && candidateRepo.count() == 0){
            String salt = EncodingUtil.generateSalt();
            AdminUser admin = new AdminUser("admin", EncodingUtil.hashString("admin", salt), salt);
            adminRepo.save(admin);

            Candidate candidate = new Candidate("teste");
            Candidate candidate1 = new Candidate("candidate");
            candidateRepo.saveAll(List.of(candidate, candidate1));

            Category categoryPc = new Category("PC");
            Category categoryMtb = new Category("MTB");
            categoryRepo.saveAll(List.of(categoryPc, categoryMtb));

            Question q1 = new Question("Select the newer CPU?", "Intel i7-12700K", "Intel i9-10900K", true, categoryPc);
            Question q2 = new Question("What bike is usually heavier?", "Yeti SB150", "Scott Spark 900", false, categoryMtb);
            Question q3 = new Question("Which component is more expensive?", "Sram Code RSC", "Sram XX1-Eagle AXS", false, categoryMtb);
            questionRepo.saveAll(List.of(q1,q2,q3));

            Sentence s1 = new Sentence("This PC is cool.", categoryPc);
            Sentence s2 = new Sentence("Asus is a company.", categoryPc);
            Sentence s3 = new Sentence("Orbea is a company.", categoryPc);
            sentenceRepo.saveAll(List.of(s1,s2,s3));

            HighScore highScore = new HighScore(5500, LocalDateTime.now(), candidate);
            HighScore highScore1 = new HighScore(7000, LocalDateTime.now().plusHours(1), candidate);
            HighScore highScore2 = new HighScore(7050, LocalDateTime.now().plusMinutes(90), candidate1);
            highScoreRepo.saveAll(List.of(highScore, highScore1, highScore2));

            LOGGER.warn("data loaded");
        }
    }
}
