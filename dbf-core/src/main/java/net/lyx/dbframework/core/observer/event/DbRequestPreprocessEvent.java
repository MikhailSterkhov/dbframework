package net.lyx.dbframework.core.observer.event;

import lombok.Getter;
import lombok.val;
import net.lyx.dbframework.core.ConnectionID;
import net.lyx.dbframework.core.observer.AbstractObservable;

@Getter
public class DbRequestPreprocessEvent extends AbstractObservable {

    private static final String TO_STRING_FORMAT = "DbRequestPreprocessEvent(eventId=%s, connection=\"%s\", sql=\"%s\")";

    private final String sql;

    public DbRequestPreprocessEvent(long eventId, ConnectionID connectionID, String sql) {
        super(eventId, connectionID);
        this.sql = sql;
    }

    @Override
    public String toString() {
        val eventId = getEventId();
        val connectionID = getConnectionID().getUniqueId();

        return String.format(TO_STRING_FORMAT, eventId, connectionID, sql);
    }
}
