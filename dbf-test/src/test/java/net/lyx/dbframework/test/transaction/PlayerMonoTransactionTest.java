package net.lyx.dbframework.test.transaction;

import net.lyx.dbframework.core.DatabaseConnection;
import net.lyx.dbframework.provider.DatabaseProvider;
import net.lyx.dbframework.core.compose.*;
import net.lyx.dbframework.core.security.BasicCredentials;
import net.lyx.dbframework.core.transaction.FailedTransactionPreprocessConsumer;
import net.lyx.dbframework.core.TransactionIsolation;
import net.lyx.dbframework.core.transaction.TransactionResult;

import java.util.Arrays;

public class PlayerMonoTransactionTest {

    public static void main(String[] args) {
        DatabaseProvider provider = new DatabaseProvider();
        DatabaseConnection connection = provider.openConnection(
                BasicCredentials.builder()
                        .uri("jdbc:h2:mem:default;DB_CLOSE_ON_EXIT=FALSE")
                        .username("root")
                        .password("123qwe")
                        .build());

        Composer composer = provider.getComposer();

        TransactionResult transactionResult = provider.openTransaction()
                .isolation(TransactionIsolation.NONE)
                .onFailed(FailedTransactionPreprocessConsumer.create()
                        .onExceptionThrow(Throwable::printStackTrace)
                        .postprocess(result -> System.out.println("proceed requests count: " + result.getProceedQueriesCount())))
                .query(provider.openTransactionQuery()
                        .marksSavepoint()
                        .writeSql("CREATE SCHEMA core"))
                .query(provider.openTransactionQuery()
                        .intermediateIsolation(TransactionIsolation.SERIALIZABLE)
                        .write(composer.useCreationPattern()
                                .name("Players")
                                .entity(StorageType.CONTAINER)
                                .signature(composer.signature()
                                        .with(CombinedStructs.styledParameter("ID",
                                                ParameterStyle.builder()
                                                        .type(ParameterType.BIGINT)
                                                        .addons(Arrays.asList(
                                                                ParameterAddon.PRIMARY,
                                                                ParameterAddon.INCREMENTING,
                                                                ParameterAddon.NOTNULL
                                                        ))
                                                        .build()))
                                        .with(CombinedStructs.styledParameter("NAME",
                                                ParameterStyle.builder()
                                                        .type(ParameterType.STRING)
                                                        .addons(Arrays.asList(
                                                                ParameterAddon.PRIMARY,
                                                                ParameterAddon.NOTNULL
                                                        ))
                                                        .build()))
                                        .combine())
                                .combine()))
                .combine()
                .call(connection);

        System.out.println(transactionResult);
    }
}
