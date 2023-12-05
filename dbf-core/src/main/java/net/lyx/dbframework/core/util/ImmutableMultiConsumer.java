package net.lyx.dbframework.core.util;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ImmutableMultiConsumer<K, H> {

    public static class Builder<K, H> {

        private final Map<K, Consumer<H>> handle = new HashMap<>();

        public Builder<K, H> register(K key, Consumer<H> consumer) {
            handle.put(key, consumer);
            return this;
        }

        public ImmutableMultiConsumer<K, H> build() {
            return new ImmutableMultiConsumer<>(handle);
        }
    }

    public static <K, T> Builder<K, T> builder() {
        return new Builder<>();
    }

    private final Map<K, Consumer<H>> map;

    public boolean execute(@NotNull K key, H helper) {
        if (!map.containsKey(key)) {
            return false;
        }

        Consumer<H> actionConsumer = map.get(key);
        boolean result = actionConsumer != null;

        if (result) {
            actionConsumer.accept(helper);
        }

        return result;
    }
}
