package net.lyx.dbframework.test.compose;

import net.lyx.dbframework.provider.DatabaseProvider;
import net.lyx.dbframework.core.compose.CombinedStructs;
import net.lyx.dbframework.core.compose.Composer;

public class DeletePlayerTest {

    public static void main(String[] args) {
        DatabaseProvider provider = new DatabaseProvider();
        Composer composer = provider.getComposer();

        String query = composer.useDeletionPattern()
                .container("Players")
                .predicates(composer.predicates()
                        .ifEqual(CombinedStructs.field("NAME", "lyx_")).bind()
                        .combine())
                .combine()
                .toNativeQuery()
                .orElse(null);

        // DELETE FROM Players WHERE NAME = 'lyx_'
        System.out.println(query);
    }
}
