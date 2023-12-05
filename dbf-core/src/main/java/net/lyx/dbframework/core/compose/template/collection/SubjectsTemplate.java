package net.lyx.dbframework.core.compose.template.collection;

import net.lyx.dbframework.core.Combinable;
import net.lyx.dbframework.core.compose.CombinedStructs;
import net.lyx.dbframework.core.compose.template.completed.CompletedSubjects;

public interface SubjectsTemplate extends Combinable<CompletedSubjects> {

    SubjectsTemplate selectAll();

    SubjectsTemplate select(CombinedStructs.CombinedLabel label);

    SubjectsTemplate average(CombinedStructs.CombinedLabel label);

    SubjectsTemplate count(CombinedStructs.CombinedLabel label);

    SubjectsTemplate min(CombinedStructs.CombinedLabel label);

    SubjectsTemplate max(CombinedStructs.CombinedLabel label);

    SubjectsTemplate sum(CombinedStructs.CombinedLabel label);
}
