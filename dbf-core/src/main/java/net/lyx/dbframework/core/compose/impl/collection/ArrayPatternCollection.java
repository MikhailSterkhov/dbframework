package net.lyx.dbframework.core.compose.impl.collection;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;

@ToString
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class ArrayPatternCollection<T> implements PatternCollection<T> {

    protected final List<T> list;

    @Override
    public ArrayPatternCollection<T> add(T element) {
        list.add(element);
        return this;
    }

    @Override
    public ArrayPatternCollection<T> addAll(T[] elements) {
        list.addAll(Arrays.asList(elements));
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public PatternCollection<T> addAll(PatternCollection<?> collection) {
        return addAll((T[]) collection.toArray());
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }
}
