package net.lyx.dbframework.core.compose.impl.pattern.type.collection;

import net.lyx.dbframework.core.compose.CombinedStructs;
import net.lyx.dbframework.core.compose.template.collection.MergesTemplate;
import net.lyx.dbframework.core.compose.template.completed.CompletedMerges;

import java.util.ArrayList;
import java.util.List;

public class MergesPattern implements MergesTemplate, CompletedMerges {

    private final List<CombinedStructs.CombinedMerge> fulls = new ArrayList<>();
    private final List<CombinedStructs.CombinedMerge> outsides = new ArrayList<>();
    private final List<CombinedStructs.CombinedMerge> additions = new ArrayList<>();
    private final List<CombinedStructs.CombinedMerge> inners = new ArrayList<>();
    private final List<CombinedStructs.CombinedMerge> unscoped = new ArrayList<>();

    @Override
    public CompletedMerges combine() {
        return this;
    }

    @Override
    public MergesTemplate full(CombinedStructs.CombinedMerge joins) {
        fulls.add(joins);
        return this;
    }

    @Override
    public MergesTemplate outside(CombinedStructs.CombinedMerge joins) {
        outsides.add(joins);
        return this;
    }

    @Override
    public MergesTemplate additional(CombinedStructs.CombinedMerge joins) {
        additions.add(joins);
        return this;
    }

    @Override
    public MergesTemplate inner(CombinedStructs.CombinedMerge joins) {
        inners.add(joins);
        return this;
    }

    @Override
    public MergesTemplate unscoped(CombinedStructs.CombinedMerge joins) {
        unscoped.add(joins);
        return this;
    }

    @Override
    public CombinedStructs.CombinedMerge[] fulls() {
        return fulls.toArray(new CombinedStructs.CombinedMerge[0]);
    }

    @Override
    public CombinedStructs.CombinedMerge[] outsides() {
        return outsides.toArray(new CombinedStructs.CombinedMerge[0]);
    }

    @Override
    public CombinedStructs.CombinedMerge[] additions() {
        return additions.toArray(new CombinedStructs.CombinedMerge[0]);
    }

    @Override
    public CombinedStructs.CombinedMerge[] inners() {
        return inners.toArray(new CombinedStructs.CombinedMerge[0]);
    }

    @Override
    public CombinedStructs.CombinedMerge[] unscoped() {
        return unscoped.toArray(new CombinedStructs.CombinedMerge[0]);
    }
}
