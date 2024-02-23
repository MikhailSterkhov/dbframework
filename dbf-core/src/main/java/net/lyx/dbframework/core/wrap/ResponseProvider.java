package net.lyx.dbframework.core.wrap;

import lombok.*;
import net.lyx.dbframework.core.Field;
import net.lyx.dbframework.core.ResponseRow;
import net.lyx.dbframework.core.ResponseStream;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ResponseProvider {

    private final ResultWrapper result;

    @Getter
    private final ResponseStream handle;

    public ResponseProvider(ResultWrapper result) {
        this.result = result;
        this.handle = createResponseImpl();
    }

    @SneakyThrows
    private ResponseStream createResponseImpl() {
        LinkedList<ResponseRow> responseRows = new LinkedList<>();

        ResultSet resultSet = result.getResult();
        ResultSetMetaData metaData = resultSet.getMetaData();

        int columnCount = metaData.getColumnCount();

        while (resultSet.next()) {
            Map<FieldID, Field> fieldByIdMap = new LinkedHashMap<>();

            for (int index = 1; index <= columnCount; index++) {
                FieldID fieldID = FieldID.builder()
                        .index(index - 1)
                        .label(metaData.getColumnLabel(index))
                        .build();

                String valueAsString;
                Object valueAsObject;
                try {
                    valueAsString = resultSet.getString(index);
                    valueAsObject = resultSet.getObject(index);
                } catch (SQLException exception) {
                    valueAsString = null;
                    valueAsObject = null;
                }

                fieldByIdMap.put(fieldID, new FieldImpl(fieldID, valueAsString, valueAsObject));
            }

            responseRows.add(new RowImpl(fieldByIdMap));
        }

        result.getStatement().close();
        resultSet.close();

        return new ResponseStreamImpl(responseRows);
    }

    @ToString
    @EqualsAndHashCode
    @Builder
    private static class FieldID {

        private final int index;
        private final String label;
    }

    @ToString(onlyExplicitlyIncluded = true)
    @RequiredArgsConstructor
    private static class FieldImpl implements Field {

        private final FieldID id;
        private final String valueAsString;

        @ToString.Include
        private final Object valueAsObject;

        @Override
        public int index() {
            return id.index;
        }

        @Override
        public String label() {
            return id.label;
        }

        @Override
        public String getAsString() {
            return valueAsString;
        }

        @Override
        public Timestamp getAsTimestamp() {
            return Timestamp.valueOf(valueAsString);
        }

        @Override
        public Integer getAsInt() {
            return valueAsString == null ? 0 : Integer.parseInt(getAsString());
        }

        @Override
        public Double getAsDouble() {
            return valueAsString == null ? 0 : Double.parseDouble(getAsString());
        }

        @Override
        public Float getAsFloat() {
            return valueAsString == null ? 0 : Float.parseFloat(getAsString());
        }

        @Override
        public Long getAsLong() {
            return valueAsString == null ? 0 : Long.parseLong(getAsString());
        }

        @Override
        public Boolean getAsBoolean() {
            return valueAsString != null && Boolean.parseBoolean(getAsString());
        }

        @Override
        public Object getAsObject() {
            return valueAsObject;
        }
    }

    @RequiredArgsConstructor
    private class RowImpl implements ResponseRow {

        private final Map<FieldID, Field> map;

        @Override
        public int length() {
            return map.size();
        }

        private FieldID findID(int index) {
            return map.keySet()
                    .stream()
                    .filter(id -> id.index == index)
                    .findFirst()
                    .orElse(null);
        }

        private FieldID findID(String label) {
            return map.keySet()
                    .stream()
                    .filter(id -> id.label.equalsIgnoreCase(label))
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public Field field(int index) {
            return map.get(findID(index));
        }

        @Override
        public Field field(String label) {
            return map.get(findID(label));
        }

        @Override
        public Field[] fields() {
            return map.values().toArray(new Field[0]);
        }

        @Override
        public boolean isNull(int index) {
            return findID(index) == null || field(index).getAsString() == null;
        }

        @Override
        public boolean isNull(String label) {
            return findID(label) == null || field(label).getAsString() == null;
        }

        @Override
        public String toString() {
            return new ResponseStreamImpl(new LinkedList<>(Collections.singletonList(this))).toString();
        }
    }

    @RequiredArgsConstructor
    private class ResponseStreamImpl implements ResponseStream {

        private final LinkedList<ResponseRow> responseRowList;

        @Override
        public long count() {
            return result.getAffectedRows();
        }

        @Override
        public ResponseRow findFirst() {
            return responseRowList.getFirst();
        }

        @Override
        public ResponseRow findLast() {
            return responseRowList.getLast();
        }

        @Override
        public ResponseRow find(int index) {
            return responseRowList.get(index - 1);
        }

        @Override
        public <R> Stream<R> map(Function<ResponseRow, R> function) {
            return responseRowList.stream().map(function);
        }

        @Override
        public ResponseStream limit(long limit) {
            responseRowList.subList(0, (int) limit);
            return this;
        }

        @Override
        public ResponseStream filter(Predicate<ResponseRow> predicate) {
            final LinkedList<ResponseRow> buf = responseRowList.stream()
                    .filter(predicate)
                    .collect(Collectors.toCollection(LinkedList::new));

            responseRowList.clear();
            responseRowList.addAll(buf);

            return this;
        }

        @Override
        public boolean anyMatch(Predicate<ResponseRow> predicate) {
            return responseRowList.stream().anyMatch(predicate);
        }

        @Override
        public boolean allMatch(Predicate<ResponseRow> predicate) {
            return responseRowList.stream().allMatch(predicate);
        }

        @Override
        public boolean noneMatch(Predicate<ResponseRow> predicate) {
            return responseRowList.stream().noneMatch(predicate);
        }

        @Override
        public void forEach(Consumer<? super ResponseRow> consumer) {
            responseRowList.forEach(consumer);
        }

        @Override
        public String toString() {
            return dump(responseRowList);
        }

        @NotNull
        @Override
        public Iterator<ResponseRow> iterator() {
            return responseRowList.iterator();
        }
    }

    private static String dump(List<ResponseRow> responseRowList) {
        StringBuilder tableDump = new StringBuilder();

        List<String> keys = responseRowList.stream()
                .flatMap(responseRow -> Stream.of(responseRow.fields()))
                .map(Field::label)
                .collect(Collectors.toList());

        if (keys.isEmpty()) {
            keys.add("<EMPTY KEY>");
        }

        tableDump.append("+");
        for (int i = 0; i < keys.size(); i++) {
            tableDump.append("--------------------+");
        }
        tableDump.append("\n");

        for (String key : keys) {
            tableDump.append(String.format("| %-18s ", key));
        }
        tableDump.append("|\n");

        tableDump.append("+");
        for (int i = 0; i < keys.size(); i++) {
            tableDump.append("--------------------+");
        }
        tableDump.append("\n");

        int numRows = Math.max(1, responseRowList.size());

        for (int row = 0; row < numRows; row++) {
            for (String key : keys) {
                List<String> values = responseRowList.stream()
                        .map(responseRow -> responseRow.field(key))
                        .map(Field::getAsString)
                        .collect(Collectors.toList());

                if (values.isEmpty()) {
                    values.add("<EMPTY VALUE>");
                }

                if (row < values.size()) {
                    tableDump.append(String.format("| %-18s ", values.get(row)));
                } else {
                    tableDump.append("|                    ");
                }
            }
            tableDump.append("|\n");
        }

        tableDump.append("+");
        for (int i = 0; i < keys.size(); i++) {
            tableDump.append("--------------------+");
        }

        return tableDump.toString();
    }
}
