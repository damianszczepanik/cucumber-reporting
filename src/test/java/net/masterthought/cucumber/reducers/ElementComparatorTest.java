package net.masterthought.cucumber.reducers;

import net.masterthought.cucumber.ReportGenerator;
import net.masterthought.cucumber.json.Element;
import net.masterthought.cucumber.json.Feature;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ElementComparatorTest extends ReportGenerator {

    @Test
    public void compare() {
        // given
        setUpWithJson(SAMPLE_JSON);
        List<Element> elements = Arrays.stream(getFeatureByName("Second feature").getElements())
                .filter(Element::isScenario)
                .collect(Collectors.toList());

        // when
        // then
        assertThat(elements.get(0)).usingComparator(new ElementComparator()).isEqualTo(elements.get(0));
        assertThat(elements.get(0)).usingComparator(new ElementComparator()).isNotEqualTo(elements.get(1));
        assertThat(elements.get(0)).usingComparator(new ElementComparator()).isNotNull();
    }

    private Feature getFeatureByName(String name) {
        return reportResult.getAllFeatures()
                .stream()
                .filter(f -> name.equals(f.getName()))
                .findFirst()
                .orElse(null);
    }

    @Test
    public void compare_backgrounds() {
        // given
        setUpWithJson(SAMPLE_JSON);
        List<Element> elements = Arrays.stream(getFeatureByName("1st feature").getElements())
                .filter(Element::isBackground)
                .collect(Collectors.toList());

        // when
        // then
        assertThat(new ElementComparator().compare(elements.get(0), elements.get(0))).isZero();
    }
}
