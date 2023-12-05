package net.lyx.dbframework.core.compose.impl.pattern.type.query;

import net.lyx.dbframework.core.compose.impl.collection.element.Encoding;
import net.lyx.dbframework.core.compose.StorageType;
import net.lyx.dbframework.core.compose.impl.pattern.AbstractPattern;
import net.lyx.dbframework.core.compose.impl.pattern.PatternCollectionConfigurator;
import net.lyx.dbframework.core.compose.impl.pattern.PatternCollections;
import net.lyx.dbframework.core.compose.impl.pattern.verification.VerificationContext;
import net.lyx.dbframework.core.compose.impl.pattern.verification.VerificationResult;
import net.lyx.dbframework.core.compose.template.CreationTemplate;
import net.lyx.dbframework.core.compose.template.completed.CompletedSignature;
import org.jetbrains.annotations.NotNull;

public class CreationTemplatedPattern extends AbstractPattern implements CreationTemplate {

    public CreationTemplatedPattern() {
        super(PatternCollections.fromPattern("creation.pattern"));
    }

    @Override
    public CreationTemplate entity(StorageType target) {
        PatternCollectionConfigurator.create("entity")
                .pushStringOnly(target.toString())
                .adjust(getTotals());
        return this;
    }

    @Override
    public CreationTemplate name(String value) {
        PatternCollectionConfigurator.create("name")
                .pushStringOnly(value)
                .adjust(getTotals());
        return this;
    }

    @Override
    public CreationTemplate signature(CompletedSignature signature) {
        PatternCollectionConfigurator.create("parameters")
                .pushSignatureOnly(signature)
                .adjust(getTotals());

        setSignatureSupportsEnabled();
        return this;
    }

    @Override
    public CreationTemplate encoding(Encoding encoding) {
        PatternCollectionConfigurator.create("encoding")
                .pushEncodingOnly(encoding)
                .adjust(getTotals());
        return this;
    }

    @Override
    public VerificationResult verify(@NotNull VerificationContext context) {
        return context.newTransaction(getTotals())
                .markCollectionsTogether("has_signature", "parameters")
                .commitVerificationResult();
    }

    private void setSignatureSupportsEnabled() {
        PatternCollectionConfigurator.create("has_signature")
                .pushStringOnly("0")
                .adjust(getTotals());
    }
}
