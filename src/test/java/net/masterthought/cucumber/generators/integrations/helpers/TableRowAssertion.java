package net.masterthought.cucumber.generators.integrations.helpers;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.commons.lang.StringUtils;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
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

    public String[] getCellsHtml() {
        WebAssertion[] cells = getCells();
        String[] values = new String[cells.length];
        for (int i = 0; i < cells.length; i++) {
            values[i] = cells[i].html();
        }
        return values;
    }


    /**
     * Validates the row cells' text match the given passed values.
     *
     * @param values reference element to compare with
     */
    public void hasExactValues(String... values) {
        WebAssertion[] array = allBySelector("td,th", WebAssertion.class);

        assertThat(array).hasSameSizeAs(values);

        for (int i = 0; i < values.length; i++) {
            assertThat(array[i].text()).describedAs("Invalid value at index %d", i).isEqualTo(values[i]);
        }
    }

    /**
     * Validates the row cells' class names contain the given values.
     *
     * @param classes reference element to compare with
     */
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

    /**
     * Validates the row cells' data-value attribute values match the given values.
     *
     * @param values reference element to compare with
     */
    public void hasExactDataValues(String... values) {
        WebAssertion[] array = allBySelector("td,th", WebAssertion.class);

        assertThat(array).hasSameSizeAs(values);

        for (int i = 0; i < values.length; i++) {
            if (StringUtils.isEmpty(values[i])) {
                assertThat(array[i].attr("data-value")).describedAs("Unexpected data-value on index %d", i).isEmpty();
            } else {
                assertThat(array[i].attr("data-value")).describedAs("Missing data-value on index %d", i).isEqualTo(values[i]);
            }
        }
    }
}
