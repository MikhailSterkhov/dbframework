package net.lyx.dbframework.core.util;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ImmutableMultiConsumer<K, T> {

    public static class Builder<K, T> {

        private final Map<K, Consumer<T>> handle = new HashMap<>();

        public Builder<K, T> register(K key, Consumer<T> consumer) {
            handle.put(key, consumer);
            return this;
        }

        public ImmutableMultiConsumer<K, T> build() {
            return new ImmutableMultiConsumer<>(handle);
        }
    }

    public static <K, T> Builder<K, T> builder() {
        return new Builder<>();
    }

    private final Map<K, Consumer<T>> map;

    public boolean execute(@NotNull K key, T helper) {
        if (!map.containsKey(key)) {
            return false;
        }

        Consumer<T> actionConsumer = map.get(key);
        boolean result = actionConsumer != null;

        if (result) {
            actionConsumer.accept(helper);
        }

        return result;
    }
}
