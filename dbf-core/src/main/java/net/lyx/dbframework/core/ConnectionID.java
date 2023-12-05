package net.lyx.dbframework.core;

import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode
@Builder(setterPrefix = "set")
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ConnectionID {

    private final UUID uniqueId;
    private final Timestamp openedTimestamp;
}
