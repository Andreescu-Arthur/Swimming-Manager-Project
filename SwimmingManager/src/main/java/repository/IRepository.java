package repository;

import java.util.Collection;
import java.util.List;

public interface IRepository<ID, T extends HasId<ID>> {    // here it was   T extends HasId<T> but i got an error for that
    int size();

    void save(T entity);

    void delete(ID id);

    void update(ID id, T entity);

    T findOne(ID id);

    Collection<T> findAll();
}
