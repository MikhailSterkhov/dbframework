package net.lyx.dbframework.core.util;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;

@ToString(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ObjectPropertyContainer {

    public enum DefaultKeyModifiers {
        AS_LOWER_CASE, AS_UPPER_CASE,
    }

    private static final ImmutableMultiConsumer<DefaultKeyModifiers, ObjectPropertyContainer> DEFAULT_KEYS_MODIFIERS
            = ImmutableMultiConsumer.<DefaultKeyModifiers, ObjectPropertyContainer>builder()
                .register(DefaultKeyModifiers.AS_LOWER_CASE, objectPropertyContainer -> objectPropertyContainer.keyModifier = (String::toLowerCase))
                .register(DefaultKeyModifiers.AS_UPPER_CASE, objectPropertyContainer -> objectPropertyContainer.keyModifier = (String::toUpperCase))
                .build();

    public static ObjectPropertyContainer newHashContainer() {
        return new ObjectPropertyContainer(new HashMap<>());
    }

    public static ObjectPropertyContainer newConcurrentContainer() {
        return new ObjectPropertyContainer(new ConcurrentHashMap<>());
    }

    public static ObjectPropertyContainer newWeakContainer() {
        return new ObjectPropertyContainer(new WeakHashMap<>());
    }

    @ToString.Include
    private final Map<String, Object> data;

    @Setter
    private Function<String, String> keyModifier;

    public ObjectPropertyContainer setDefaultKeyModifier(DefaultKeyModifiers type) {
        DEFAULT_KEYS_MODIFIERS.execute(type, this);
        return this;
    }

    private String getModifiedKey(String originKey) {
        if (keyModifier != null) {
            return keyModifier.apply(originKey);
        }

        return originKey;
    }

    public ObjectPropertyContainer setProperty(String key, Object value) {
        data.put(getModifiedKey(key), value);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProperty(String key) {
        return (T) data.get(getModifiedKey(key));
    }

    public void foreach(BiConsumer<String, Object> foreachConsumer) {
        if (foreachConsumer != null) {
            data.forEach(foreachConsumer);
        }
    }
}
