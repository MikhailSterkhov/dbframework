package net.lyx.dbframework.dao;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.lyx.dbframework.core.compose.CombinedStructs;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@ToString
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class EntityAccessCondition {

    public static EntityAccessCondition createMono(@NotNull String label, @Nullable Object value) {
        return new EntityAccessCondition(
                Collections.singletonList(CombinedStructs.field(label, value)));
    }

    @SafeVarargs
    public static <T> EntityAccessCondition createMany(@NotNull String label, @Nullable T... values) {
        return new EntityAccessCondition(Stream.of(values)
                .map(value -> CombinedStructs.field(label, value))
                .collect(Collectors.toList()));
    }

    public static EntityAccessCondition create() {
        return new EntityAccessCondition(new ArrayList<>());
    }

    private final List<CombinedStructs.CombinedField> fields;

    public EntityAccessCondition withMono(@NotNull String label, @Nullable Object value) {
        fields.add(CombinedStructs.field(label, value));
        return this;
    }

    public EntityAccessCondition withMany(@NotNull String label, @Nullable Object... values) {
        fields.addAll(Stream.of(values)
                .map(value -> CombinedStructs.field(label, value))
                .collect(Collectors.toList()));
        return this;
    }
}
