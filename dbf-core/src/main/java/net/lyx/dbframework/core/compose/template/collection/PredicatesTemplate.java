package net.lyx.dbframework.core.compose.template.collection;

import net.lyx.dbframework.core.compose.CombinedStructs;
import net.lyx.dbframework.core.Combinable;
import net.lyx.dbframework.core.compose.template.completed.CompletedPredicates;

public interface PredicatesTemplate extends Combinable<CompletedPredicates> {

    PredicationAgent ifEqual(CombinedStructs.CombinedField field);

    PredicationAgent ifMatches(CombinedStructs.CombinedField field);

    PredicationAgent ifMoreThen(CombinedStructs.CombinedField field);

    PredicationAgent ifLessThen(CombinedStructs.CombinedField field);

    PredicationAgent ifMoreOrEqual(CombinedStructs.CombinedField field);

    PredicationAgent ifLessOrEqual(CombinedStructs.CombinedField field);

    interface PredicationAgent {

        PredicatesTemplate bind();

        PredicatesTemplate or();

        PredicatesTemplate and();
    }
}
