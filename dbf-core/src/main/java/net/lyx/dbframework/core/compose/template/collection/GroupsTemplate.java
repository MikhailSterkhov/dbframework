package net.lyx.dbframework.core.compose.template.collection;

import net.lyx.dbframework.core.compose.CombinedStructs;
import net.lyx.dbframework.core.Combinable;
import net.lyx.dbframework.core.compose.template.completed.CompletedGroups;

public interface GroupsTemplate extends Combinable<CompletedGroups> {

    GroupsTemplate with(CombinedStructs.CombinedField field);
}
