package net.lyx.dbframework.core.compose.template.collection;

import net.lyx.dbframework.core.compose.CombinedStructs;
import net.lyx.dbframework.core.Combinable;
import net.lyx.dbframework.core.compose.template.completed.CompletedMerges;

public interface MergesTemplate extends Combinable<CompletedMerges> {

    MergesTemplate full(CombinedStructs.CombinedMerge joins);

    MergesTemplate outside(CombinedStructs.CombinedMerge joins);

    MergesTemplate additional(CombinedStructs.CombinedMerge joins);

    MergesTemplate inner(CombinedStructs.CombinedMerge joins);

    MergesTemplate unscoped(CombinedStructs.CombinedMerge joins);
}
