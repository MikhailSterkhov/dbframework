package net.lyx.dbframework.dao.repository;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public interface Repository<T> {

    void deleteMono(T object);

    void insertMono(T object);

    void updateMono(T object);

    T findFirst();

    T findFirst(Predicate<T> predicate);

    T findLast();

    T findLast(Comparator<T> comparator);

    List<T> findAll();

    List<T> findBy(Predicate<T> predicate);
}
