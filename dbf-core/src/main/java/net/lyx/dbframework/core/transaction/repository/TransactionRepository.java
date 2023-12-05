package net.lyx.dbframework.core.transaction.repository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface TransactionRepository {

    void prepareTransactionData(@NotNull TransactionData transactionData);

    List<TransactionRequest> combineRequests(@NotNull TransactionData transactionData);
}
