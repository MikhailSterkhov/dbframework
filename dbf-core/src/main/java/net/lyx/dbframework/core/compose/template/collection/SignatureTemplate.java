package net.lyx.dbframework.core.compose.template.collection;

import net.lyx.dbframework.core.compose.CombinedStructs;
import net.lyx.dbframework.core.Combinable;
import net.lyx.dbframework.core.compose.template.completed.CompletedSignature;

public interface SignatureTemplate extends Combinable<CompletedSignature> {

    SignatureTemplate with(CombinedStructs.CombinedStyledParameter parameter);
}
