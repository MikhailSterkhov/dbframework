package net.lyx.dbframework.core.compose.transform;

import lombok.RequiredArgsConstructor;
import net.lyx.dbframework.core.compose.impl.collection.PatternCollection;
import net.lyx.dbframework.core.compose.impl.pattern.PatternCollections;
import net.lyx.dbframework.core.compose.transform.data.TransformInputState;
import net.lyx.dbframework.core.compose.transform.data.TransformStatements;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public final class PatternToQueryTransformer {

    private final String pattern;
    private final PatternCollections totals;

    public String toNativeQuery() {
        StringBuilder nativeQueryBuilder = new StringBuilder();
        String[] patternLineArray = pattern.split("\n");

        for (String patternLine : patternLineArray) {
            patternLine = patternLine.replace("\r", "");
            String lineSuffix = QueryTransformationOps.getLineSuffix(patternLine).replace("\r", "");

            // Если строчка не начинается с условия, то вставляем ее исходное значение
            if (!patternLine.trim().startsWith("@")) {
                nativeQueryBuilder
                        .append(patternLine)
                        .append(" ");
                continue;
            }

            TransformInputState patternState = createState(patternLine);
            TransformStatements statement = patternState.getMainStatement();

            String name = patternState.getName();

            PatternCollection<Object> collection = totals.get(name);

            String output = QueryTransformationOps.transformInput(collection == null ? new Object[0] : collection.toArray(),
                    patternState,
                    collection != null && !collection.isEmpty()
                            ? statement.getCorrected()
                            : statement.getUncorrected(),
                    lineSuffix
            );

            nativeQueryBuilder.append(output);
            if (!nativeQueryBuilder.toString().endsWith(" ")) {
                nativeQueryBuilder.append(" ");
            }
        }

        return nativeQueryBuilder.toString();
    }

    private TransformInputState createState(String patternLine) {
        Map<String, String> statementsTexts = QueryTransformationOps.getStatementsTexts(patternLine);

        String collectionCondition = QueryTransformationOps.trimNamedCondition(patternLine);
        String fullStatement = statementsTexts.get(collectionCondition);

        Map<String, TransformStatements> followed = statementsTexts.keySet()
                .stream()
                .filter(key -> key.startsWith("$"))
                .collect(
                        Collectors.toMap(
                                value -> value.substring(1),
                                value -> createStatements(value, statementsTexts.get(value)),

                                (u, v) -> { throw new RuntimeException(); },
                                () -> new TreeMap<>(
                                        Comparator.comparingInt(key -> statementsTexts.get("$" + key).length()))
                        )
                );

        return TransformInputState.builder()
                .name(collectionCondition.substring(1))
                .mainStatement(createStatements(collectionCondition, fullStatement))
                .followedStatements(followed)
                .build();
    }

    private TransformStatements createStatements(String condition, String fullPattern) {
        String correctlyFullPattern = fullPattern;
        if (correctlyFullPattern.startsWith("@") && !correctlyFullPattern.endsWith("\"")) {
            correctlyFullPattern += "\"";
        }
        else {
            correctlyFullPattern = String.format("\"%s\"", fullPattern);
        }

        String[] statementsArray = QueryTransformationOps.findSplitStatements(correctlyFullPattern);

        TransformStatements.Builder statementBuilder
                = TransformStatements.builder()
                    .condition(condition)
                    .full(QueryTransformationOps.fixEndsCorners(correctlyFullPattern))
                    .corrected(statementsArray[0]);

        if (statementsArray.length == 1) {
            return statementBuilder
                    .uncorrected("")
                    .build();
        }

        return statementBuilder
                .uncorrected(statementsArray[1])
                .build();
    }
}
