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
import java.util.ArrayList;
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

            Candidate candidate = new Candidate("test");
            Candidate candidate1 = new Candidate("candidate");
            candidateRepo.saveAll(List.of(candidate, candidate1));

            Category categoryPc = new Category("PC");
            Category categoryMtb = new Category("MTB");
            Category testCategory = new Category("test");
            categoryRepo.saveAll(List.of(categoryPc, categoryMtb, testCategory));

            Question q1 = new Question("Select the newer CPU?", "Intel i7-12700K", "Intel i9-10900K", true, categoryPc);
            Question q2 = new Question("What bike is usually heavier?", "Yeti SB150", "Scott Spark 900", true, categoryMtb);
            Question q3 = new Question("Which component is more expensive?", "Sram Code RSC", "Sram XX1-Eagle AXS", false, categoryMtb);
            questionRepo.saveAll(List.of(q1,q2,q3));

            List<Question> questions = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                questions.add(new Question("This is a question: " + i, "A1", "A2", true, testCategory));
            }
            questionRepo.saveAll(questions);

            Sentence s1 = new Sentence("This PC is cool.", categoryPc);
            Sentence s2 = new Sentence("Asus is a company.", categoryPc);
            Sentence s3 = new Sentence("Orbea is a company.", categoryMtb);
            sentenceRepo.saveAll(List.of(s1,s2,s3));

            List<Sentence> sentences = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                sentences.add(new Sentence("This is a Test Sentence " + i, testCategory));
            }
            sentenceRepo.saveAll(sentences);

            HighScore highScore = new HighScore(5500, 1, "test", LocalDateTime.now());
            HighScore highScore1 = new HighScore(7000, 1, "test1", LocalDateTime.now().plusHours(1));
            HighScore highScore2 = new HighScore(1243, 1, "test2", LocalDateTime.now().plusMinutes(90));
            HighScore highScore3 = new HighScore(7050, 1, "test3", LocalDateTime.now().plusMinutes(91));
            HighScore highScore4 = new HighScore(3523, 1, "test4", LocalDateTime.now().plusMinutes(92));
            HighScore highScore5 = new HighScore(4563, 1, "test5", LocalDateTime.now().plusMinutes(93));
            HighScore highScore6 = new HighScore(7050, 1, "test6", LocalDateTime.now().plusMinutes(94));
            HighScore highScore7 = new HighScore(7050, 1, "test7", LocalDateTime.now().plusMinutes(95));
            HighScore highScore8 = new HighScore(7050, 1, "test8", LocalDateTime.now().plusMinutes(96));
            HighScore highScore9 = new HighScore(1000, 1, "test9", LocalDateTime.now().plusMinutes(97));
            HighScore highScore10 = new HighScore(1000, 1, "test10", LocalDateTime.now().plusMinutes(97));
            HighScore highScore11 = new HighScore(1000, 1, "test11", LocalDateTime.now().plusMinutes(32));
            HighScore highScore12 = new HighScore(1000, 1, "test12", LocalDateTime.now().plusMinutes(197));
            HighScore highScore13 = new HighScore(1000, 1, "test13", LocalDateTime.now().plusMinutes(93));
            HighScore highScore14 = new HighScore(1000, 1, "test14", LocalDateTime.now().plusMinutes(943));
            HighScore highScore15 = new HighScore(1000, 1, "test15", LocalDateTime.now().plusMinutes(77));
            highScoreRepo.saveAll(List.of(highScore, highScore1, highScore2, highScore3, highScore4,highScore5, highScore6,
                    highScore7, highScore8, highScore9, highScore10, highScore11, highScore12, highScore13, highScore14,
                    highScore15));

            LOGGER.warn("data loaded");
        }
    }
}
