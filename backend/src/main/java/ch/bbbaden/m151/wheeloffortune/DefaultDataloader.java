package ch.bbbaden.m151.wheeloffortune;

import ch.bbbaden.m151.wheeloffortune.auth.api.AuthService;
import ch.bbbaden.m151.wheeloffortune.auth.user.AdminRepo;
import ch.bbbaden.m151.wheeloffortune.auth.user.AdminUser;
import ch.bbbaden.m151.wheeloffortune.game.candidate.CandidateRepo;
import ch.bbbaden.m151.wheeloffortune.game.data.category.Category;
import ch.bbbaden.m151.wheeloffortune.game.data.category.CategoryService;
import ch.bbbaden.m151.wheeloffortune.game.data.question.Question;
import ch.bbbaden.m151.wheeloffortune.game.data.question.QuestionService;
import ch.bbbaden.m151.wheeloffortune.game.data.sentence.Sentence;
import ch.bbbaden.m151.wheeloffortune.game.data.sentence.SentenceService;
import ch.bbbaden.m151.wheeloffortune.util.EncodingUtil;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class DefaultDataloader {
    public static final Logger LOGGER = LoggerFactory.getLogger(DefaultDataloader.class);

    private final AdminRepo adminRepo;
    private final CandidateRepo candidateRepo;

    private final CategoryService categoryService;
    private final SentenceService sentenceService;
    private final QuestionService questionService;
    private final AuthService authService;

    @EventListener(ApplicationReadyEvent.class)
    public void loadData(){
        if(adminRepo.count() == 0 && candidateRepo.count() == 0) {
            //add default admin users
            String salt = EncodingUtil.generateSalt();
            AdminUser admin = new AdminUser("admin", EncodingUtil.hashString("Adm1nUser", salt), salt);
            String bachofnerSalt = EncodingUtil.generateSalt();
            AdminUser secondary = new AdminUser("manuel bachofner",
                    EncodingUtil.hashString("BachofnersPass3", bachofnerSalt), bachofnerSalt);
            adminRepo.saveAll(List.of(admin, secondary));

            //login
            String token = authService.loginAdmin("admin", "Adm1nUser").getBody().getToken();

            //Categories
            Category categoryPc = new Category(1, "PC");
            categoryService.addNew(token, categoryPc);
            Category categoryMtb = new Category(2, "MTB");
            categoryService.addNew(token, categoryMtb);
            Category categoryGeneral = new Category(3, "GENERAL");
            categoryService.addNew(token, categoryGeneral);

            //Sentences
            sentenceService.addNew(token, new Sentence(1, "This PC is cool.", categoryPc));
            sentenceService.addNew(token, new Sentence(2, "Asus is a company.", categoryPc));
            sentenceService.addNew(token, new Sentence(3, "Nvidia is better than AMD!", categoryPc));
            sentenceService.addNew(token, new Sentence(4, "Red key are the best!", categoryPc));

            sentenceService.addNew(token, new Sentence(5, "Orbea is a company.", categoryMtb));
            sentenceService.addNew(token, new Sentence(6, "Yeti bikes are heavy.", categoryMtb));
            sentenceService.addNew(token, new Sentence(7, "Evening rides are awesome.", categoryMtb));
            sentenceService.addNew(token, new Sentence(8, "Fox is better than RockShox", categoryMtb));

            sentenceService.addNew(token, new Sentence(9, "Red flowers look fake.", categoryGeneral));
            sentenceService.addNew(token, new Sentence(10, "I like food.", categoryGeneral));
            sentenceService.addNew(token, new Sentence(11, "School can be annoying.", categoryGeneral));
            sentenceService.addNew(token, new Sentence(12, "What if, the grass was orange?", categoryGeneral));

            //Questions
            questionService.addNew(token, new Question(1, "What CPU is newer?", "Intel i7-12700K", "Intel i9-10900K", true, categoryPc));
            questionService.addNew(token, new Question(2, "What is RGB?", "Red Green Blue", "Right, GOOD & Bold", true, categoryPc));
            questionService.addNew(token, new Question(3, "Should i always water cool?", "YES", "NO", false, categoryPc));
            questionService.addNew(token, new Question(4, "What is CSS used for?", "Validates HTML", "Designs HTML", false, categoryPc));

            questionService.addNew(token, new Question(5, "What bike is usually heavier?", "Yeti SB150", "Scott Spark 900", true, categoryMtb));
            questionService.addNew(token, new Question(6, "Which component is more expensive?", "Sram Code RSC", "Sram XX1-Eagle AXS", false, categoryMtb));
            questionService.addNew(token, new Question(7, "RockShox ... is a XC Fork?", "ZEB", "SID", false, categoryMtb));
            questionService.addNew(token, new Question(8, "Bold only sells hard-tails?", "YES", "NO", true, categoryMtb));

            questionService.addNew(token, new Question(9, "What country has the most inhabitants?", "China", "USA", true, categoryGeneral));
            questionService.addNew(token, new Question(10, "What is the highest Mountain?", "Matterhorn", "Mount Everest", false, categoryGeneral));
            questionService.addNew(token, new Question(11, "How much Ballon d'Or's did Messi Win?", "05", "07", false, categoryGeneral));
            questionService.addNew(token, new Question(12, "How much water is in the pacific Ocean?", "394 million cubic kilometers", "714 million cubic kilometers", false, categoryGeneral));
            LOGGER.info("game data successfully loaded");
        }
    }
}
