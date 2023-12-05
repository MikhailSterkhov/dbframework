package net.lyx.dbframework.core.compose.impl.pattern.type.collection;

import net.lyx.dbframework.core.compose.CombinedStructs;
import net.lyx.dbframework.core.compose.template.collection.GroupsTemplate;
import net.lyx.dbframework.core.compose.template.completed.CompletedGroups;

import java.util.ArrayList;
import java.util.List;

public class GroupsPattern implements GroupsTemplate, CompletedGroups {

    private final List<CombinedStructs.CombinedField> fields = new ArrayList<>();

    @Override
    public CompletedGroups combine() {
        return this;
    }

    @Override
    public GroupsTemplate with(CombinedStructs.CombinedField field) {
        fields.add(field);
        return this;
    }

    @Override
    public CombinedStructs.CombinedField[] fields() {
        return fields.toArray(new CombinedStructs.CombinedField[0]);
    }
}
