package net.masterthought.cucumber.sorting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.json.support.StepObject;
import net.masterthought.cucumber.json.support.TagObject;

/**
 * Keeps references to classes that sort results.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public final class SortingFactory {

    private final SortingMethod sortingMethod;

    public SortingFactory(SortingMethod sortingMethod) {
        this.sortingMethod = sortingMethod;
    }

    public List<Feature> sortFeatures(Collection<Feature> features) {
        if (sortingMethod == SortingMethod.NATURAL) {
           return new ArrayList<>(features);
        }
        return toSortedList(features, new FeaturesAlphabeticalComparator());
    }

    public List<TagObject> sortTags(Collection<TagObject> tags) {
        if (sortingMethod == SortingMethod.NATURAL) {
           return new ArrayList<>(tags);
        }
        return toSortedList(tags, new TagObjectAlphabeticalComparator());
    }

    public List<StepObject> sortSteps(Collection<StepObject> steps) {
        if (sortingMethod == SortingMethod.NATURAL) {
            return new ArrayList<>(steps);
        }
        return toSortedList(steps, new StepObjectAlphabeticalComparator());
    }

    private static <T> List<T> toSortedList(Collection<T> values, Comparator<T> comparator) {
        List<T> list = new ArrayList<>(values);
        Collections.sort(list, comparator);
        return list;
    }

}
