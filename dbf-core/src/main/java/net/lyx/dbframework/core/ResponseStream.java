package net.lyx.dbframework.core;

import net.lyx.dbframework.core.util.result.Result;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface ResponseStream {

    <R> Stream<R> map(Function<ResponseRow, R> function);

    Result<ResponseRow> findFirst();

    Result<ResponseRow> findLast();

    Result<ResponseRow> find(int index);

    ResponseStream limit(long limit);

    ResponseStream filter(Predicate<ResponseRow> predicate);

    boolean anyMatch(Predicate<ResponseRow> predicate);

    boolean allMatch(Predicate<ResponseRow> predicate);

    boolean noneMatch(Predicate<ResponseRow> predicate);

    long count();

    void forEach(Consumer<ResponseRow> consumer);
}
