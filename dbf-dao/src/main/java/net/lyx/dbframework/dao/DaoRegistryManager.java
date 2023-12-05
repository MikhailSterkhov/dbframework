package net.lyx.dbframework.dao;

import lombok.var;

import java.util.HashMap;
import java.util.Map;

public final class DaoRegistryManager {

    private final Map<Class<?>, Dao<?>> registries = new HashMap<>();

    private <T> void prepareRegistration(Dao<T> dao) {
        var accessContext = new DataAccessContext<T>();

        dao.prepare(accessContext);
        dao.writeAccess(accessContext);
    }

    public void register(Dao<?> dao) {
        var daoClass = dao.getClass();
        if (registries.containsKey(daoClass)) {
            throw new DataAccessException("dao already registered");
        }

        prepareRegistration(dao);

        registries.put(daoClass, dao);
    }

    @SuppressWarnings("unchecked")
    public <T> Dao<T> get(Class<? extends Dao<T>> daoClass) {
        return (Dao<T>) registries.get(daoClass);
    }
}
