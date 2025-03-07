package repository;

public interface repository<T> {
    void add(T t);
    void delete(T t);
    void update(T t);
}
