package ch.bbbaden.m151.wheeloffortune;

import ch.bbbaden.m151.wheeloffortune.auth.user.AdminRepo;
import ch.bbbaden.m151.wheeloffortune.auth.user.AdminUser;
import ch.bbbaden.m151.wheeloffortune.util.EncodingUtil;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DefaultDataloader {
    public static final Logger LOGGER = LoggerFactory.getLogger(DefaultDataloader.class);

    private final AdminRepo adminRepo;

    @EventListener(ApplicationReadyEvent.class)
    public void loadData(){
        if(adminRepo.count() == 0){
            String salt = EncodingUtil.generateSalt();
            AdminUser admin = new AdminUser("admin", EncodingUtil.hashString("admin", salt), salt);
            adminRepo.save(admin);

            LOGGER.warn("data loaded");
        }
    }
}
