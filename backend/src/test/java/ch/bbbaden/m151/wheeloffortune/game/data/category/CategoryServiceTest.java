package ch.bbbaden.m151.wheeloffortune.game.data.category;

import ch.bbbaden.m151.wheeloffortune.game.data.GenericAuthenticatedEntityService;
import ch.bbbaden.m151.wheeloffortune.game.data.GenericAuthenticatedEntityServiceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class CategoryServiceTest extends GenericAuthenticatedEntityServiceTest<Integer, CategoryDTO, Category, CategoryRepo> {

    @Autowired
    private CategoryService service;

    @MockBean
    CategoryRepo repoMock;

    @Test
    void isEntityValid_trueTest(){
        Category category = new Category("Categroy");
        assertTrue(service.isEntityValid(category));
    }

    @Test
    void isEntityValid_nameShortTest(){
        Category category = new Category("1");
        assertFalse(service.isEntityValid(category));
    }

    @Test
    void isEntityValid_nameLongTest(){
        Category category = new Category("This is a way to long category name.");
        assertFalse(service.isEntityValid(category));
    }

    @Override
    protected CategoryRepo getRepoMock() {
        return repoMock;
    }

    @Override
    protected GenericAuthenticatedEntityService<Integer, CategoryDTO, Category, CategoryRepo> getGenericService() {
        return service;
    }

    @Override
    protected Category generateEntity() {
        return generateEntity(1);
    }

    private Category generateEntity(Integer id) {
        Category category = new Category();
        category.setId(id);
        category.setName("category" + id);
        return category;
    }

    @Override
    protected List<Category> generateSomeEntities() {
        List<Category> categories = new ArrayList<>();
        for (int i = 1; i < 5; i++)
            categories.add(generateEntity(i));
        return categories;
    }

    @Override
    protected CategoryDTO generateDTO() {
        return new CategoryDTO(1, "category" + 1);
    }

    @Override
    protected boolean doesDTOEqualEntity(CategoryDTO dto, Category entity) {
        return dto.getId().equals(entity.getId()) && dto.getName().equals(entity.getName());
    }
}