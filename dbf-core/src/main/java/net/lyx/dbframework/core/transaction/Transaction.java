package net.lyx.dbframework.core.transaction;

import net.lyx.dbframework.core.Combinable;
import org.jetbrains.annotations.NotNull;

public interface Transaction extends Combinable<PreparedTransaction> {

    Transaction isolation(@NotNull TransactionIsolation isolation);

    Transaction onFailed(@NotNull FailedTransactionPreprocessConsumer failed);

    Transaction query(@NotNull TransactionQuery query);
}
