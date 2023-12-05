package net.lyx.dbframework.core.compose;

import net.lyx.dbframework.core.compose.template.*;
import net.lyx.dbframework.core.compose.template.collection.*;

public interface Composer {

    RestorationTemplate useRestorationPattern();

    CreationTemplate useCreationPattern();

    EjectionTemplate useEjectionPattern();

    DeletionTemplate useDeletionPattern();

    SearchTemplate useSearchPattern();

    InsertionTemplate useInsertionPattern();

    MergesTemplate merges();

    GroupsTemplate groups();

    SubjectsTemplate subjects();

    SignatureTemplate signature();

    PredicatesTemplate predicates();
}
