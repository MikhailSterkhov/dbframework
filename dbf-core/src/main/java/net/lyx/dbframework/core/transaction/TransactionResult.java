package net.lyx.dbframework.core.transaction;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.sql.SQLException;

@Getter
@Builder
@ToString
public class TransactionResult {

    private final int totalQueriesCount;
    private final int proceedQueriesCount;

    private final TransactionStatus status;

    // If TransactionStatus is FAILED then exception IS NOT NULL.
    private final SQLException exception;
}
