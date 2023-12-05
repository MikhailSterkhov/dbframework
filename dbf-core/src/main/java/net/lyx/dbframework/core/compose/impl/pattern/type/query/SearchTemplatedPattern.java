package net.lyx.dbframework.core.compose.impl.pattern.type.query;

import net.lyx.dbframework.core.compose.CombinedStructs;
import net.lyx.dbframework.core.compose.impl.pattern.PatternCollectionConfigurator;
import net.lyx.dbframework.core.compose.impl.pattern.AbstractPattern;
import net.lyx.dbframework.core.compose.impl.pattern.PatternCollections;
import net.lyx.dbframework.core.compose.impl.pattern.verification.VerificationContext;
import net.lyx.dbframework.core.compose.impl.pattern.verification.VerificationResult;
import net.lyx.dbframework.core.compose.template.SearchTemplate;
import net.lyx.dbframework.core.compose.template.completed.*;
import org.jetbrains.annotations.NotNull;

public class SearchTemplatedPattern extends AbstractPattern implements SearchTemplate {

    public SearchTemplatedPattern() {
        super(PatternCollections.fromPattern("search.pattern"));
    }

    @Override
    public SearchTemplate limit(int limit) {
        PatternCollectionConfigurator.create("limit")
                .pushStringOnly(String.valueOf(limit))
                .adjust(getTotals());
        return this;
    }

    @Override
    public SearchTemplate container(String table) {
        PatternCollectionConfigurator.create("container")
                .pushStringOnly(table)
                .adjust(getTotals());
        return this;
    }

    @Override
    public SearchTemplate subjects(CompletedSubjects subjects) {
        PatternCollectionConfigurator.create("subjects")
                .pushSubjectsOnly(subjects)
                .adjust(getTotals());
        return this;
    }

    @Override
    public SearchTemplate predicates(CompletedPredicates predicates) {
        PatternCollectionConfigurator.create("predicates")
                .pushConditionsOnly(predicates)
                .adjust(getTotals());
        return this;
    }

    @Override
    public SearchTemplate merges(CompletedMerges merges) {
        PatternCollectionConfigurator.create("merges")
                .pushMergesOnly(merges)
                .adjust(getTotals());
        return this;
    }

    @Override
    public SearchTemplate groups(CompletedGroups groups) {
        PatternCollectionConfigurator.create("groups")
                .pushGroupsOnly(groups)
                .adjust(getTotals());
        return this;
    }

    @Override
    public SearchTemplate sort(CombinedStructs.CombinedOrderedLabel order) {
        PatternCollectionConfigurator.create("orders")
                .pushOrdersOnly(order)
                .adjust(getTotals());
        return this;
    }

    @Override
    public VerificationResult verify(@NotNull VerificationContext context) {
        return context.newTransaction(getTotals())
                .markCollectionMaxSize("limit", 1)
                .markCollectionMaxSize("container", 1)
                .markCollectionMaxSize("orders", 1)
                //.markCollectionsConflict("groups", "predicates")
                .commitVerificationResult();
    }
}
