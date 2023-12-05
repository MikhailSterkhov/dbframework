package net.lyx.dbframework.test.compose;

import net.lyx.dbframework.provider.DatabaseProvider;
import net.lyx.dbframework.core.compose.*;
import net.lyx.dbframework.core.compose.impl.collection.element.Encoding;

import java.util.Arrays;
import java.util.Collections;

public class CreatePlayersTableTest {

    public static void main(String[] args) {
        DatabaseProvider provider = new DatabaseProvider();
        Composer composer = provider.getComposer();

        String query = composer.useCreationPattern()
                .encoding(Encoding.builder()
                        .characterStyle("utf8mb4")
                        .collate("utf8mb4_unicode_ci")
                        .build())
                .entity(StorageType.CONTAINER)
                .name("Players")
                .signature(composer.signature()
                        .with(CombinedStructs.styledParameter("ID",
                                ParameterStyle.builder()
                                        .type(ParameterType.BIGINT)
                                        .addons(Arrays.asList(
                                                ParameterAddon.INCREMENTING,
                                                ParameterAddon.NOTNULL,
                                                ParameterAddon.UNIQUE))
                                        .build()))
                        .with(CombinedStructs.styledParameter("NAME",
                                ParameterStyle.builder()
                                        .length(32)
                                        .type(ParameterType.STRING)
                                        .addons(Arrays.asList(
                                                ParameterAddon.UNIQUE,
                                                ParameterAddon.NOTNULL))
                                        .encoding(Encoding.builder()
                                                .characterStyle("utf8mb4")
                                                .collate("utf8mb4_unicode_ci")
                                                .build())
                                        .build()))
                        .with(CombinedStructs.styledParameter("AGE",
                                ParameterStyle.builder()
                                        .type(ParameterType.INT)
                                        .addons(Collections.singletonList(
                                                ParameterAddon.NOTNULL))
                                        .defaultValue(1)
                                        .build()))
                        .combine())
                .combine()
                .toNativeQuery()
                .orElse(null);

        // CREATE TABLE IF NOT EXISTS Players ( ID BIGINT INCREMENTING NOTNULL UNIQUE, NAME STRING(32) UNIQUE NOTNULL CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci, AGE INT NOTNULL DEFAULT 1 ) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci
        System.out.println(query);
    }
}
