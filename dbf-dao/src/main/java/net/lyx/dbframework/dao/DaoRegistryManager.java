package net.lyx.dbframework.dao;

import lombok.var;
import net.lyx.dbframework.dao.repository.Repository;

import java.util.HashMap;
import java.util.Map;

public final class DaoRegistryManager {

    private final Map<Class<?>, Repository<?>> registries = new HashMap<>();

    private <T> void prepareRegistration(Dao<T> dao) {
        dao.prepare(new DataAccessContext<>());
    }

    public <T> Repository<T> register(Dao<T> dao) {
        var daoClass = dao.getClass();
        if (registries.containsKey(daoClass)) {
            throw new DataAccessException("dao already registered");
        }

        prepareRegistration(dao);
        registries.put(daoClass, dao.repository());

        return dao.repository();
    }

    @SuppressWarnings("unchecked")
    public <T> Repository<T> get(Class<? super Dao<T>> daoClass) {
        return (Repository<T>) registries.get(daoClass);
    }
}
