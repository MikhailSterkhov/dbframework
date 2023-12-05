package net.lyx.dbframework.core.compose.template.completed;

import net.lyx.dbframework.core.compose.CombinedStructs;

public interface CompletedMerges {

    CombinedStructs.CombinedMerge[] fulls(); // full

    CombinedStructs.CombinedMerge[] outsides(); // right outer

    CombinedStructs.CombinedMerge[] additions(); // left inner

    CombinedStructs.CombinedMerge[] inners(); // inner

    CombinedStructs.CombinedMerge[] unscoped(); // outer
}
