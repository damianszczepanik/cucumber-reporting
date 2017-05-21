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

    private final SoringMethod soringMethod;

    public SortingFactory(SoringMethod soringMethod) {
        this.soringMethod = soringMethod;
    }

    public List<Feature> sortFeatures(Collection<Feature> features) {
        switch (soringMethod) {
            case NATURAL:
                return new ArrayList(features);
            case ALPHABETICAL:
                return toSortedList(features, new FeaturesAlphabeticalComparator());
            default:
                throw createUnknownMethodException(soringMethod);
        }
    }

    public List<TagObject> sortTags(Collection<TagObject> tags) {
        switch (soringMethod) {
            case NATURAL:
                return new ArrayList(tags);
            case ALPHABETICAL:
                return toSortedList(tags, new TagObjectAlphabeticalComparator());
            default:
                throw createUnknownMethodException(soringMethod);
        }
    }

    public List<StepObject> sortSteps(Collection<StepObject> steps) {
        switch (soringMethod) {
            case NATURAL:
                return new ArrayList(steps);
            case ALPHABETICAL:
                return toSortedList(steps, new StepObjectAlphabeticalComparator());
            default:
                throw createUnknownMethodException(soringMethod);
        }
    }

    private static <T> List<T> toSortedList(Collection<T> values, Comparator<T> comparator) {
        List<T> list = new ArrayList<>(values);
        Collections.sort(list, comparator);
        return list;
    }

    private RuntimeException createUnknownMethodException(SoringMethod soringMethod) {
        return new IllegalArgumentException("Unsupported sorting method: " + soringMethod);
    }
}
