package net.lyx.dbframework.core.compose.impl.pattern.type.query;

import net.lyx.dbframework.core.compose.CombinedStructs;
import net.lyx.dbframework.core.compose.impl.pattern.AbstractPattern;
import net.lyx.dbframework.core.compose.impl.pattern.PatternCollections;
import net.lyx.dbframework.core.compose.impl.pattern.verification.VerificationContext;
import net.lyx.dbframework.core.compose.impl.pattern.verification.VerificationResult;
import net.lyx.dbframework.core.compose.template.RestorationTemplate;
import org.jetbrains.annotations.NotNull;

public class RestorationTemplatedPattern extends AbstractPattern implements RestorationTemplate {

    public RestorationTemplatedPattern() {
        super(PatternCollections.fromPattern("restoration.pattern"));
    }

    @Override
    public RestorationTemplate container(String table) {
        return null;
    }

    @Override
    public RestorationTemplate update(CombinedStructs.CombinedStyledParameter parameter) {
        return null;
    }

    @Override
    public RestorationTemplate add(CombinedStructs.CombinedStyledParameter parameter) {
        return null;
    }

    @Override
    public RestorationTemplate delete(CombinedStructs.CombinedField field) {
        return null;
    }

    @Override
    public RestorationTemplate rename(CombinedStructs.CombinedRenaming renaming) {
        return null;
    }

    @Override
    public VerificationResult verify(@NotNull VerificationContext context) {
        return context.asError("not filled");
    }
}
