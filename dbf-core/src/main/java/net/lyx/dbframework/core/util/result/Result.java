package net.lyx.dbframework.core.util.result;

import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Result<T> {

    private static <T> Result<T> createInternal(T val, boolean exclusive) {
        return new Result<>(val, (val != null ? ResultState.COMPLETED : ResultState.IDLE), exclusive, null);
    }

    public static <T> Result<T> of(T val) {
        return createInternal(val, false);
    }

    public static <T> Result<T> ofExclusive(T val) {
        return createInternal(val, true);
    }

    public static <T> Result<T> ofEmpty() {
        return of(null);
    }

    @ToString.Include
    @EqualsAndHashCode.Include
    private volatile T val;

    @EqualsAndHashCode.Include
    private ResultState state;

    @Getter
    private boolean exclusive;

    private Consumer<T> transitionConsumer;
    public synchronized T get() {
        return val;
    }

    public synchronized Result<T> beginIntent(Supplier<T> startVal) {
        checkModifiable();

        this.state = ResultState.TRANSITION;
        if (startVal != null) {
            this.val = startVal.get();
        }

        return this;
    }

    public synchronized Result<T> beginIntent() {
        return beginIntent(null);
    }

    public synchronized Result<T> completeIntent(Supplier<T> completedVal) {
        checkModifiable();

        if (state == ResultState.TRANSITION && isExclusive() && val != null) {
            throw new IllegalArgumentException("can not complete exclusive result");
        }

        this.val = completedVal.get();
        postComplete();
        return this;
    }

    public T orElseGet(Supplier<T> newVal) {
        if (val == null) {
            val = newVal.get();
        }

        return val;
    }

    public T orElse(T newVal) {
        return orElseGet(() -> newVal);
    }

    public <R> Result<R> map(Function<T, R> function) {
        Result<R> result;
        if (val == null) {
            result = ofEmpty();
        }
        else {
            R mappedInstance = function.apply(val);
            result = of(mappedInstance);
        }

        result.state = state;
        result.exclusive = exclusive;

        return result;
    }

    private void checkModifiable() {
        if (state == ResultState.UNMODIFIED) {
            throw new IllegalArgumentException("can not modify");
        }
    }

    private synchronized void postComplete() {
        if (state != ResultState.UNMODIFIED && state != ResultState.COMPLETED) {
            if (transitionConsumer != null) {

                transitionConsumer.accept(val);
                transitionConsumer = null;
            }

            if (state == ResultState.TRANSITION && isExclusive()) {
                state = ResultState.UNMODIFIED;
            } else {
                state = ResultState.COMPLETED;
            }
        }
    }

    @SneakyThrows
    public synchronized void whenCompleted(@NotNull Consumer<T> consumer) {
        if (state == ResultState.COMPLETED) {
            consumer.accept(val);
            return;
        }

        transitionConsumer = (transitionConsumer == null ? consumer : transitionConsumer);

        if (!transitionConsumer.equals(consumer)) {
            transitionConsumer = transitionConsumer.andThen(consumer);
        }
    }

}
