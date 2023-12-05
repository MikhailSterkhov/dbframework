package net.lyx.dbframework.core.transaction.impl;

import net.lyx.dbframework.core.DatabaseConnection;
import net.lyx.dbframework.core.transaction.*;
import org.jetbrains.annotations.NotNull;

public class DatabaseTransaction implements Transaction, PreparedTransaction {

    @Override
    public PreparedTransaction combine() {
        return this;
    }

    @Override
    public Transaction isolation(@NotNull TransactionIsolation isolation) {
        return null;
    }

    @Override
    public Transaction onFailed(@NotNull FailedTransactionPreprocessConsumer failed) {
        return null;
    }

    @Override
    public Transaction query(@NotNull TransactionQuery query) {
        return null;
    }

    @Override
    public TransactionResult call(@NotNull DatabaseConnection connection) {
        return null;
    }
}
