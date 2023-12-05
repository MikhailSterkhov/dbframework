package net.lyx.dbframework.dao.repository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.lyx.dbframework.dao.make.DaoRequestMaker;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DaoRepository<T> implements Repository<T> {

    @Getter
    private final DaoRepositoryLifecycle<T> lifecycle;

    @Override
    public void deleteMono(T object) {
        DaoRequestMaker.makeDelete(this, object);
    }

    @Override
    public void insertMono(T object) {
        DaoRequestMaker.makeInsert(this, object);
    }

    @Override
    public void updateMono(T object) {
    }

    @Override
    public T findFirst() {
        return DaoRequestMaker.makeSearch(1, this)
                .stream()
                .findFirst().orElse(null);
    }

    @Override
    public T findFirst(Predicate<T> predicate) {
        return findAll()
                .stream()
                .filter(predicate).findFirst().orElse(null);
    }

    @Override
    public T findLast() {
        LinkedList<T> result = new LinkedList<>(findAll());
        return result.getLast(); // todo - оптимизировать
    }

    @Override
    public T findLast(Comparator<T> comparator) {
        return findAll()
                .stream()
                .sorted(comparator).collect(Collectors.toCollection(LinkedList::new))
                .getLast();
    }

    @Override
    public List<T> findAll() {
        return DaoRequestMaker.makeSearch(0, this);
    }

    @Override
    public List<T> findBy(Predicate<T> predicate) {
        return findAll().stream().filter(predicate).collect(Collectors.toList());
    }
}
