package net.lyx.dbframework.core.observer;

import net.lyx.dbframework.core.observer.event.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public abstract class ObserverAdapter implements DatabaseObserver {

    private static final int EVENT_HANDLER_SIGNATURE_SIZE = 1;

    private final Map<Class<?>, Method> eventAdaptersMap = new HashMap<>();

// ========================= // OVERRIDE THAT`S // ========================= //

    protected void observe(DbRequestCompletedEvent event) {}

    protected void observe(DbConnectEvent event) {}

    protected void observe(DbClosedEvent event) {}

    protected void observe(DbRequestPreprocessEvent event) {}

    protected void observe(DbReconnectPreprocessEvent event) {}

// =========================================================================== //

    private Method getEventAdapter(Class<?> eventType) {
        return Stream.of(getClass().getDeclaredMethods())
                .filter(method -> method.getParameterCount() == EVENT_HANDLER_SIGNATURE_SIZE)
                .filter(method -> method.getParameterTypes()[EVENT_HANDLER_SIGNATURE_SIZE - 1] == eventType)
                .findFirst()
                .orElse(null);
    }

    @Override
    public final void observe(Observable event) {
        final Class<? extends Observable> eventType = event.getClass();

        final Method method = eventAdaptersMap.computeIfAbsent(
                eventType,
                f -> getEventAdapter(eventType));

        try {
            if (method != null) {
                method.setAccessible(true);
                method.invoke(this, event);
            }
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
