package net.masterthought.cucumber.reducers;

import net.masterthought.cucumber.json.Element;
import org.apache.commons.lang.StringUtils;

import java.util.Comparator;

/**
 * Compares scenarios and shows if they have the same Id
 * or holds on the same line if it's a background.
 */
class ElementComparator implements Comparator<Element> {

    @Override
    public int compare(Element e1, Element e2) {
        if (e1 != null && e2 != null && StringUtils.equalsIgnoreCase(e1.getType(), e2.getType())) {
            if (e1.isScenario()) {
                return Comparator.nullsFirst(String::compareToIgnoreCase)
                        .compare(e1.getId(), e2.getId());
            }
            return Comparator.nullsFirst(Integer::compare)
                    .compare(e1.getLine(), e2.getLine());
        }
        return -1;
    }
}
