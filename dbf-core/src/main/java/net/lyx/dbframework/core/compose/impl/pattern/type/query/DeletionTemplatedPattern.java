package net.lyx.dbframework.core.compose.impl.pattern.type.query;

import lombok.var;
import net.lyx.dbframework.core.compose.impl.pattern.AbstractPattern;
import net.lyx.dbframework.core.compose.impl.pattern.PatternCollectionConfigurator;
import net.lyx.dbframework.core.compose.impl.pattern.PatternCollections;
import net.lyx.dbframework.core.compose.impl.pattern.verification.VerificationContext;
import net.lyx.dbframework.core.compose.impl.pattern.verification.VerificationResult;
import net.lyx.dbframework.core.compose.template.DeletionTemplate;
import net.lyx.dbframework.core.compose.template.completed.CompletedPredicates;
import org.jetbrains.annotations.NotNull;

public class DeletionTemplatedPattern extends AbstractPattern implements DeletionTemplate {

    public DeletionTemplatedPattern() {
        super(PatternCollections.fromPattern("deletion.pattern"));
    }

    @Override
    public DeletionTemplate container(String containerName) {
        PatternCollectionConfigurator.create("container")
                .pushStringOnly(containerName)
                .adjust(getTotals());
        return this;
    }

    @Override
    public DeletionTemplate predicates(CompletedPredicates predicates) {
        PatternCollectionConfigurator.create("predicates")
                .pushConditionsOnly(predicates)
                .adjust(getTotals());
        return this;
    }

    @Override
    public VerificationResult verify(@NotNull VerificationContext verificationContext) {
        var containerCollectionName = "container";
        var container = getTotals().get(containerCollectionName);

        if (container == null || container.isEmpty()) {
            return verificationContext.asNotEnoughData(containerCollectionName);
        }
        return verificationContext.asSuccessful();
    }
}
