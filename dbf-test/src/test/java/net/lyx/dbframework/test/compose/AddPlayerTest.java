package net.lyx.dbframework.test.compose;

import net.lyx.dbframework.provider.DatabaseProvider;
import net.lyx.dbframework.core.compose.CombinedStructs;
import net.lyx.dbframework.core.compose.Composer;

public class AddPlayerTest {

    public static void main(String[] args) {
        DatabaseProvider provider = new DatabaseProvider();
        Composer composer = provider.getComposer();

        String query = composer.useInsertionPattern()
                .container("Players")
                .withValue(CombinedStructs.field("NAME", "lyx_"))
                .withValue(CombinedStructs.field("AGE", 1))
                .useDuplicationReduce()
                .updateOnConflict(CombinedStructs.field("NAME", "lyx_"))
                .combine()
                .toNativeQuery()
                .orElse(null);

        // INSERT INTO Players ( NAME, AGE ) VALUES ( 'lyx_', 1 ) ON DUPLICATE KEY UPDATE NAME = 'lyx_'
        System.out.println(query);
    }
}
