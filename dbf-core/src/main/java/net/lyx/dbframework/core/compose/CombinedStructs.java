package net.lyx.dbframework.core.compose;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CombinedStructs {

    public CombinedMerge merged(String table, String labelFrom, String labelTo) {
        return CombinedMerge.builder()
                .table(table)
                .labelFrom(labelFrom)
                .labelTo(labelTo)
                .build();
    }

    public CombinedMerge merged(String table, String generalLabel) {
        return CombinedMerge.builder()
                .table(table)
                .labelFrom(generalLabel)
                .labelTo(generalLabel)
                .build();
    }

    public CombinedLabel label(String label) {
        return CombinedLabel.builder()
                .label(label)
                .build();
    }

    public CombinedLabel label(String label, String castTo) {
        return CombinedLabel.builder()
                .label(label)
                .castTo(castTo)
                .build();
    }

    public CombinedRenaming renaming(String name, String renameTo) {
        return CombinedRenaming.builder()
                .name(name)
                .renameTo(renameTo)
                .build();
    }

    public CombinedField field(String label, Object value) {
        return CombinedField.builder()
                .label(label)
                .value(value)
                .build();
    }

    public CombinedField fieldNullable(String label) {
        return CombinedField.builder()
                .label(label)
                .build();
    }

    public CombinedOrderedLabel orderedLabel(OrderDirection direction, String label) {
        return CombinedOrderedLabel.builder()
                .direction(direction)
                .label(label)
                .build();
    }

    public CombinedOrderedLabel orderedAlphabetic(String label) {
        return CombinedOrderedLabel.builder()
                .direction(OrderDirection.ASCENDING)
                .label(label)
                .build();
    }

    public CombinedStyledParameter styledParameter(String name, ParameterStyle style) {
        return CombinedStyledParameter.builder()
                .name(name)
                .style(style)
                .build();
    }

    @Getter
    @ToString
    @Builder(toBuilder = true)
    public static class CombinedMerge {

        String table;
        String labelFrom;
        String labelTo;
    }

    @Getter
    @ToString
    @Builder(toBuilder = true)
    public static class CombinedRenaming {

        String name;
        String renameTo;
    }

    @Getter
    @ToString
    @Builder(toBuilder = true)
    public static class CombinedLabel {

        String label;
        String castTo;
    }

    @Getter
    @ToString
    @Builder(toBuilder = true)
    public static class CombinedField {

        String label;
        Object value;
    }

    @Getter
    @ToString
    @Builder(toBuilder = true)
    public static class CombinedOrderedLabel {

        OrderDirection direction;
        String label;
    }

    @Getter
    @ToString
    @Builder(toBuilder = true)
    public static class CombinedStyledParameter {

        String name;
        ParameterStyle style;
    }
}
