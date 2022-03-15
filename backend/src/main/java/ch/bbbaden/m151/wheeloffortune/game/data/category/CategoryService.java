package ch.bbbaden.m151.wheeloffortune.game.data.category;

import ch.bbbaden.m151.wheeloffortune.auth.token.SecurityTokenService;
import ch.bbbaden.m151.wheeloffortune.game.data.GenericAuthenticatedEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService extends GenericAuthenticatedEntityService<Integer, CategoryDTO, Category, CategoryRepo> {

    @Autowired
    protected CategoryService(SecurityTokenService securityTokenService, CategoryRepo repo) {
        super(securityTokenService, repo);
    }
}
