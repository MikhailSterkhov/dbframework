package net.lyx.dbframework.core.compose.impl.collection;

public interface PatternCollection<T> {

    T[] toArray();

    PatternCollection<T> add(T element);

    PatternCollection<T> addAll(T[] elements);

    PatternCollection<T> addAll(PatternCollection<?> collection);

    int size();

    boolean isEmpty();
}
