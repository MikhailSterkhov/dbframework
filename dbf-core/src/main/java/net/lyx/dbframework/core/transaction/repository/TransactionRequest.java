package net.lyx.dbframework.core.transaction.repository;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import net.lyx.dbframework.core.compose.template.completed.CompletedQuery;
import net.lyx.dbframework.core.TransactionIsolation;

@Getter
@Builder
@ToString
public class TransactionRequest {

    private TransactionIsolation intermediateIsolation;
    private TransactionResponseHandler responseHandler;

    private CompletedQuery target;
}
