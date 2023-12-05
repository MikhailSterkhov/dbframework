package net.lyx.dbframework.core.transaction;

import net.lyx.dbframework.core.Combinable;
import net.lyx.dbframework.core.compose.template.completed.CompletedQuery;
import org.jetbrains.annotations.NotNull;

public interface TransactionQuery {

    TransactionQuery marksSavepoint();

    TransactionQuery intermediateIsolation(@NotNull TransactionIsolation isolation);

    TransactionQuery writeSql(@NotNull String sql);

    TransactionQuery write(@NotNull CompletedQuery query);
}
