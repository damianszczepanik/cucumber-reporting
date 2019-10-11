package net.masterthought.cucumber.reducers;

import net.masterthought.cucumber.json.Element;
import net.masterthought.cucumber.json.Feature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class ReportFeatureReplaceableMerger implements ReportFeatureMerger {

    private static final ElementComparator ELEMENT_COMPARATOR = new ElementComparator();

    @Override
    public List<Feature> merge(List<Feature> features) {
        Map<String, Feature> mergedFeatures = new HashMap<>();
        for (Feature feature : features) {
            Feature mergedFeature = mergedFeatures.get(feature.getId());
            if (mergedFeature == null) {
                mergedFeatures.put(feature.getId(), feature);
            }
            else {
                updateElements(mergedFeatures.get(feature.getId()), feature.getElements());
            }
        }
        return new ArrayList<>(mergedFeatures.values());
    }

    private void updateElements(Feature feature, Element[] elements) {
        for (int i = 0; i < elements.length; i++) {
            Element current = elements[i];
            if (current.isScenario()) {
                int index = find(feature.getElements(), current);
                boolean hasBackground = isBackground(i - 1, elements);

                if (index < 0) {
                    feature.addElements(
                            hasBackground ?
                                    new Element[] {elements[i - 1], current} :
                                    new Element[] {current}
                            );
                }
                else {
                    feature.getElements()[index] = current;
                    if (hasBackground && isBackground(index - 1, feature.getElements())) {
                        feature.getElements()[index - 1] = elements[i - 1];
                    }
                }
            }
        }
    }

    private boolean isBackground(int elementInd, Element[] elements) {
        return elementInd >= 0 &&
                elements != null &&
                elementInd < elements.length &&
                elements[elementInd].isBackground();
    }

    private int find(Element[] elements, Element target) {
        for (int i = 0; i < elements.length; i++) {
            if (ELEMENT_COMPARATOR.compare(elements[i], target) == 0) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean test(List<ReducingMethod> reducingMethods) {
        return reducingMethods != null && reducingMethods.contains(ReducingMethod.MERGE_FEATURES_BY_ID);
    }
}
