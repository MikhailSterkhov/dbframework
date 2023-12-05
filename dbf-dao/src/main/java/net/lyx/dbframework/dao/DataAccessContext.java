package net.lyx.dbframework.dao;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.var;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class DataAccessContext<E> {

    @Getter
    @Builder
    private static class LabelAccessor<E, T> {

        private final BiConsumer<E, T> setter;
        private final Function<E, T> getter;

        private final String label;

        private final boolean isNullable;
        private final boolean isPrimary;
        private final boolean isAutoFilled;
    }

    @Setter
    @Getter
    private String containerName;

    @Setter
    private Map<String, LabelAccessor<?, ?>> labelAccessorsCache;

    public final boolean isEmpty() {
        return labelAccessorsCache == null || labelAccessorsCache.isEmpty();
    }

    private String[] getLabelsArray(Predicate<LabelAccessor<?, ?>> filter) {
        if (labelAccessorsCache == null) {
            return new String[0];
        }
        return labelAccessorsCache.values()
                .stream()
                .filter(filter)
                .map(LabelAccessor::getLabel)
                .toArray(String[]::new);
    }

    public final String[] labels() {
        return getLabelsArray(a -> true);
    }

    public final String[] keys() {
        return getLabelsArray(LabelAccessor::isPrimary);
    }

    public final String[] autofilled() {
        return getLabelsArray(LabelAccessor::isAutoFilled);
    }

    @SuppressWarnings("unchecked")
    public <T> void update(@NotNull E object, @NotNull String name, @Nullable T value) {
        var labelAccessor = (LabelAccessor<E, T>) labelAccessorsCache.get(name.toLowerCase());
        if (labelAccessor == null) {
            throw new DataAccessException("label '" + name + "' is not registered");
        }

        var setter = labelAccessor.getSetter();
        if (setter != null) {
            setter.accept(object, value);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T read(@NotNull E object, @NotNull String name) {
        var labelAccessor = (LabelAccessor<E, T>) labelAccessorsCache.get(name.toLowerCase());
        if (labelAccessor == null) {
            throw new DataAccessException("label '" + name + "' is not registered");
        }

        var getter = labelAccessor.getGetter();
        if (getter == null) {
            throw new DataAccessException("getter of '" + name + "' is not found");
        }

        return getter.apply(object);
    }

    public <T> void write(@NotNull String name, @NotNull Function<E, T> getter, @Nullable BiConsumer<E, T> setter) {
        var accessor = LabelAccessor.<E, T>builder()
                .label(name)
                .getter(getter)
                .setter(setter)
                .isNullable(true)
                .build();

        labelAccessorsCache.put(name.toLowerCase(), accessor);
    }

    public <T> void writeFinal(@NotNull String name, @Nullable Function<E, T> getter) {
        var accessor = LabelAccessor.<E, T>builder()
                .label(name)
                .getter(getter)
                .build();

        labelAccessorsCache.put(name.toLowerCase(), accessor);
    }

    public <T> void writeKey(@NotNull String name, @NotNull Function<E, T> getter, @Nullable BiConsumer<E, T> setter) {
        var accessor = LabelAccessor.<E, T>builder()
                .label(name)
                .getter(getter)
                .setter(setter)
                .isPrimary(true)
                .build();

        labelAccessorsCache.put(name.toLowerCase(), accessor);
    }

    public <T> void writeAutofill(@NotNull String name, @NotNull Function<E, T> getter, @Nullable BiConsumer<E, T> setter) {
        var accessor = LabelAccessor.<E, T>builder()
                .label(name)
                .getter(getter)
                .setter(setter)
                .isPrimary(true)
                .isAutoFilled(true)
                .build();

        labelAccessorsCache.put(name.toLowerCase(), accessor);
    }
}
