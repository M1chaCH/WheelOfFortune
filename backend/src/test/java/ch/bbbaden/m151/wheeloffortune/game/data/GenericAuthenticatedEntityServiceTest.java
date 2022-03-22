package ch.bbbaden.m151.wheeloffortune.game.data;

import ch.bbbaden.m151.wheeloffortune.auth.token.SecurityTokenService;
import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.auth.InvalidatedSecurityTokenException;
import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.auth.SecurityTokenNotFoundException;
import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.entity.EntityAlreadyExistsException;
import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.entity.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public abstract class GenericAuthenticatedEntityServiceTest<I, D extends WebDto<I, E>, E extends WebEntity<I, D>, R extends CrudRepository<E, I>> {

    @MockBean
    protected SecurityTokenService tokenServiceMock;

    protected abstract R getRepoMock();
    protected abstract GenericAuthenticatedEntityService<I, D, E, R> getGenericService();
    protected void mockAdditionalBeans() {}

    protected abstract E generateEntity();
    protected abstract List<E> generateSomeEntities();
    protected abstract D generateDTO();
    protected abstract boolean doesDTOEqualEntity(D dto, E entity);

    @BeforeEach
    void init(){
        mockAdditionalBeans();
    }

    @Test
    void getAllAsDto_Test() {
        List<E> givenEntities = generateSomeEntities();

        when(getRepoMock().findAll()).thenReturn(givenEntities);

        List<D> returnedDTOs = getGenericService().getAllAsDto();
        assertEquals(givenEntities.size(), returnedDTOs.size());

        for (int i = 0; i < givenEntities.size(); i++)
            assertTrue(doesDTOEqualEntity(returnedDTOs.get(i), givenEntities.get(i)));
    }

    @Test
    void getById_existingTest() {
        E entity = generateEntity();
        when(getRepoMock().findById(entity.getId())).thenReturn(Optional.of(entity));
        assertEquals(entity, getGenericService().getById(entity.getId()));
    }

    @Test
    void getById_notExistingTest() {
        when(getRepoMock().findById(any())).thenReturn(Optional.empty());
        GenericAuthenticatedEntityService<I, D, E, R> service = getGenericService();
        assertThrows(EntityNotFoundException.class, () -> service.getById(null));
    }

    @Test
    void getByIdAsDto_existingTest() {
        E entity = generateEntity();
        when(getRepoMock().findById(entity.getId())).thenReturn(Optional.of(entity));
        assertTrue(doesDTOEqualEntity(getGenericService().getByIdAsDto(entity.getId()), entity));
    }

    @Test
    void getByIdAsDto_notExistingTest() {
        when(getRepoMock().findById(any())).thenReturn(Optional.empty());
        GenericAuthenticatedEntityService<I, D, E, R> service = getGenericService();
        assertThrows(EntityNotFoundException.class, () -> service.getByIdAsDto(null));
    }

    @Test
    void addNewEntity_successTest() {
        E entity = generateEntity();
        when(tokenServiceMock.isTokenValid(anyString())).thenReturn(true);
        when(getRepoMock().findById(any())).thenReturn(Optional.empty());
        when(getRepoMock().save(entity)).thenReturn(entity);

        assertEquals(entity, getGenericService().addNew("someexistingtoken", entity));
    }

    @Test
    void addNewEntity_alreadyExistsTest() {
        E entity = generateEntity();
        when(tokenServiceMock.isTokenValid(anyString())).thenReturn(true);
        when(getRepoMock().findById(any())).thenReturn(Optional.of(entity));
        when(getRepoMock().save(entity)).thenReturn(entity);

        GenericAuthenticatedEntityService<I, D, E, R> service = getGenericService();
        assertThrows(EntityAlreadyExistsException.class, () ->
                service.addNew("someexistingtoken", entity));
    }

    @Test
    void addNewEntity_invalidTokenTest() {
        E entity = generateEntity();
        String tokenString = "somefaketoken";
        when(tokenServiceMock.isTokenValid(tokenString)).thenThrow(new InvalidatedSecurityTokenException(tokenString));
        when(getRepoMock().findById(any())).thenReturn(Optional.empty());
        when(getRepoMock().save(entity)).thenReturn(entity);

        GenericAuthenticatedEntityService<I, D, E, R> service = getGenericService();
        assertThrows(InvalidatedSecurityTokenException.class, () -> service.addNew(tokenString, entity));
    }

    @Test
    void addNewEntity_deletedTokenTest() {
        E entity = generateEntity();
        String tokenString = "somefaketoken";
        when(tokenServiceMock.isTokenValid(tokenString)).thenThrow(new SecurityTokenNotFoundException(tokenString));
        when(getRepoMock().findById(any())).thenReturn(Optional.empty());
        when(getRepoMock().save(entity)).thenReturn(entity);

        GenericAuthenticatedEntityService<I, D, E, R> service = getGenericService();
        assertThrows(SecurityTokenNotFoundException.class, () -> service.addNew(tokenString, entity));
    }

    @Test
    void addNewDTO_successTest() {
        D dto = generateDTO();
        E entity = dto.parseToEntity();
        when(tokenServiceMock.isTokenValid(anyString())).thenReturn(true);
        when(getRepoMock().findById(any())).thenReturn(Optional.empty());
        when(getRepoMock().save(any())).thenReturn(entity);

        assertEquals(dto, getGenericService().addNew("someexistingtoken", dto));
    }

    @Test
    void addNewDTO_alreadyExistsTest() {
        D dto = generateDTO();
        E entity = dto.parseToEntity();
        when(tokenServiceMock.isTokenValid(anyString())).thenReturn(true);
        when(getRepoMock().findById(any())).thenReturn(Optional.of(entity));
        when(getRepoMock().save(any())).thenReturn(entity);

        GenericAuthenticatedEntityService<I, D, E, R> service = getGenericService();
        assertThrows(EntityAlreadyExistsException.class, () ->
                service.addNew("someexistingtoken", dto));
    }

    @Test
    void addNewDTO_invalidTokenTest() {
        D dto = generateDTO();
        E entity = dto.parseToEntity();
        String tokenString = "somefaketoken";
        when(tokenServiceMock.isTokenValid(tokenString)).thenThrow(new InvalidatedSecurityTokenException(tokenString));
        when(getRepoMock().findById(any())).thenReturn(Optional.empty());
        when(getRepoMock().save(any())).thenReturn(entity);

        GenericAuthenticatedEntityService<I, D, E, R> service = getGenericService();
        assertThrows(InvalidatedSecurityTokenException.class, () -> service.addNew(tokenString, dto));
    }

    @Test
    void addNewDTO_deletedTokenTest() {
        D dto = generateDTO();
        E entity = dto.parseToEntity();
        String tokenString = "somefaketoken";
        when(tokenServiceMock.isTokenValid(tokenString)).thenThrow(new SecurityTokenNotFoundException(tokenString));
        when(getRepoMock().findById(any())).thenReturn(Optional.empty());
        when(getRepoMock().save(any())).thenReturn(entity);

        GenericAuthenticatedEntityService<I, D, E, R> service = getGenericService();
        assertThrows(SecurityTokenNotFoundException.class, () -> service.addNew(tokenString, dto));
    }

    @Test
    void editEntity_successTest() {
        E entity = generateEntity();
        when(tokenServiceMock.isTokenValid(anyString())).thenReturn(true);
        when(getRepoMock().findById(any())).thenReturn(Optional.of(entity));
        when(getRepoMock().save(entity)).thenReturn(entity);

        assertEquals(entity, getGenericService().edit("someexistingtoken", entity));
    }

    @Test
    void editEntity_notExistingTest() {
        E entity = generateEntity();
        when(tokenServiceMock.isTokenValid(anyString())).thenReturn(true);
        when(getRepoMock().findById(any())).thenReturn(Optional.empty());
        when(getRepoMock().save(entity)).thenReturn(entity);

        GenericAuthenticatedEntityService<I, D, E, R> service = getGenericService();
        assertThrows(EntityNotFoundException.class, () -> service.edit("someexistingtoken", entity));
    }

    @Test
    void editEntity_invalidTokenTest() {
        E entity = generateEntity();
        String tokenString = "invalidtoken";
        when(tokenServiceMock.isTokenValid(tokenString)).thenThrow(new InvalidatedSecurityTokenException(tokenString));
        when(getRepoMock().findById(any())).thenReturn(Optional.of(entity));
        when(getRepoMock().save(entity)).thenReturn(entity);

        GenericAuthenticatedEntityService<I, D, E, R> service = getGenericService();
        assertThrows(InvalidatedSecurityTokenException.class, () -> service.edit(tokenString, entity));
    }

    @Test
    void editEntity_deletedTokenTest() {
        E entity = generateEntity();
        String tokenString = "deletedtoken";
        when(tokenServiceMock.isTokenValid(tokenString)).thenThrow(new SecurityTokenNotFoundException(tokenString));
        when(getRepoMock().findById(any())).thenReturn(Optional.of(entity));
        when(getRepoMock().save(entity)).thenReturn(entity);

        GenericAuthenticatedEntityService<I, D, E, R> service = getGenericService();
        assertThrows(SecurityTokenNotFoundException.class, () -> service.edit(tokenString, entity));
    }

    @Test
    void editDTO_successTest() {
        D dto = generateDTO();
        E entity = dto.parseToEntity();
        when(tokenServiceMock.isTokenValid(anyString())).thenReturn(true);
        when(getRepoMock().findById(any())).thenReturn(Optional.of(entity));
        when(getRepoMock().save(any())).thenReturn(entity);

        assertEquals(dto, getGenericService().edit("someexistingtoken", dto));
    }

    @Test
    void editDTO_notExistingTest() {
        D dto = generateDTO();
        E entity = dto.parseToEntity();
        when(tokenServiceMock.isTokenValid(anyString())).thenReturn(true);
        when(getRepoMock().findById(any())).thenReturn(Optional.empty());
        when(getRepoMock().save(any())).thenReturn(entity);

        GenericAuthenticatedEntityService<I, D, E, R> service = getGenericService();
        assertThrows(EntityNotFoundException.class, () -> service.edit("someexistingtoken", dto));
    }

    @Test
    void editDTO_invalidTokenTest() {
        D dto = generateDTO();
        E entity = dto.parseToEntity();
        String tokenString = "invalidtoken";
        when(tokenServiceMock.isTokenValid(tokenString)).thenThrow(new InvalidatedSecurityTokenException(tokenString));
        when(getRepoMock().findById(any())).thenReturn(Optional.of(entity));
        when(getRepoMock().save(any())).thenReturn(entity);

        GenericAuthenticatedEntityService<I, D, E, R> service = getGenericService();
        assertThrows(InvalidatedSecurityTokenException.class, () -> service.edit(tokenString, dto));
    }

    @Test
    void editDTO_deletedTokenTest() {
        D dto = generateDTO();
        E entity = dto.parseToEntity();
        String tokenString = "deletedtoken";
        when(tokenServiceMock.isTokenValid(tokenString)).thenThrow(new SecurityTokenNotFoundException(tokenString));
        when(getRepoMock().findById(any())).thenReturn(Optional.of(entity));
        when(getRepoMock().save(any())).thenReturn(entity);

        GenericAuthenticatedEntityService<I, D, E, R> service = getGenericService();
        assertThrows(SecurityTokenNotFoundException.class, () -> service.edit(tokenString, dto));
    }

    @Test
    void deleteEntity_successTest() {
        E entity = generateEntity();
        when(tokenServiceMock.isTokenValid(anyString())).thenReturn(true);
        when(getRepoMock().findById(any())).thenReturn(Optional.of(entity));

        try{
            getGenericService().delete("someexistingtoken", entity.getId());
        }catch (Exception e){
            fail(e);
        }
    }

    @Test
    void deleteEntity_notExistingTest() {
        E entity = generateEntity();
        when(tokenServiceMock.isTokenValid(anyString())).thenReturn(true);
        when(getRepoMock().findById(any())).thenReturn(Optional.empty());

        GenericAuthenticatedEntityService<I, D, E, R> service = getGenericService();
        I id = entity.getId();
        assertThrows(EntityNotFoundException.class, () -> service.delete("someexistingtoken", id));
    }

    @Test
    void deleteEntity_invalidTokenTest() {
        E entity = generateEntity();
        String tokenString = "invalidtoken";
        when(tokenServiceMock.isTokenValid(tokenString)).thenThrow(new InvalidatedSecurityTokenException(tokenString));
        when(getRepoMock().findById(any())).thenReturn(Optional.of(entity));

        GenericAuthenticatedEntityService<I, D, E, R> service = getGenericService();
        I id = entity.getId();
        assertThrows(InvalidatedSecurityTokenException.class, () -> service.delete(tokenString, id));
    }

    @Test
    void deleteEntity_deletedTokenTest() {
        E entity = generateEntity();
        String tokenString = "deletedtoken";
        when(tokenServiceMock.isTokenValid(tokenString)).thenThrow(new SecurityTokenNotFoundException(tokenString));
        when(getRepoMock().findById(any())).thenReturn(Optional.of(entity));

        GenericAuthenticatedEntityService<I, D, E, R> service = getGenericService();
        I id = entity.getId();
        assertThrows(SecurityTokenNotFoundException.class, () -> service.delete(tokenString, id));
    }

    @Test
    void deleteDTO_successTest() {
        D dto = generateDTO();
        E entity = dto.parseToEntity();
        when(tokenServiceMock.isTokenValid(anyString())).thenReturn(true);
        when(getRepoMock().findById(any())).thenReturn(Optional.of(entity));

        try{
            getGenericService().delete("someexistingtoken", dto.getId());
        }catch (Exception e){
            fail(e);
        }
    }

    @Test
    void deleteDTO_notExistingTest() {
        D dto = generateDTO();
        when(tokenServiceMock.isTokenValid(anyString())).thenReturn(true);
        when(getRepoMock().findById(any())).thenReturn(Optional.empty());

        GenericAuthenticatedEntityService<I, D, E, R> service = getGenericService();
        I id = dto.getId();
        assertThrows(EntityNotFoundException.class, () -> service.delete("someexistingtoken", id));
    }

    @Test
    void deleteDTO_invalidTokenTest() {
        D dto = generateDTO();
        E entity = dto.parseToEntity();
        String tokenString = "invalidtoken";
        when(tokenServiceMock.isTokenValid(tokenString)).thenThrow(new InvalidatedSecurityTokenException(tokenString));
        when(getRepoMock().findById(any())).thenReturn(Optional.of(entity));

        GenericAuthenticatedEntityService<I, D, E, R> service = getGenericService();
        I id = dto.getId();
        assertThrows(InvalidatedSecurityTokenException.class, () -> service.delete(tokenString, id));
    }

    @Test
    void deleteDTO_deletedTokenTest() {
        D dto = generateDTO();
        E entity = dto.parseToEntity();
        String tokenString = "deletedtoken";
        when(tokenServiceMock.isTokenValid(tokenString)).thenThrow(new SecurityTokenNotFoundException(tokenString));
        when(getRepoMock().findById(any())).thenReturn(Optional.of(entity));

        GenericAuthenticatedEntityService<I, D, E, R> service = getGenericService();
        I id = dto.getId();
        assertThrows(SecurityTokenNotFoundException.class, () -> service.delete(tokenString, id));
    }
}