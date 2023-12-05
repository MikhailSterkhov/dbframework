package net.lyx.dbframework.core.compose.impl.pattern.verification;

import lombok.RequiredArgsConstructor;
import lombok.var;
import net.lyx.dbframework.core.compose.impl.pattern.PatternCollections;

import java.util.HashSet;
import java.util.Set;

public final class VerificationContext {

    public VerificationResult asSuccessful() {
        return VerificationResult.builder()
                .code(VerificationResultCode.SUCCESS)
                .build();
    }

    public VerificationResult asExceptionally(Exception exception) {
        exception.printStackTrace();
        return asError(exception.getMessage());
    }

    public VerificationResult asError(String errorMessage) {
        return VerificationResult.builder()
                .code(VerificationResultCode.ERROR)
                .errorMessage(errorMessage)
                .build();
    }

    public VerificationResult asNotEnoughData(String name) {
        var errorMessage = String.format("parameter '%s' is not initialized", name);
        return asError(errorMessage);
    }

    public Transaction newTransaction(PatternCollections totals) {
        return new Transaction(totals);
    }

    @RequiredArgsConstructor
    public class Transaction {

        private final PatternCollections totals;

        private final Set<String> namesAsNull = new HashSet<>();
        private final Set<Exception> conflicts = new HashSet<>();

        public Transaction checkNotNull(Object value, String name) {
            if (value == null) {
                namesAsNull.add(name);
            }
            return this;
        }

        public Transaction markRequiredCollection(String name) {
            var collection = totals.get(name);

            if (collection == null || collection.isEmpty()) {
                namesAsNull.add(String.format("'%s'", name));
            }
            return this;
        }

        public Transaction markRequiredCollections(String... names) {
            for (var name : names) markRequiredCollection(name);
            return this;
        }

        public Transaction markCollectionMaxSize(String name, int maxSize) {
            var collection = totals.get(name);

            if (collection != null && collection.size() > maxSize) {
                conflicts.add(
                        new IllegalArgumentException(
                                String.format("Collection '%s' size must be <= %d", name, maxSize)
                        ));
            }
            return this;
        }

        public Transaction markCollectionsConflict(String collectionName1, String collectionName2) {
            var collection1 = totals.get(collectionName1);
            var collection2 = totals.get(collectionName2);

            if ((collection1 != null && !collection1.isEmpty())
                    && (collection2 != null && !collection2.isEmpty())) {

                conflicts.add(new IllegalArgumentException(
                        String.format("Found collections conflict with '%s' and '%s'",
                                collectionName1, collectionName2)
                ));
            }
            return this;
        }

        public Transaction markCollectionsTogether(String collectionName1, String collectionName2) {
            var collection1 = totals.get(collectionName1);
            var collection2 = totals.get(collectionName2);

            if (((collection1 != null && !collection1.isEmpty()) && (collection2 == null || collection2.isEmpty()))
                    || ((collection1 == null || collection1.isEmpty()) && (collection2 != null && !collection2.isEmpty()))) {

                conflicts.add(new IllegalArgumentException(
                        String.format("Collections '%s' and '%s' must be works together",
                                collectionName1, collectionName2)
                ));
            }
            return this;
        }

        public VerificationResult commitVerificationResult() {
            if (!namesAsNull.isEmpty()) {
                var name = namesAsNull.stream()
                        .findFirst()
                        .orElse(null);

                return asNotEnoughData(name);
            }

            if (!conflicts.isEmpty()) {
                var conflict = conflicts.stream()
                        .findFirst()
                        .orElse(null);

                return asExceptionally(conflict);
            }

            return asSuccessful();
        }
    }
}
