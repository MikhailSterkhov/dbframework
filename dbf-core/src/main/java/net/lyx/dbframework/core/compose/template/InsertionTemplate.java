package net.lyx.dbframework.core.compose.template;

import net.lyx.dbframework.core.compose.CombinedStructs;
import net.lyx.dbframework.core.compose.Tabled;
import net.lyx.dbframework.core.compose.TemplatedQuery;

public interface InsertionTemplate extends TemplatedQuery, Tabled<InsertionTemplate> {

    InsertionTemplate withValue(CombinedStructs.CombinedField field);

    InsertionTemplate updateOnConflict(CombinedStructs.CombinedField field);

    InsertionTemplate useDuplicationReduce();
}
