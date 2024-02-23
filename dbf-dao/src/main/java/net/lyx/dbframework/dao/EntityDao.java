package net.lyx.dbframework.dao;

import java.util.List;
import java.util.Optional;

public interface EntityDao<E> {

    void deleteMonoById(Long id);

    void deleteMono(E entity);

    void deleteManyById(Long... ids);

    void deleteMany(E... entities);

    void delete(EntityAccessCondition condition);

    Optional<Long> insertMono(E entity);

    List<Long> insertMany(E... entities);

    Optional<E> findMonoById(Long id);

    Optional<E> findMono(EntityAccessCondition condition);

    List<E> findManyById(Long... ids);

    List<E> findMany(EntityAccessCondition condition);

    List<E> findManyWithLimit(int limit, EntityAccessCondition condition);

    List<E> findWithLimit(int limit);

    List<E> findAll();
}
