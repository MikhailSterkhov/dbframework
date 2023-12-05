package net.lyx.dbframework.core.compose.template;

import net.lyx.dbframework.core.compose.CombinedStructs;
import net.lyx.dbframework.core.compose.Tabled;
import net.lyx.dbframework.core.compose.TemplatedQuery;

public interface RestorationTemplate extends TemplatedQuery, Tabled<RestorationTemplate> {

    RestorationTemplate update(CombinedStructs.CombinedStyledParameter parameter);

    RestorationTemplate add(CombinedStructs.CombinedStyledParameter parameter);

    RestorationTemplate delete(CombinedStructs.CombinedField field);

    RestorationTemplate rename(CombinedStructs.CombinedRenaming renaming);
}
