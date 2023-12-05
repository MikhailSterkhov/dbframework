package net.lyx.dbframework.provider;

import lombok.Getter;
import net.lyx.dbframework.core.ConnectionID;
import net.lyx.dbframework.core.DatabaseConnection;
import net.lyx.dbframework.core.compose.Composer;
import net.lyx.dbframework.core.compose.impl.PatternComposerImpl;
import net.lyx.dbframework.core.transaction.PreparedTransaction;
import net.lyx.dbframework.core.transaction.Transaction;
import net.lyx.dbframework.core.transaction.TransactionFactory;
import net.lyx.dbframework.core.transaction.TransactionQuery;
import net.lyx.dbframework.core.transaction.impl.DatabaseTransactionQuery;
import net.lyx.dbframework.core.transaction.repository.TransactionRepository;
import net.lyx.dbframework.core.wrap.JdbcWrapper;
import net.lyx.dbframework.core.security.Credentials;
import net.lyx.dbframework.dao.Dao;
import net.lyx.dbframework.dao.DaoRegistryManager;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

public final class DatabaseProvider {

    private final Set<DatabaseConnection> activeConnections = new CopyOnWriteArraySet<>();

    @Getter
    private final Composer composer = new PatternComposerImpl();

    private final TransactionFactory transactionFactory = new TransactionFactory();
    private final DaoRegistryManager daoRegistryManager = new DaoRegistryManager();

    private DatabaseConnection createDatabaseConnection(Credentials credentials) {
        ConnectionID connectionID = ConnectionID.builder()
                .setUniqueId(UUID.randomUUID())
                .setOpenedTimestamp(new Timestamp(System.currentTimeMillis()))
                .build();
        return DatabaseConnection.builder()
                .id(connectionID)
                .jdbcWrapper(JdbcWrapper.builder()
                        .connectionID(connectionID)
                        .exceptionHandler((t, e) -> e.printStackTrace())
                        .credentials(credentials)
                        .build())
                .build();
    }

    public synchronized DatabaseConnection openConnection(@NotNull Credentials credentials) {
        DatabaseConnection databaseConnection = createDatabaseConnection(credentials);
        activeConnections.add(databaseConnection);

        return databaseConnection;
    }

    public PreparedTransaction prepareTransaction(TransactionRepository repository) {
        return transactionFactory.createPreparedTransaction(repository);
    }

    public synchronized Transaction openTransaction() {
        return transactionFactory.createTransaction();
    }

    public synchronized TransactionQuery openTransactionQuery() {
        return new DatabaseTransactionQuery();
    }

    public synchronized Collection<DatabaseConnection> getActiveConnections() {
        return Collections.unmodifiableCollection(activeConnections);
    }

    public synchronized <T> Dao<T> registerDao(@NotNull Dao<T> dao) {
        daoRegistryManager.register(dao);
        return dao;
    }

    public synchronized <T> Dao<T> getDao(@NotNull Class<? extends Dao<T>> daoClass) {
        return daoRegistryManager.get(daoClass);
    }
}
