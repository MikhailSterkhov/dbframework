package net.lyx.dbframework.test.core;

import net.lyx.dbframework.core.observer.DatabaseObserver;
import net.lyx.dbframework.core.observer.Observable;

public class AnyEventPrintingObserverTest implements DatabaseObserver {

    @Override
    public void observe(Observable event) {
        System.out.println(event);
    }
}
