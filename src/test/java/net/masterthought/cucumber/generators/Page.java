package net.masterthought.cucumber.generators;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.Charsets;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.junit.After;

import net.masterthought.cucumber.ReportGenerator;
import net.masterthought.cucumber.ValidationException;
import net.masterthought.cucumber.json.Hook;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public abstract class Page extends ReportGenerator {

    protected static final String SAMPLE_JOSN = "sample.json";
    protected static final String EMPTY_JOSN = "empty.json";

    protected AbstractPage page;

    @After
    public void cleanUp() {
        // delete report file if was already created by any of test
        if (page != null) {
            File report = new File(configuration.getReportDirectory(), page.getWebPage());
            report.delete();
            page = null;
        }
    }

    protected ElementWrapper documentFrom(String pageName) {
        File input = new File(configuration.getReportDirectory(), pageName);
        try {
            return new ElementWrapper(Jsoup.parse(input, Charsets.UTF_8.name(), StringUtils.EMPTY));
        } catch (IOException e) {
            throw new ValidationException(e);
        }
    }


    // ================= <TITLE>
    protected ElementWrapper getTitle(ElementWrapper document) {
        return document.oneBySelector("head").oneBySelector("title");
    }

    // ================= class=report-lead
    protected ElementWrapper getLeadHeader(ElementWrapper document) {
        return getLead(document).oneBySelector("h2");
    }

    protected ElementWrapper getLeadDescription(ElementWrapper document) {
        return getLead(document).oneBySelector("p");
    }

    private ElementWrapper getLead(ElementWrapper document) {
        return document.byId("report-lead");
    }


    // ================= class=stats-table
    protected ElementWrapper getHeaderOfStatsTable(ElementWrapper document) {
        return getTableStats(document).oneBySelector("thead");
    }

    protected ElementWrapper[] getBodyOfStatsTable(ElementWrapper document) {
        return getTableStats(document).allBySelector("tbody tr");
    }

    protected ElementWrapper[] getFooterCellsOfStatsTable(ElementWrapper document) {
        return getTableStats(document).allBySelector("tfoot tr td");
    }

    protected ElementWrapper getEmptyReportMessage(ElementWrapper document) {
        return getSummary(document).oneBySelector("p");
    }

    private ElementWrapper getTableStats(ElementWrapper document) {
        return getSummary(document).oneByClass("stats-table");
    }

    private ElementWrapper getSummary(ElementWrapper document) {
        return document.byId("summary");
    }


    // ================= id=report
    protected ElementWrapper getReport(ElementWrapper document) {
        return document.byId("report");
    }

    protected ElementWrapper[] getTags(ElementWrapper report) {
        return report.childByClass("tags").allBySelector("a");
    }

    protected ElementWrapper getBrief(ElementWrapper element) {
        return element.childByClass("brief");
    }

    protected ElementWrapper[] getBefore(ElementWrapper element) {
        return getHooks(element.childByClass("hooks-before"));
    }

    protected ElementWrapper[] getAfter(ElementWrapper element) {
        return getHooks(element.childByClass("hooks-after"));
    }

    private ElementWrapper[] getHooks(ElementWrapper hooks) {
        return hooks.allByClass("hook");
    }

    protected ElementWrapper getDescription(ElementWrapper report) {
        return report.childByClass("description");
    }

    protected ElementWrapper getFeatureDetails(ElementWrapper element) {
        return element.oneByClass("feature-details");
    }

    protected ElementWrapper[] getElements(ElementWrapper document) {
        return getReport(document).allByClass("element");
    }


    // ================= <TABLE>
    protected ElementWrapper[] getRows(ElementWrapper statsTable) {
        return statsTable.allBySelector("tr");
    }

    protected ElementWrapper[] getHeaderCells(ElementWrapper row) {
        return row.allBySelector("th");
    }

    protected ElementWrapper[] getCells(ElementWrapper row) {
        return row.allBySelector("td");
    }


    // ================= others
    /**
     * Validates if element of passed array is equal (as a text) to passed values (reference).
     * 
     * @param array
     *            elements that will be compared
     * @param values
     *            reference element to compare with
     */
    protected void validateElements(ElementWrapper[] array, String... values) {
        if (array.length != values.length) {
            throw new IllegalArgumentException(
                    String.format("Found %d elements but expected to compare with %d.", array.length, values.length));
        }

        for (int i = 0; i < values.length; i++) {
            String html2Text = array[i].text();
            if (!html2Text.equals(values[i])) {
                throw new IllegalArgumentException(
                        String.format("On index %d found '%s' while expected '%s'", i, array[i].text(), values[i]));
            }
        }
    }

    protected void validateCSSClasses(ElementWrapper[] array, String... classes) {
        if (array.length != classes.length) {
            throw new IllegalArgumentException(
                    String.format("Found %d elements but expected to compare with %d.", array.length, classes.length));
        }

        for (int i = 0; i < classes.length; i++) {
            if (StringUtils.isEmpty(classes[i])) {
                if (!array[i].classNames().isEmpty()) {
                    throw new IllegalArgumentException(String.format(
                            "On index %d found '%s' while expected none css class", i, array[i].classNames()));
                }
            } else if (!array[i].classNames().contains(classes[i])) {
                throw new IllegalArgumentException(String.format("On index %d found '%s' while expected '%s'", i,
                        array[i].classNames(), classes[i]));
            }
        }
    }

    protected void validateReportLink(ElementWrapper[] cells, String href, String name) {
        // link is available in first cell of the row
        ElementWrapper link = cells[0];
        validateLink(link.oneBySelector("a"), href, name);
    }

    protected void validateLink(ElementWrapper link, String href, String name) {
        assertThat(link.text()).isEqualTo(name);
        assertThat(link.attr("href")).isEqualTo(href);
    }

    protected void validateBrief(ElementWrapper brief, String keyword, String name) {
        validateFullBrief(brief, keyword, name, null);
    }

    protected void validateFullBrief(ElementWrapper brief, String keyword, String name, String duration) {
        assertThat(brief.oneByClass("keyword").text()).isEqualTo(keyword);
        assertThat(brief.oneByClass("name").text()).isEqualTo(name);
        if (duration != null) {
            assertThat(brief.oneByClass("duration").text()).isEqualTo(duration);
        }
    }

    protected void validateHook(ElementWrapper[] elements, Hook[] hooks, String hookName) {
        assertThat(elements).hasSameSizeAs(hooks);
        for (int i = 0; i < elements.length; i++) {
            ElementWrapper brief = elements[i].oneByClass("brief");
            assertThat(brief.classNames()).contains(hooks[i].getStatus().getRawName());
            assertThat(elements[i].oneByClass("keyword").text()).isEqualTo(hookName);
            if (hooks[i].getMatch() != null) {
                assertThat(elements[i].oneByClass("name").text()).isEqualTo(hooks[i].getMatch().getLocation());
            }
            if (hooks[i].getResult() != null) {
                assertThat(elements[i].oneByClass("duration").text())
                        .isEqualTo(hooks[i].getResult().getFormatedDuration());
                if (hooks[i].getResult().getErrorMessage() != null) {
                    assertThat(elements[i].oneByClass("message").text())
                            .contains(hooks[i].getResult().getErrorMessage());
                }
            }
        }
    }
}
