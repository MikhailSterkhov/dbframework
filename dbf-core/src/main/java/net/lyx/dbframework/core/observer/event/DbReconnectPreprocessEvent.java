package net.lyx.dbframework.core.observer.event;

import lombok.val;
import net.lyx.dbframework.core.ConnectionID;
import net.lyx.dbframework.core.observer.AbstractObservable;

public class DbReconnectPreprocessEvent extends AbstractObservable {

    private static final String TO_STRING_FORMAT = "DbReconnectPreprocessEvent(eventId=%s, connection=\"%s\")";

    public DbReconnectPreprocessEvent(long eventId, ConnectionID connectionID) {
        super(eventId, connectionID);
    }

    @Override
    public String toString() {
        val eventId = getEventId();
        val connectionID = getConnectionID().getUniqueId();

        return String.format(TO_STRING_FORMAT, eventId, connectionID);
    }
}
