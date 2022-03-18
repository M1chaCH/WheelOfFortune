package ch.bbbaden.m151.wheeloffortune.game.data;

import ch.bbbaden.m151.wheeloffortune.auth.token.SecurityTokenService;
import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.auth.InvalidatedSecurityTokenException;
import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.auth.SecurityTokenNotFoundException;
import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.entity.EntityAlreadyExistsException;
import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.entity.EntityNotFoundException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * This is a Generic Service that implements basic error-handling and authentication for the crud operations.
 * <br><strong>read operations are NOT authenticated!!</strong>
 * @param <I> the id of the entity of the service
 * @param <E> the entity of the service (must extend {@link WebEntity})
 * @param <R> the repository to access the entity in the database (must extend {@link CrudRepository})
 */
@Service
public abstract class GenericAuthenticatedEntityService<I, D extends WebDto<I, E>, E extends WebEntity<I, D>, R extends CrudRepository<E, I>>{

    protected final SecurityTokenService securityTokenService;
    protected final R repo;

    //works because is not generically autowired & intellij does not recognise that(check inheritance)
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    protected GenericAuthenticatedEntityService(SecurityTokenService securityTokenService, R repo) {
        this.securityTokenService = securityTokenService;
        this.repo = repo;
    }

    /**
     * finds all
     * @return an Iterable of all {@link E} in the database
     */
    public Iterable<E> getAll(){
        return repo.findAll();
    }

    /**
     * like {@link #getAll()} but parses result to DTOs
     */
    public List<D> getAllAsDto(){
        return StreamSupport.stream(this.getAll().spliterator(), false)
                .map(E::parseToDTO)
                .collect(Collectors.toList());
    }

    /**
     * finds by id
     * @param id the {@link I} id of the entity in search
     * @return the found {@link E}
     * @throws EntityNotFoundException when no entity with the given id was found
     */
    public E getById(I id){
        return repo.findById(id).orElseThrow(() -> new EntityNotFoundException("unknown", id));
    }

    /**
     * like {@link #getById(I)} but parses result to DTO
     */
    public D getByIdAsDto(I id){
        return getById(id).parseToDTO();
    }

    /**
     * checks if the token is valid, checks if entity id is new and saves in db
     * @param securityTokenString the token to authenticate the request (usually from request-header)
     * @param toAdd the {@link E} to save in the database
     * @throws SecurityTokenNotFoundException
     * when securityTokenString does not exist
     * @throws InvalidatedSecurityTokenException when securityTokenString is not valid
     * @throws EntityAlreadyExistsException when entity already exists in the db
     */
    public E addNew(String securityTokenString, E toAdd){
        checkToken(securityTokenString);

        if(repo.findById(toAdd.getId()).isPresent())
            throw new EntityAlreadyExistsException(toAdd.getClass().getName(), toAdd.getId());

        return repo.save(toAdd);
    }

    /**
     * like {@link #addNew(String, WebEntity)} but parses result to DTO
     */
    public D addNew(String securityTokenString, D dtoToAdd){
        return addNew(securityTokenString, dtoToAdd.parseToEntity()).parseToDTO();
    }

    /**
     * checks if the token is valid, checks that the entity id exists in db and saves the entity
     * @param securityTokenString the token to authenticate the request (usually from request-header)
     * @param toEdit the {@link E} to update in the database
     * @throws SecurityTokenNotFoundException
     * when securityTokenString does not exist
     * @throws InvalidatedSecurityTokenException when securityTokenString is not valid
     * @throws EntityNotFoundException when entity does not exist in the db
     */
    public E edit(String securityTokenString, E toEdit){
        checkToken(securityTokenString);

        if(repo.findById(toEdit.getId()).isEmpty())
            throw new EntityNotFoundException(toEdit.getClass().getName(), toEdit.getId());

        return repo.save(toEdit);
    }

    /**
     * like {@link #edit(String, WebEntity)} but parses result to DTO
     */
    public D edit(String securityTokenString, D dtoToEdit){
        return edit(securityTokenString, dtoToEdit.parseToEntity()).parseToDTO();
    }

    /**
     * checks if the token is valid, checks that the entity id exists in the db and deletes the entity
     * @param securityTokenString the token to authenticate the request (usually from request-header)
     * @param toDelete the {@link E} to delete in the database
     * @throws SecurityTokenNotFoundException
     * when securityTokenString does not exist
     * @throws InvalidatedSecurityTokenException when securityTokenString is not valid
     * @throws EntityNotFoundException when entity does not exist in the db
     */
    public void delete(String securityTokenString, E toDelete){
        checkToken(securityTokenString);

        if(repo.findById(toDelete.getId()).isEmpty())
            throw new EntityNotFoundException(toDelete.getClass().getName(), toDelete.getId());

        repo.delete(toDelete);
    }

    /**
     * like {@link #delete(String, WebEntity)} but parses result to DTO
     */
    public void delete(String securityTokenString, D dtoToDelete){
        delete(securityTokenString, dtoToDelete.parseToEntity());
    }

    /**
     * if nothing is thrown then to token is valid and authorized
     * @param securityTokenString the security token that should be checked
     * @throws SecurityTokenNotFoundException when token does not exist
     * @throws InvalidatedSecurityTokenException when token is invalidated
     */
    private void checkToken(String securityTokenString){
        if(!securityTokenService.isTokenValid(securityTokenString))
            throw new InvalidatedSecurityTokenException(securityTokenString);
    }
}
