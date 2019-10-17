package net.masterthought.cucumber.reducers;

import net.masterthought.cucumber.json.Element;
import org.apache.commons.lang.StringUtils;

import java.util.Comparator;

/**
 * Compares two elements and shows if they have the same Id for scenario type
 * or they are on the same line if it's a background.
 */
class ElementComparator implements Comparator<Element> {

    /**
     * @return comparison result of Ids or line numbers if elements have the same type
     * or -1 if type of elements is different.
     */
    @Override
    public int compare(Element element1, Element element2) {
        if (hasSameType(element1, element2)) {
            if (element1.isScenario()) {
                return Comparator.nullsFirst(String::compareToIgnoreCase)
                        .compare(element1.getId(), element2.getId());
            }
            /*
             * Compares non-scenario elements, like Background.
             */
            return Comparator.nullsFirst(Integer::compare)
                    .compare(element1.getLine(), element2.getLine());
        }
        return -1;
    }

    private boolean hasSameType(Element element1, Element element2) {
        return element1 != null && element2 != null &&
                StringUtils.equalsIgnoreCase(element1.getType(), element2.getType());
    }
}
