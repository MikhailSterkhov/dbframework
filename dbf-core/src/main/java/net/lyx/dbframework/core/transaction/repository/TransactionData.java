package net.lyx.dbframework.core.transaction.repository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.lyx.dbframework.core.transaction.FailedTransactionPreprocessConsumer;
import net.lyx.dbframework.core.transaction.TransactionIsolation;

import java.util.function.Supplier;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class TransactionData {

    private FailedTransactionPreprocessConsumer onFailedPreprocessor;
    private TransactionIsolation isolation;

    private final Supplier<TransactionRequestContext> contextSupplier;

    public TransactionRequestContext createRequestContext() {
        return contextSupplier.get();
    }
}
