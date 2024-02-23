package net.lyx.dbframework.test.transaction;

import lombok.var;
import net.lyx.dbframework.core.DatabaseConnection;
import net.lyx.dbframework.provider.DatabaseProvider;
import net.lyx.dbframework.core.compose.*;
import net.lyx.dbframework.core.security.BasicCredentials;
import net.lyx.dbframework.core.transaction.FailedTransactionPreprocessConsumer;
import net.lyx.dbframework.core.transaction.PreparedTransaction;
import net.lyx.dbframework.core.TransactionIsolation;
import net.lyx.dbframework.core.transaction.repository.*;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class PlayerTransactionRepositoryTest implements TransactionRepository {

    private static final String QUERIES_PROCEED_LOG_FORMAT = "%d/%d";

    public static void main(String[] args) {
        DatabaseProvider provider = new DatabaseProvider();
        DatabaseConnection connection = provider.openConnection(
                BasicCredentials.builder()
                        .uri("jdbc:h2:mem:default;DB_CLOSE_ON_EXIT=FALSE")
                        .username("root")
                        .password("123qwe")
                        .build());

        PreparedTransaction transaction = provider.prepareTransaction(new PlayerTransactionRepositoryTest());

        transaction.call(connection);
    }

    @Override
    public void prepareTransactionData(@NotNull TransactionData transactionData) {
        var onFailedPreprocessor = FailedTransactionPreprocessConsumer.create()
                .onExceptionThrow(Throwable::printStackTrace)
                .postprocess(result -> System.out.println("Transaction proceed is failed"))
                .preprocess(result -> {

                    var proceedQueriesCount = result.getProceedQueriesCount();
                    var totalQueriesCount = result.getTotalQueriesCount();

                    System.out.println("Transaction queries count proceed: "
                            + String.format(QUERIES_PROCEED_LOG_FORMAT, proceedQueriesCount, totalQueriesCount));
                });

        transactionData.setIsolation(TransactionIsolation.NONE);
        transactionData.setOnFailedPreprocessor(onFailedPreprocessor);
    }

    @Override
    public List<TransactionRequest> combineRequests(@NotNull TransactionData transactionData) {
        return Arrays.asList(
                this.request_createTable(transactionData.createRequestContext()),
                this.request_insertPlayerInfo(transactionData.createRequestContext())
        );
    }

    private TransactionRequest request_createTable(TransactionRequestContext context) {
        var composer = context.getComposer();
        var query = composer.useCreationPattern()
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
                .combine();
        return TransactionRequest.builder()
                .intermediateIsolation(TransactionIsolation.SERIALIZABLE)
                .target(query)
                .build();
    }

    private TransactionRequest request_insertPlayerInfo(TransactionRequestContext context) {
        var composer = context.getComposer();
        var nameField = CombinedStructs.field("NAME", "lyx_");

        var responseHandler = TransactionResponseHandler.builder()
                .ifNoPresent(() -> System.out.println("no response data"))
                .ifPresent(responseStream -> System.out.println(responseStream.findFirst().field("id").getAsInt()))
                .build();

        var query = composer.useInsertionPattern()
                .container("Players")
                .useDuplicationReduce()

                .withValue(nameField)
                .updateOnConflict(nameField)

                .withValue(CombinedStructs.field("AGE", 1))
                .combine();

        return TransactionRequest.builder()
                .intermediateIsolation(TransactionIsolation.READ_UNCOMMITTED)
                .target(query)
                .responseHandler(responseHandler)
                .build();
    }
}
