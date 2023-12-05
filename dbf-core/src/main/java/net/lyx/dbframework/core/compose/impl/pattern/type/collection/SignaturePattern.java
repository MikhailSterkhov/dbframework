package net.lyx.dbframework.core.compose.impl.pattern.type.collection;

import net.lyx.dbframework.core.compose.CombinedStructs;
import net.lyx.dbframework.core.compose.template.collection.SignatureTemplate;
import net.lyx.dbframework.core.compose.template.completed.CompletedSignature;

import java.util.ArrayList;
import java.util.List;

public class SignaturePattern implements SignatureTemplate, CompletedSignature {

    private final List<CombinedStructs.CombinedStyledParameter> list = new ArrayList<>();

    @Override
    public CompletedSignature combine() {
        return this;
    }

    @Override
    public SignatureTemplate with(CombinedStructs.CombinedStyledParameter parameter) {
        list.add(parameter);
        return this;
    }

    @Override
    public CombinedStructs.CombinedStyledParameter[] parameters() {
        return list.toArray(new CombinedStructs.CombinedStyledParameter[0]);
    }
}
