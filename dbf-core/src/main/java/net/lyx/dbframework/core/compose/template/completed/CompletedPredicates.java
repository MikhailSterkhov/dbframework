package net.lyx.dbframework.core.compose.template.completed;

import net.lyx.dbframework.core.compose.CombinedStructs;
import net.lyx.dbframework.core.compose.ConditionBinder;
import net.lyx.dbframework.core.compose.ConditionMatcher;

public interface CompletedPredicates {

    CompletedPredicateNode first();

    interface CompletedPredicateNode {

        CompletedPredicateNode poll();

        CombinedStructs.CombinedField field();
        
        ConditionMatcher matcher();

        ConditionBinder binder();
    }
}
