package net.lyx.dbframework.dao;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.var;
import net.lyx.dbframework.core.DatabaseConnection;
import net.lyx.dbframework.core.compose.template.completed.CompletedQuery;

import java.util.NoSuchElementException;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public final class DaoRequestCaller {

    public Long insertEntityWithId(DatabaseConnection connection, CompletedQuery preparedQuery) {
        var result = preparedQuery.call(connection);
        try {
            var firstRow = result.get().findFirst();

            if (firstRow != null) {
                return firstRow.field(0).getAsLong();
            }
        } catch (NoSuchElementException ignored) {
        }
        return 0L;
    }
}
