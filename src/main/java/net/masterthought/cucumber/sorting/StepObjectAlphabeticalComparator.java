package net.masterthought.cucumber.sorting;

import net.masterthought.cucumber.json.support.StepObject;

import java.util.Comparator;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class StepObjectAlphabeticalComparator implements Comparator<StepObject> {

    @Override
    public int compare(StepObject stepObject1, StepObject stepObject2) {
        // since there might be the only one StepObject with given location, compare by location only
        return Integer.signum(stepObject1.getLocation().compareTo(stepObject2.getLocation()));
    }
}
