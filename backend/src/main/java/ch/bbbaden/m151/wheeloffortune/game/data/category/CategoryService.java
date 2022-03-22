package ch.bbbaden.m151.wheeloffortune.game.data.category;

import ch.bbbaden.m151.wheeloffortune.auth.token.SecurityTokenService;
import ch.bbbaden.m151.wheeloffortune.game.data.GenericAuthenticatedEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService extends GenericAuthenticatedEntityService<Integer, CategoryDTO, Category, CategoryRepo> {

    public static final int MAX_CATEGORY_NAME_LENGTH = 20;
    public static final int MIN_CATEGORY_NAME_LENGTH = 2;

    @Autowired
    protected CategoryService(SecurityTokenService securityTokenService, CategoryRepo repo) {
        super(securityTokenService, repo);
    }

    @Override
    public boolean isEntityValid(Category entity) {
        try{
            String name = entity.getName();
            return name.length() >= MIN_CATEGORY_NAME_LENGTH && name.length() <= MAX_CATEGORY_NAME_LENGTH;
        }catch (Exception e){ //to lazy to check for null (:
            return false;
        }
    }
}
