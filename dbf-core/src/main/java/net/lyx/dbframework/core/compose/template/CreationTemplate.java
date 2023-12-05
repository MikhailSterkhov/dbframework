package net.lyx.dbframework.core.compose.template;

import net.lyx.dbframework.core.compose.impl.collection.element.Encoding;
import net.lyx.dbframework.core.compose.StorageType;
import net.lyx.dbframework.core.compose.TemplatedQuery;
import net.lyx.dbframework.core.compose.template.completed.CompletedSignature;

public interface CreationTemplate extends TemplatedQuery {

    CreationTemplate entity(StorageType target);

    CreationTemplate name(String value);

    CreationTemplate signature(CompletedSignature signature);

    CreationTemplate encoding(Encoding encoding);
}
