package net.masterthought.cucumber;

import net.masterthought.cucumber.json.Element;
import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.json.Step;
import org.assertj.core.groups.Tuple;
import org.assertj.core.internal.StandardComparisonStrategy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Simple comparator for features to compare it somehow.
 */
public class ReportResultSimpleFeatureComparator implements Comparator<Feature> {

    @Override
    public int compare(Feature feature1, Feature feature2) {
        return StandardComparisonStrategy.instance()
                .areEqual(buildTupleByFeature(feature1), buildTupleByFeature(feature2)) ? 0 : -1;
    }

    private Tuple buildTupleByFeature(Feature feature) {
        List<Object> values = new ArrayList<>();
        values.add(feature.getUri());
        values.add(feature.getLine());
        values.add(feature.getId());
        values.add(feature.getName());

        for (Element e : feature.getElements()) {
            values.add(e.getLine());
            values.add(e.getType());
            values.add(e.getId());
            values.add(e.getName());

            for (Step s : e.getSteps()) {
                values.add(s.getLine());
                values.add(s.getName());
                values.add(s.getResult().getStatus());
            }
        }
        return new Tuple(values);
    }
}
