package net.lyx.dbframework.core.compose.template.completed;

import net.lyx.dbframework.core.compose.CombinedStructs;

public interface CompletedSubjects {

    CombinedStructs.CombinedLabel[] generals();

    CombinedStructs.CombinedLabel[] averages();

    CombinedStructs.CombinedLabel[] counts();

    CombinedStructs.CombinedLabel[] mines();

    CombinedStructs.CombinedLabel[] maxes();

    CombinedStructs.CombinedLabel[] summed();

    boolean isSelectedAll();
}
