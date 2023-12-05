package net.lyx.dbframework.core.compose.impl.pattern.type.query;

import net.lyx.dbframework.core.compose.StorageType;
import net.lyx.dbframework.core.compose.impl.pattern.PatternCollectionConfigurator;
import net.lyx.dbframework.core.compose.impl.pattern.AbstractPattern;
import net.lyx.dbframework.core.compose.impl.pattern.PatternCollections;
import net.lyx.dbframework.core.compose.impl.pattern.verification.VerificationContext;
import net.lyx.dbframework.core.compose.impl.pattern.verification.VerificationResult;
import net.lyx.dbframework.core.compose.template.EjectionTemplate;
import org.jetbrains.annotations.NotNull;

public class EjectionTemplatedPattern extends AbstractPattern implements EjectionTemplate {

    private static final String ENTITY = "entity";
    private static final String NAME = "name";

    public EjectionTemplatedPattern() {
        super(PatternCollections.fromPattern("ejection.pattern"));
    }

    @Override
    public EjectionTemplate entity(StorageType entity) {
        PatternCollectionConfigurator.create(ENTITY)
                .pushStringOnly(entity.toString())
                .adjust(getTotals());
        return this;
    }

    @Override
    public EjectionTemplate name(String specifiedName) {
        PatternCollectionConfigurator.create(NAME)
                .pushStringOnly(specifiedName)
                .adjust(getTotals());
        return this;
    }

    @Override
    public VerificationResult verify(@NotNull VerificationContext verificationContext) {
        return verificationContext.newTransaction(getTotals())
                .markRequiredCollections(ENTITY, NAME)
                .commitVerificationResult();
    }
}
