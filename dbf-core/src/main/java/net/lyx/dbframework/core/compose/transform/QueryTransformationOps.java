package net.lyx.dbframework.core.compose.transform;

import lombok.experimental.UtilityClass;
import net.lyx.dbframework.core.compose.transform.data.TransformInputState;
import net.lyx.dbframework.core.compose.transform.data.TransformStatements;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class QueryTransformationOps {

    private static final String CONDITION_FORMAT = "@[%s]";

    private int getRepeatsCount(String input, String target) {
        Pattern pattern = Pattern.compile(target);
        Matcher matcher = pattern.matcher(input);

        int count = 0;
        while (matcher.find()) {
            count++;
        }

        return count;
    }

    public Map<String, String> getStatementsTexts(String input) {
        Map<String, String> resultMap = new LinkedHashMap<>();
        String statementCorner = "\"";

        String buf = input;
        while (buf.contains("->" + statementCorner)) {
            String name = trimNamedCondition(buf);

            if (buf.contains("+")) {
                int firstCornerIndex = buf.indexOf(statementCorner) + 1;
                buf = buf.substring(firstCornerIndex, buf.lastIndexOf(statementCorner));
            } else {
                if (buf.startsWith("@")) {
                    buf = buf.substring(buf.indexOf(statementCorner));
                } else {
                    buf = buf.substring(buf.indexOf(String.format(CONDITION_FORMAT, name)));
                    continue;
                }

                int firstCornerIndex = buf.indexOf(statementCorner) + 1;

                String prev = buf;
                buf = buf.substring(firstCornerIndex, findLastCornerIndex(firstCornerIndex - 1, buf));

                String left = prev.replace(buf, "");
                if (left.contains("->")) {

                    Map<String, String> leftTexts = getStatementsTexts(left);
                    resultMap.putAll(leftTexts);
                }

                if (buf.endsWith(":")) {
                    buf = buf.substring(0, buf.length() - 1);
                }
            }

            resultMap.put(name, buf);
        }

        return resultMap;
    }

    private int findLastCornerIndex(int firstCornerIndex, String input) {
        int skips = 0, add = 0;

        char[] charArray = input.toCharArray();
        int max = charArray.length;

        for (int index = firstCornerIndex + 1; index < max; index++) {
            char ch = charArray[index];
            if (ch == '"') {
                if (skips > 0) {
                    skips--;
                    continue;
                }

                int prev = index - 1;
                if (prev >= 0) {

                    // чтобы можно было добавлять кавычки в исходный стейтмент, которые не будут являться гранями стейтмента, а пойдут в результативный стейтмент
                    if (charArray[prev] == '\\') {
                        continue;
                    }

                    // чекаем разделение на стейтменты
                    if (charArray[prev] == ':') {
                        add++;
                        continue;
                    }
                }

                // чекаем разделение на стейтменты (наперед)
                int next = index + 1;
                if (next != max && charArray[next] == ':') {
                    skips++;
                    continue;
                }

                return index + add;
            }
            else if (ch == '@') {
                skips += 2;
            }
        }

        return -1;
    }

    public String[] findSplitStatements(String fullStatement) {
        String splitRegex = "\":\"";
        String corner = "\"";

        if (!fullStatement.contains(splitRegex)) {
            return new String[]{fullStatement};
        }

        String[] variantsArray = fullStatement.split(splitRegex);
        if (variantsArray.length == 2) {
            return new String[]{variantsArray[0] + corner, corner + variantsArray[1]};
        }

        List<String> resultsList = new ArrayList<>();

        resultsList.add(String.join(splitRegex, Arrays.asList(variantsArray).subList(0, variantsArray.length - 1)));
        resultsList.add(corner + variantsArray[variantsArray.length - 1]);

        return resultsList.toArray(new String[0]);
    }

    public String fixEndsCorners(String input) {
        if (getRepeatsCount(input, "\"") % 2 == 0) {
            return input;
        }

        if (input.endsWith("\"\"") || (input.endsWith("\"") && !input.startsWith("\"") && !input.startsWith("@"))) {
            input = input.substring(0, input.length() - 1);
        }
        return input;
    }

    private String fixTotalCorners(String input) {
        if (input.startsWith("\"") && input.endsWith("\"")) {
            input = input.substring(1, input.length() - 1);
        }
        return input;
    }

    public String trimNamedCondition(String input) {
        input = input.trim();

        String conditionPrefix = "@";
        String conditionEndSign = "]";

        if (input.startsWith(conditionPrefix)) {
            return input.substring(2, input.indexOf(conditionEndSign));
        }

        int beginTokenIndex = input.indexOf(conditionPrefix);
        return input.substring(beginTokenIndex + 2, input.indexOf(conditionEndSign, beginTokenIndex));
    }

    public String transformInput(Object[] elements, TransformInputState mainStatement, String input, String delimiter) {
        StringBuilder stringBuilder = new StringBuilder();
        AtomicReference<String> inputRef = new AtomicReference<>(input);

        // для начала найдем вложененые функции и обработаем их, присоединив необходимые стейтменты
        String followedPatternFormat = "@[$%s]->%s";
        String placeholderPatternFormat = "{$%s}";

        Map<String, TransformStatements> followedStatementsMap
                = mainStatement.getFollowedStatements();

        AtomicReference<String> previousPlaceholderRef = new AtomicReference<>();
        AtomicReference<String> previousPatternRef = new AtomicReference<>();

        followedStatementsMap.forEach((name, statements) -> {
            String currentInput = inputRef.get();
            String previousPlaceholder = previousPlaceholderRef.get();

            if (previousPlaceholder != null) {
                String previousPattern = previousPatternRef.get();

                String statementFull = statements.getFull();
                String corrected = statements.getCorrected();
                String uncorrected = statements.getUncorrected();

                statements.setFull(statementFull.replace(previousPattern, previousPlaceholder));
                statements.setCorrected(corrected.replace(previousPattern, previousPlaceholder));
                statements.setUncorrected(uncorrected.replace(previousPattern, previousPlaceholder));
            }

            String placeholder = String.format(placeholderPatternFormat, name);
            previousPlaceholderRef.set(placeholder);

            String pattern = String.format(followedPatternFormat, name, statements.getFull());
            previousPatternRef.set(pattern);

            String replacedInput = currentInput.replace(pattern, placeholder);
            if (replacedInput.trim().startsWith("\"@"))
                replacedInput = replacedInput.replaceFirst("\"@", "@");

            inputRef.set(replacedInput);
        });

        // летс гоу реплейсить все
        for (Object element : elements) {
            input = fixTotalCorners(inputRef.get());

            Map<String, Object> elementFieldsValuesMap = getElementFieldsMap(element);

            // теперь зареплейсим внутренние функции
            List<String> followNamesList = new ArrayList<>(followedStatementsMap.keySet());
            Collections.reverse(followNamesList);

            for (String followName : followNamesList) {
                String placeholder = String.format("{$%s}", followName);

                if (!input.contains(placeholder)) {
                    continue;
                }

                TransformStatements statements = followedStatementsMap.get(followName);
                Object obj = elementFieldsValuesMap.get(followName);

                String statementResult = "";

                if (obj != null) {
                    if (statements != null) {
                        statementResult = statements.getCorrected();
                    }
                } else {
                    if (statements != null) {
                        statementResult = statements.getUncorrected();
                    }
                }

                String result = fixEndsCorners(fixTotalCorners(statementResult));

                input = input.replace(placeholder, result);
            }

            // теперь реплейсим все переменные
            for (String field : elementFieldsValuesMap.keySet()) {
                Object value = elementFieldsValuesMap.get(field);

                input = input.replace("$" + field, value == null ? "" : value.toString());
            }

            // и херачим готовую строку в результат
            stringBuilder
                    .append(input)
                    .append(delimiter);
        }

        // а если еще и есть разделитель, то удалим его на конце
        if (!delimiter.isEmpty() && stringBuilder.toString().endsWith(delimiter)) {

            int length = stringBuilder.length();
            stringBuilder.delete(length - delimiter.length(), length);
        }

        return stringBuilder.toString();
    }

    private Map<String, Object> getElementFieldsMap(Object element) {
        Field[] fields = element.getClass().getDeclaredFields();

        Map<String, Object> result = new HashMap<>();

        for (Field field : fields) {
            Object value;
            try {
                field.setAccessible(true);
                value = field.get(element);
            }
            catch (Exception exception) {
                value = null;
            }

            result.put(field.getName(), value);
        }

        return result;
    }

    public String getLineSuffix(String input) {
        String suffixSign = "[+]";

        if (input.contains(suffixSign)) {
            int lastIndexOf = input.lastIndexOf(suffixSign);

            return input.substring(lastIndexOf + suffixSign.length()).replace("_", " ");
        }

        return "";
    }
}
