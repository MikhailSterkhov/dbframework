package net.lyx.dbframework.core.transaction;

import net.lyx.dbframework.core.transaction.impl.DatabaseTransaction;
import net.lyx.dbframework.core.transaction.repository.TransactionRepository;

public final class TransactionFactory {

    public Transaction createTransaction() {
        return new DatabaseTransaction();
    }

    public PreparedTransaction createPreparedTransaction(TransactionRepository repository) {
        return new DatabaseTransaction();
    }
}
