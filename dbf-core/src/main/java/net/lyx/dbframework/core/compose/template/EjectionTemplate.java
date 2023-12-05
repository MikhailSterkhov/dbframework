package net.lyx.dbframework.core.compose.template;

import net.lyx.dbframework.core.compose.StorageType;
import net.lyx.dbframework.core.compose.TemplatedQuery;

public interface EjectionTemplate extends TemplatedQuery {

    EjectionTemplate entity(StorageType storageType);

    EjectionTemplate name(String specifiedName);
}
