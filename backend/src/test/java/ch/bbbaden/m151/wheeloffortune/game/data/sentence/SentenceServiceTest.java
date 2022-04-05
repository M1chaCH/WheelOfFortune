package ch.bbbaden.m151.wheeloffortune.game.data.sentence;

import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.entity.EntityNotFoundException;
import ch.bbbaden.m151.wheeloffortune.game.data.GenericAuthenticatedEntityService;
import ch.bbbaden.m151.wheeloffortune.game.data.GenericAuthenticatedEntityServiceTest;
import ch.bbbaden.m151.wheeloffortune.game.data.category.Category;
import ch.bbbaden.m151.wheeloffortune.game.data.category.CategoryDTO;
import ch.bbbaden.m151.wheeloffortune.game.data.category.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class SentenceServiceTest extends GenericAuthenticatedEntityServiceTest<Integer, SentenceDTO, Sentence, SentenceRepo>{

    @Autowired
    SentenceService service;

    @MockBean
    SentenceRepo repoMock;

    @MockBean
    CategoryService categoryServiceMock;

    @Test
    void getAllByCategory_successTest() {
        final Integer categoryId = 1;
        List<Sentence> entities = generateSomeEntities();
        Category category = new CategoryDTO(categoryId, "random").parseToEntity();
        when(categoryServiceMock.getById(categoryId)).thenReturn(category);
        when(repoMock.findSentencesByCategory(category)).thenReturn(entities);

        List<Sentence> returnedEntities = service.getAllByCategory(categoryId);
        assertEquals(entities.size(), returnedEntities.size());
        for (int i = 0; i < entities.size(); i++) {
            assertEquals(entities.get(i), returnedEntities.get(i));
        }
    }

    @Test
    void getAllByCategory_deletedCategoryTest() {
        final Integer categoryId = 2;
        List<Sentence> entities = generateSomeEntities();
        Category category = new CategoryDTO(categoryId, "random").parseToEntity();
        when(categoryServiceMock.getById(categoryId)).thenThrow(EntityNotFoundException.class);
        when(repoMock.findSentencesByCategory(category)).thenReturn(entities);

        assertThrows(EntityNotFoundException.class, () -> service.getAllByCategory(categoryId));
    }

    @Test
    void getAllAsDtoByCategory_successTest() {
        final Integer categoryId = 1;
        List<Sentence> entities = generateSomeEntities();
        Category category = new CategoryDTO(categoryId, "random").parseToEntity();
        when(categoryServiceMock.getById(categoryId)).thenReturn(category);
        when(repoMock.findSentencesByCategory(category)).thenReturn(entities);

        List<SentenceDTO> returnedDTOs = service.getAllAsDtoByCategory(categoryId);
        assertEquals(entities.size(), returnedDTOs.size());
        for (int i = 0; i < entities.size(); i++) {
            assertTrue(doesDTOEqualEntity(returnedDTOs.get(i), entities.get(i)));
        }
    }

    @Test
    void getAllAsDtoByCategory_deletedCategoryTest() {
        final Integer categoryId = 2;
        List<Sentence> entities = generateSomeEntities();
        Category category = new CategoryDTO(categoryId, "random").parseToEntity();
        when(categoryServiceMock.getById(categoryId)).thenThrow(EntityNotFoundException.class);
        when(repoMock.findSentencesByCategory(category)).thenReturn(entities);

        assertThrows(EntityNotFoundException.class, () -> service.getAllAsDtoByCategory(categoryId));
    }

    @Test
    void isEntityValid_trueTest(){
        Sentence sentence = generateEntity();
        assertTrue(service.isEntityValid(sentence));
    }

    @Test
    void isEntityValid_sentenceShortTest(){
        Sentence sentence = generateEntity();
        sentence.setSentence("123456789");
        assertFalse(service.isEntityValid(sentence));
    }

    @Test
    void isEntityValid_sentenceLongTest(){
        Sentence sentence = generateEntity();
        sentence.setSentence("this is a way to long sentence. it cant ever be displayed.");
        assertFalse(service.isEntityValid(sentence));
    }

    @Test
    void isEntityValid_containsInvalidTest(){
        Sentence sentence = generateEntity();

        sentence.setSentence("this sentence <> is illegal");
        assertFalse(service.isEntityValid(sentence));

        sentence.setSentence("this sentence ;is illegal");
        assertFalse(service.isEntityValid(sentence));

        sentence.setSentence("this123 sentence is illegal");
        assertFalse(service.isEntityValid(sentence));
    }

    @Test
    void isEntityValid_categoryInvalidTest(){
        Sentence sentence = generateEntity();
        when(categoryServiceMock.isEntityValid(any())).thenReturn(false);
        assertFalse(service.isEntityValid(sentence));
    }

    @Override
    protected SentenceRepo getRepoMock() {
        return repoMock;
    }

    @Override
    protected GenericAuthenticatedEntityService<Integer, SentenceDTO, Sentence, SentenceRepo> getGenericService() {
        return service;
    }

    @Override
    protected void mockAdditionalBeans() {
        super.mockAdditionalBeans();
        when(categoryServiceMock.isEntityValid(any())).thenReturn(true);
    }

    @Override
    protected Sentence generateEntity() {
        return generateEntity(1);
    }

    protected Sentence generateEntity(Integer id) {
        Sentence sentence = new Sentence(
                "This is just some Sentence!",
                new CategoryDTO(1, "random").parseToEntity()
        );
        sentence.setId(id);
        return sentence;
    }

    @Override
    protected List<Sentence> generateSomeEntities() {
        List<Sentence> sentences = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            sentences.add(generateEntity(i));
        }
        return sentences;
    }

    @Override
    protected SentenceDTO generateDTO() {
        return new SentenceDTO(1, "This is just some Sentence!", new CategoryDTO(1, "random"));
    }

    @Override
    protected boolean doesDTOEqualEntity(SentenceDTO dto, Sentence entity) {
        return dto.getId().equals(entity.getId()) &&
                dto.getSentence().equals(entity.getSentence()) &&
                dto.getCategoryDTO().equals(entity.getCategory().parseToDTO());
    }
}