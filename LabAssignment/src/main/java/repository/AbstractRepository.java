package repository;

import java.util.HashMap;
import java.util.Map;


public abstract class AbstractRepository<ID, T extends HasId<ID>> implements IRepository<ID, T> {
    protected Map<ID, T> entities;
    protected Validator<T> validator;

    public AbstractRepository(Validator<T> valid) {
        entities = new HashMap<>();
        validator = valid;
    }

    @Override
    public void update(ID id, T entity) {
        try {
            validator.validate(entity);
        } catch (ValidationException ex) {
            System.err.println("The entity is not valid: " + entity);
            throw ex;
        }

        if (!(entities.get(id) == null)) {
            if (!id.equals(entity.getId()))
                if (entities.get(entity.getId()) != null)
                    throw new RepositoryException("Id " + entity.getId() + " already exists!!");
            entities.put((ID) entity.getId(), entity);
            System.out.println("Modified entity " + entity);
        } else
            throw new RepositoryException("Id " + id + " does not exists");
    }

    @Override
    public void save(T entity) {
        try {
            validator.validate(entity);
        } catch (ValidationException ex) {
            System.err.println("The entity is not valid: " + entity);
            throw ex;
        }

        // setEntityId(entity);
        ID id = (ID) entity.getId();
        if (entities.get(id) == null) {
            entities.put(id, entity);
        } else
            throw new RepositoryException("Id already exists" + id);

    }

    @Override
    public void delete(ID id) {
        entities.remove(id);
        System.out.println("Entity deleted " + id);
    }


    @Override
    public T findOne(ID id) {
        T res = entities.get(id);
        if (res != null)
            return res;
        throw new RepositoryException("Id not found " + id);
    }

    @Override
    public Iterable<T> findAll() {
        return entities.values();
    }

}
