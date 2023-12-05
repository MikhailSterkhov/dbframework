package net.lyx.dbframework.core.compose.template;

import net.lyx.dbframework.core.compose.CombinedStructs;
import net.lyx.dbframework.core.compose.Tabled;
import net.lyx.dbframework.core.compose.TemplatedQuery;
import net.lyx.dbframework.core.compose.template.completed.CompletedGroups;
import net.lyx.dbframework.core.compose.template.completed.CompletedMerges;
import net.lyx.dbframework.core.compose.template.completed.CompletedPredicates;
import net.lyx.dbframework.core.compose.template.completed.CompletedSubjects;

public interface SearchTemplate extends TemplatedQuery, Tabled<SearchTemplate> {

    SearchTemplate limit(int limit);

    SearchTemplate subjects(CompletedSubjects subjects);

    SearchTemplate predicates(CompletedPredicates predicates);

    SearchTemplate merges(CompletedMerges merges);

    SearchTemplate groups(CompletedGroups groups);

    SearchTemplate sort(CombinedStructs.CombinedOrderedLabel order);
}
