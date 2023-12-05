package net.lyx.dbframework.core.compose.template.completed;

import net.lyx.dbframework.core.DatabaseConnection;
import net.lyx.dbframework.core.ResponseStream;
import net.lyx.dbframework.core.util.result.Result;

public interface CompletedQuery {

    //todo - Result<ResponseStream> callTransactional(DatabaseConnection connection);

    Result<ResponseStream> call(DatabaseConnection connection);

    Result<String> toNativeQuery();
}
