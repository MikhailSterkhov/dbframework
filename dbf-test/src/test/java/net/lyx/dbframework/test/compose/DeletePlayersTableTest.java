package net.lyx.dbframework.test.compose;

import net.lyx.dbframework.provider.DatabaseProvider;
import net.lyx.dbframework.core.compose.Composer;
import net.lyx.dbframework.core.compose.StorageType;

public class DeletePlayersTableTest {

    public static void main(String[] args) {
        DatabaseProvider provider = new DatabaseProvider();
        Composer composer = provider.getComposer();

        String query = composer.useEjectionPattern()
                .entity(StorageType.CONTAINER)
                .name("Players")
                .combine()
                .toNativeQuery()
                .orElse(null);

        // DROP TABLE Players ;
        System.out.println(query);
    }
}
