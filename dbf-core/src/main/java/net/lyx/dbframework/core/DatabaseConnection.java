package net.lyx.dbframework.core;

import lombok.*;
import net.lyx.dbframework.core.observer.DatabaseObserver;
import net.lyx.dbframework.core.util.result.Result;
import net.lyx.dbframework.core.wrap.ResponseProvider;
import net.lyx.dbframework.core.wrap.JdbcWrapper;
import net.lyx.dbframework.core.wrap.ResultWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Builder
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class DatabaseConnection {

    private static final String[] FETCH_FUNCTIONS = {"SELECT", "SHOW", "DESCRIBE"};

    @Getter
    private final ConnectionID id;
    private final JdbcWrapper jdbcWrapper;

    public Result<ResponseStream> call(String sql) {
        if (!jdbcWrapper.isConnected()) {
            jdbcWrapper.reconnect();
        }

        Result<ResultWrapper> result = canUseFetch(sql)
                ? jdbcWrapper.executeFetch(sql) : jdbcWrapper.executeUpdate(sql);

        return result.map(this::toResponseImpl);
    }

    private boolean canUseFetch(String sql) {
        final String preparedSql = sql.trim().toUpperCase();
        return Stream.of(FETCH_FUNCTIONS)
                .anyMatch(preparedSql::startsWith);
    }

    @SneakyThrows
    private ResponseStream toResponseImpl(ResultWrapper result) {
        ResponseProvider responseProvider = new ResponseProvider(result);
        return responseProvider.getHandle();
    }

    public DatabaseConnection addObserver(@NotNull DatabaseObserver observer) {
        jdbcWrapper.addObserver(observer);
        return this;
    }

    public void openTransaction(TransactionIsolation isolation) {
        if (!jdbcWrapper.isConnected()) {
            jdbcWrapper.reconnect();
        }

        jdbcWrapper.setTransactionState(TransactionState.ACTIVE);
        jdbcWrapper.setTransactionIsolation(isolation);
    }

    public void openTransaction() {
        openTransaction(TransactionIsolation.SERIALIZABLE);
    }

    public void closeTransaction() {
        if (jdbcWrapper.isConnected()) {
            jdbcWrapper.setTransactionState(TransactionState.INACTIVE);
        }
    }

    public synchronized <T> T ofTransactionalGet(TransactionIsolation isolation, Supplier<T> transactionSupplier) {
        openTransaction(isolation);
        T value = transactionSupplier.get();
        closeTransaction();
        return value;
    }

    public synchronized <T> T ofTransactionalGet(Supplier<T> transactionSupplier) {
        return ofTransactionalGet(TransactionIsolation.SERIALIZABLE, transactionSupplier);
    }

    public synchronized void close() {
        jdbcWrapper.close();
    }
}
