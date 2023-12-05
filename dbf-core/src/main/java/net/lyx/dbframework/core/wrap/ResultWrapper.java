package net.lyx.dbframework.core.wrap;

import lombok.*;

import java.sql.ResultSet;
import java.sql.Statement;

@Getter
@ToString
@Builder(toBuilder = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ResultWrapper {

    private final long affectedRows;

    private final Statement statement;
    private final ResultSet result;
}