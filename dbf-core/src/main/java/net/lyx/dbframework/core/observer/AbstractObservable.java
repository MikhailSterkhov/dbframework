package net.lyx.dbframework.core.observer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.lyx.dbframework.core.ConnectionID;

@Getter
@RequiredArgsConstructor
public class AbstractObservable implements Observable {

    private final long eventId;

    private final ConnectionID connectionID;
}
