package net.lyx.dbframework.test.compose;

import net.lyx.dbframework.provider.DatabaseProvider;
import net.lyx.dbframework.core.compose.*;

import java.util.Collections;

public class ModifyPlayersTableStructureTest {

    public static void main(String[] args) {
        DatabaseProvider provider = new DatabaseProvider();
        Composer composer = provider.getComposer();

        String query = composer.useRestorationPattern()
                .container("Players")
                .add(CombinedStructs.styledParameter("SIZE",
                        ParameterStyle.builder()
                                .defaultValue(150)
                                .type(ParameterType.INT)
                                .addons(Collections.singletonList(ParameterAddon.NOTNULL))
                                .build()))
                .combine()
                .toNativeQuery()
                .orElse(null);

        System.out.println(query);
    }
}
