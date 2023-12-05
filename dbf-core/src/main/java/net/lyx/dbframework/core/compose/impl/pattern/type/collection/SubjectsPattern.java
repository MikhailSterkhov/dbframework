package net.lyx.dbframework.core.compose.impl.pattern.type.collection;

import net.lyx.dbframework.core.compose.CombinedStructs;
import net.lyx.dbframework.core.compose.template.collection.SubjectsTemplate;
import net.lyx.dbframework.core.compose.template.completed.CompletedSubjects;

import java.util.ArrayList;
import java.util.List;

public class SubjectsPattern implements SubjectsTemplate, CompletedSubjects {

    private final List<CombinedStructs.CombinedLabel> generals = new ArrayList<>();
    private final List<CombinedStructs.CombinedLabel> averages = new ArrayList<>();
    private final List<CombinedStructs.CombinedLabel> counts = new ArrayList<>();
    private final List<CombinedStructs.CombinedLabel> mines = new ArrayList<>();
    private final List<CombinedStructs.CombinedLabel> maxes = new ArrayList<>();
    private final List<CombinedStructs.CombinedLabel> summed = new ArrayList<>();

    private boolean isSelectedAll;

    @Override
    public CompletedSubjects combine() {
        return this;
    }

    @Override
    public SubjectsTemplate selectAll() {
        isSelectedAll = true;
        generals.add(CombinedStructs.CombinedLabel.builder().label("*").build());
        return this;
    }

    @Override
    public SubjectsTemplate select(CombinedStructs.CombinedLabel label) {
        generals.add(label);
        return this;
    }

    @Override
    public SubjectsTemplate average(CombinedStructs.CombinedLabel label) {
        averages.add(label);
        return this;
    }

    @Override
    public SubjectsTemplate count(CombinedStructs.CombinedLabel label) {
        counts.add(label);
        return this;
    }

    @Override
    public SubjectsTemplate min(CombinedStructs.CombinedLabel label) {
        mines.add(label);
        return this;
    }

    @Override
    public SubjectsTemplate max(CombinedStructs.CombinedLabel label) {
        maxes.add(label);
        return this;
    }

    @Override
    public SubjectsTemplate sum(CombinedStructs.CombinedLabel label) {
        summed.add(label);
        return this;
    }

    @Override
    public CombinedStructs.CombinedLabel[] generals() {
        return generals.toArray(new CombinedStructs.CombinedLabel[0]);
    }

    @Override
    public CombinedStructs.CombinedLabel[] averages() {
        return averages.toArray(new CombinedStructs.CombinedLabel[0]);
    }

    @Override
    public CombinedStructs.CombinedLabel[] counts() {
        return counts.toArray(new CombinedStructs.CombinedLabel[0]);
    }

    @Override
    public CombinedStructs.CombinedLabel[] mines() {
        return mines.toArray(new CombinedStructs.CombinedLabel[0]);
    }

    @Override
    public CombinedStructs.CombinedLabel[] maxes() {
        return maxes.toArray(new CombinedStructs.CombinedLabel[0]);
    }

    @Override
    public CombinedStructs.CombinedLabel[] summed() {
        return summed.toArray(new CombinedStructs.CombinedLabel[0]);
    }

    @Override
    public boolean isSelectedAll() {
        return isSelectedAll;
    }
}
