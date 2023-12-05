package net.lyx.dbframework.core.compose.impl.collection.element;

import lombok.Builder;
import lombok.ToString;
import net.lyx.dbframework.core.compose.ConditionBinder;
import net.lyx.dbframework.core.compose.ConditionMatcher;

@ToString
@Builder(toBuilder = true)
public class WrappedCondition implements WrappedElement {

    private ConditionMatcher matcher;
    private ConditionBinder binderNext;
    private String label;
    private String value;
}
