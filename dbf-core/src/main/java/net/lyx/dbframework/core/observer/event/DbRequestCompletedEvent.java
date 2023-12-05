package net.lyx.dbframework.core.observer.event;

import lombok.Getter;
import lombok.val;
import net.lyx.dbframework.core.ConnectionID;
import net.lyx.dbframework.core.util.result.Result;
import net.lyx.dbframework.core.wrap.ResultWrapper;

@Getter
public class DbRequestCompletedEvent extends DbRequestPreprocessEvent {

    private static final String TO_STRING_FORMAT = "DbRequestCompletedEvent(eventId=%s, connection=\"%s\", sql=\"%s\")";

    private final Result<ResultWrapper> result;

    public DbRequestCompletedEvent(long eventId, ConnectionID connectionID, String sql, Result<ResultWrapper> result) {
        super(eventId, connectionID, sql);

        this.result = result;
    }

    @Override
    public String toString() {
        val eventId = getEventId();
        val connectionID = getConnectionID().getUniqueId();
        val sql = getSql();

        return String.format(TO_STRING_FORMAT, eventId, connectionID, sql);
    }
}
