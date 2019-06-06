package net.masterthought.cucumber.sorting;

import java.util.Comparator;

import org.apache.commons.lang3.ObjectUtils;

import net.masterthought.cucumber.json.Feature;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class FeaturesAlphabeticalComparator implements Comparator<Feature> {

    @Override
    public int compare(Feature feature1, Feature feature2) {
        // order by the name so first compare by the name
        int nameCompare = ObjectUtils.compare(feature1.getName(), feature2.getName());
        if (nameCompare != 0) {
            return nameCompare;
        }

        // if names are the same, compare by the ID which should be unieque by JSON file
        int idCompare = ObjectUtils.compare(feature1.getId(), feature2.getId());
        if (idCompare != 0) {
            return idCompare;
        }

        // if ids are the same it means that feature exists in more than one JSON file so compare by JSON report
        return ObjectUtils.compare(feature1.getReportFileName(), feature2.getReportFileName());
    }
}
