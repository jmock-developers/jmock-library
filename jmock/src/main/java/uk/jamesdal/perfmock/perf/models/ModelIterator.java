package uk.jamesdal.perfmock.perf.models;

import uk.jamesdal.perfmock.perf.PerfModel;

import java.util.Arrays;
import java.util.List;

public class ModelIterator extends PerfModel {

    public final List<PerfModel> modelList;

    public int index;

    public ModelIterator(PerfModel... models) {
        this.modelList = Arrays.asList(models);
        index = 0;
    }

    @Override
    public double sample() {
        assert index < modelList.size();
        index++;
        return modelList.get(index - 1).sample();
    }
}
