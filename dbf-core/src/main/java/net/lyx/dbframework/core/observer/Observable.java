package net.lyx.dbframework.core.observer;

import net.lyx.dbframework.core.ConnectionID;

public interface Observable {

    long getEventId();

    ConnectionID getConnectionID();
}
