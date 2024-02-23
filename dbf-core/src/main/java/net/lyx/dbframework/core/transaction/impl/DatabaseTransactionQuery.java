package net.lyx.dbframework.core.transaction.impl;

import net.lyx.dbframework.core.compose.template.completed.CompletedQuery;
import net.lyx.dbframework.core.TransactionIsolation;
import net.lyx.dbframework.core.transaction.TransactionQuery;
import org.jetbrains.annotations.NotNull;

public class DatabaseTransactionQuery implements TransactionQuery {

    @Override
    public TransactionQuery marksSavepoint() {
        return null;
    }

    @Override
    public TransactionQuery intermediateIsolation(@NotNull TransactionIsolation isolation) {
        return null;
    }

    @Override
    public TransactionQuery writeSql(@NotNull String sql) {
        return null;
    }

    @Override
    public TransactionQuery write(@NotNull CompletedQuery query) {
        return null;
    }
}
