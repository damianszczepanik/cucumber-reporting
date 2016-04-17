package net.masterthought.cucumber.generators.helpers;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.commons.lang.StringUtils;

public class TableRowAssertion extends ReportAssertion {

    public LinkAssertion getReportLink() {
        return super.getLink();
    }

    protected WebAssertion[] getCells() {
        return allBySelector("td,th", WebAssertion.class);
    }

    public String[] getCellsValues() {
        WebAssertion[] cells = getCells();
        String[] values = new String[cells.length];
        for (int i = 0; i < cells.length; i++) {
            values[i] = cells[i].text();
        }
        return values;
    }

    /**
     * Validates if element of passed array is equal (as a text) to passed values (reference).
     * 
     * @param array
     *            elements that will be compared
     * @param values
     *            reference element to compare with
     */
    public void hasExactValues(String... values) {
        WebAssertion[] array = allBySelector("td,th", WebAssertion.class);

        assertThat(array.length).isEqualTo(values.length);

        for (int i = 0; i < values.length; i++) {
            assertThat(array[i].text()).describedAs("Invalid value at index %d", i).isEqualTo(values[i]);
        }
    }

    public void hasExactCSSClasses(String... classes) {
        WebAssertion[] array = allBySelector("td,th", WebAssertion.class);

        assertThat(array.length).isEqualTo(classes.length);

        for (int i = 0; i < classes.length; i++) {
            if (StringUtils.isEmpty(classes[i])) {
                assertThat(array[i].classNames()).describedAs("Unexpected CSC class on index %d", i).isEmpty();
            } else {
                assertThat(array[i].classNames()).describedAs("Missing CSC class on index %d", i).contains(classes[i]);
            }
        }
    }
}
