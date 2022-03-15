package ch.bbbaden.m151.wheeloffortune.game.data;

import ch.bbbaden.m151.wheeloffortune.auth.token.SecurityTokenService;
import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.entity.EntityAlreadyExistsException;
import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.entity.EntityNotFoundException;
import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.auth.InvalidatedSecurityTokenException;
import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.auth.SecurityTokenNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

/**
 * This is a Generic Service that implements basic error-handling and authentication for the crud operations.
 * <br><strong>read operations are NOT authenticated!!</strong>
 * @param <I> the id of the entity of the service
 * @param <E> the entity of the service (must extend {@link WebEntity})
 * @param <R> the repository to access the entity in the database (must extend {@link CrudRepository})
 */
@Service
public abstract class GenericAuthenticatedEntityService<I, E extends CrudEntity<I>, R extends CrudRepository<E, I>>{
    public static final Logger LOGGER = LoggerFactory.getLogger(GenericAuthenticatedEntityService.class);
    private static final String REPO_NOT_INITIALIZED_MESSAGE = "repo has no been initialized";

    protected final SecurityTokenService securityTokenService;
    protected final R repo;

    protected GenericAuthenticatedEntityService(SecurityTokenService securityTokenService, R repo) {
        this.securityTokenService = securityTokenService;
        this.repo = repo;
    }

    /**
     * finds all
     * @return an Iterable of all {@link E} in the database
     * @throws IllegalStateException when repo has not been initialized
     */
    public Iterable<E> getAll(){
        if(repo == null)
            throw new IllegalStateException(REPO_NOT_INITIALIZED_MESSAGE);

        return repo.findAll();
    }

    /**
     * finds by id
     * @param id the {@link I} id of the entity in search
     * @return the found {@link E}
     * @throws EntityNotFoundException when no entity with the given id was found
     * @throws IllegalStateException when repo has not been initialized
     */
    public E getById(I id){
        if(repo == null)
            throw new IllegalStateException(REPO_NOT_INITIALIZED_MESSAGE);

        return repo.findById(id).orElseThrow(() -> new EntityNotFoundException("unknown", id.toString()));
    }

    /**
     * checks if the token is valid, checks if entity id is new and saves in db
     * @param securityTokenString the token to authenticate the request (usually from request-header)
     * @param toAdd the {@link E} to save in the database
     * @throws SecurityTokenNotFoundException
     * when securityTokenString does not exist
     * @throws InvalidatedSecurityTokenException when securityTokenString is not valid
     * @throws EntityAlreadyExistsException when entity already exists in the db
     * @throws IllegalStateException when repo has not been initialized
     */
    public void addNew(String securityTokenString, E toAdd){
        checkRepoAndAuth(securityTokenString);

        if(repo.findById(toAdd.getId()).isPresent())
            throw new EntityAlreadyExistsException(toAdd.getClass().getName(), toAdd.getId().toString());

        repo.save(toAdd);
    }


    /**
     * checks if the token is valid, checks that the entity id exists in db and saves the entity
     * @param securityTokenString the token to authenticate the request (usually from request-header)
     * @param toEdit the {@link E} to update in the database
     * @throws SecurityTokenNotFoundException
     * when securityTokenString does not exist
     * @throws InvalidatedSecurityTokenException when securityTokenString is not valid
     * @throws EntityNotFoundException when entity does not exist in the db
     * @throws IllegalStateException when repo has not been initialized
     */
    public void edit(String securityTokenString, E toEdit){
        checkRepoAndAuth(securityTokenString);

        if(repo.findById(toEdit.getId()).isEmpty())
            throw new EntityNotFoundException(toEdit.getClass().getName(), toEdit.getId().toString());

        repo.save(toEdit);
    }

    /**
     * checks if the token is valid, checks that the entity id exists in the db and deletes the entity
     * @param securityTokenString the token to authenticate the request (usually from request-header)
     * @param toDelete the {@link E} to delete in the database
     * @throws SecurityTokenNotFoundException
     * when securityTokenString does not exist
     * @throws InvalidatedSecurityTokenException when securityTokenString is not valid
     * @throws EntityNotFoundException when entity does not exist in the db
     * @throws IllegalStateException when repo has not been initialized
     */
    public void delete(String securityTokenString, E toDelete){
        checkRepoAndAuth(securityTokenString);

        if(repo.findById(toDelete.getId()).isEmpty())
            throw new EntityNotFoundException(toDelete.getClass().getName(), toDelete.getId().toString());

        repo.delete(toDelete);
    }

    private void checkRepoAndAuth(String securityTokenString){
        if(repo == null)
            throw new IllegalStateException(REPO_NOT_INITIALIZED_MESSAGE);

        if(!securityTokenService.isTokenValid(securityTokenString))
            throw new InvalidatedSecurityTokenException(securityTokenString);
    }
}
