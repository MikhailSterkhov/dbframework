package net.lyx.dbframework.core;

import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface ResponseStream extends Iterable<ResponseRow> {

    <R> Stream<R> map(Function<ResponseRow, R> function);

    @Nullable
    ResponseRow findFirst();

    @Nullable
    ResponseRow findLast();

    @Nullable
    ResponseRow find(int index);

    ResponseStream limit(long limit);

    ResponseStream filter(Predicate<ResponseRow> predicate);

    boolean anyMatch(Predicate<ResponseRow> predicate);

    boolean allMatch(Predicate<ResponseRow> predicate);

    boolean noneMatch(Predicate<ResponseRow> predicate);

    long count();

    void forEach(Consumer<? super ResponseRow> consumer);
}
