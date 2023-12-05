package net.lyx.dbframework.core.compose.impl.collection;

import net.lyx.dbframework.core.compose.impl.collection.element.WrappedElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WrappedPatternCollection extends ArrayPatternCollection<WrappedElement> {

    public static WrappedPatternCollection singleton(WrappedElement element) {
        return new WrappedPatternCollection(Collections.singletonList(element));
    }

    public static WrappedPatternCollection multiplied() {
        return new WrappedPatternCollection(new ArrayList<>());
    }

    protected WrappedPatternCollection(List<WrappedElement> list) {
        super(list);
    }

    @Override
    public WrappedElement[] toArray() {
        return list.toArray(new WrappedElement[0]);
    }
}
