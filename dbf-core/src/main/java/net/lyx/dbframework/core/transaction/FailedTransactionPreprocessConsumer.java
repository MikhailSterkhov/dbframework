package net.lyx.dbframework.core.transaction;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.SQLException;
import java.util.function.Consumer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FailedTransactionPreprocessConsumer implements Consumer<TransactionResult> {

    public static FailedTransactionPreprocessConsumer create() {
        return new FailedTransactionPreprocessConsumer();
    }

    private Consumer<SQLException> onExceptionThrowConsumer;
    private Consumer<TransactionResult> postprocessConsumer, preprocessConsumer;

    public FailedTransactionPreprocessConsumer onExceptionThrow(Consumer<SQLException> extension) {
        this.onExceptionThrowConsumer = extension;
        return this;
    }

    public FailedTransactionPreprocessConsumer postprocess(Consumer<TransactionResult> extension) {
        if (postprocessConsumer == null) {
            postprocessConsumer = extension;
        } else {
            postprocessConsumer = postprocessConsumer.andThen(extension);
        }
        return this;
    }

    public FailedTransactionPreprocessConsumer preprocess(Consumer<TransactionResult> extension) {
        if (preprocessConsumer == null) {
            preprocessConsumer = extension;
        } else {
            preprocessConsumer = preprocessConsumer.andThen(extension);
        }
        return this;
    }

    @Override
    public void accept(TransactionResult result) {
        if (result.getStatus() == TransactionStatus.SUCCESS) {
            return;
        }

        processExtension(preprocessConsumer, result);

        processExtension(onExceptionThrowConsumer, result.getException());

        processExtension(postprocessConsumer, result);
    }

    private <T> void processExtension(Consumer<T> extension, T intermediate) {
        if (extension != null && intermediate != null) {
            extension.accept(intermediate);
        }
    }

}
