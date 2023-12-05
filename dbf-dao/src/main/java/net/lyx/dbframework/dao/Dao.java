package net.lyx.dbframework.dao;

import net.lyx.dbframework.dao.repository.Repository;

public interface Dao<T> {

    Repository<T> repository();

    void prepare(DataAccessContext<T> context);

    void writeAccess(DataAccessContext<T> context);
}
