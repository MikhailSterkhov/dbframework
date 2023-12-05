package net.lyx.dbframework.core.compose.template;

import net.lyx.dbframework.core.compose.Tabled;
import net.lyx.dbframework.core.compose.TemplatedQuery;
import net.lyx.dbframework.core.compose.template.completed.CompletedPredicates;

public interface DeletionTemplate extends TemplatedQuery, Tabled<DeletionTemplate> {

    DeletionTemplate predicates(CompletedPredicates predicates);
}
