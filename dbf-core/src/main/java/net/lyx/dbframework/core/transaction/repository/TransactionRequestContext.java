package net.lyx.dbframework.core.transaction.repository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.lyx.dbframework.core.DatabaseConnection;
import net.lyx.dbframework.core.compose.Composer;

@Getter
@ToString
@RequiredArgsConstructor
public class TransactionRequestContext {

    private final Composer composer;
    private final DatabaseConnection activeConnection;
}
