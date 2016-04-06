package net.masterthought.cucumber.generators;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.Charsets;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.After;

import net.masterthought.cucumber.ReportGenerator;
import net.masterthought.cucumber.ValidationException;

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

    // ================= id=lead
    protected ElementWrapper getLeadHeader(ElementWrapper document) {
        return getLead(document).oneBySelector("h2");
    }

    protected ElementWrapper getLeadDescription(ElementWrapper document) {
        return getLead(document).oneBySelector("p");
    }

    private ElementWrapper getLead(ElementWrapper document) {
        return document.byId("lead");
    }


    // ================= class=stats-table
    protected ElementWrapper getHeaderOfStatsTable(ElementWrapper document) {
        return getTableStats(document).oneBySelector("thead");
    }

    protected Elements getBodyOfStatsTable(ElementWrapper document) {
        return getTableStats(document).allBySelector("tbody tr");
    }

    protected Elements getFooterCellsOfStatsTable(ElementWrapper document) {
        return getTableStats(document).allBySelector("tfoot tr td");
    }

    protected Elements getEmptyReportMessage(ElementWrapper document) {
        return getSummary(document).allBySelector("div p");
    }

    protected ElementWrapper getTableStats(ElementWrapper document) {
        return getSummary(document).oneByClass("stats-table");
    }

    private ElementWrapper getSummary(ElementWrapper document) {
        return document.byId("summary");
    }


    // ================= id=report

    protected ElementWrapper getReport(ElementWrapper document) {
        return document.byId("report");
    }

    protected Elements getFeatureTags(ElementWrapper report) {
        return report.oneByClass("tags").getElement().children();
    }

    protected ElementWrapper getFeatureDescription(ElementWrapper report) {
        return getFeatureDetails(report).oneByClass("description");
    }

    protected ElementWrapper getFeatureDetails(ElementWrapper element) {
        return element.oneByClass("feature-details");
    }

    protected Elements getScenarios(ElementWrapper document) {
        return getReport(document).allByClass("element");
    }

    protected Element getFeatureKeyword(ElementWrapper document) {
        return getReport(document).oneByClass("keyword-feature").getElement();
    }

    protected Element getElementKeyword(ElementWrapper element) {
        return element.oneByClass("keyword-element").getElement();
    }

    // ================= <TABLE>
    protected Elements getRows(ElementWrapper statsTable) {
        return statsTable.allBySelector("tr");
    }

    protected Elements getHeaderCells(Element row) {
        return new ElementWrapper(row).allBySelector("th");
    }

    protected Elements getCells(Element row) {
        return new ElementWrapper(row).allBySelector("td");
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
    protected void validateElements(Elements array, String... values) {
        if (array.size() != values.length) {
            throw new IllegalArgumentException(
                    String.format("Found %d elements but expected to compare with %d.", array.size(), values.length));
        }

        for (int i = 0; i < values.length; i++) {
            String html2Text = array.get(i).text();
            if (!html2Text.equals(values[i])) {
                throw new IllegalArgumentException(
                        String.format("On index %d found '%s' while expected '%s'", i, array.get(i).text(), values[i]));
            }
        }
    }

    protected void validateCSSClasses(Elements array, String... classes) {
        if (array.size() != classes.length) {
            throw new IllegalArgumentException(
                    String.format("Found %d elements but expected to compare with %d.", array.size(), classes.length));
        }

        for (int i = 0; i < classes.length; i++) {
            if (StringUtils.isEmpty(classes[i])) {
                if (!array.get(i).classNames().isEmpty()) {
                    throw new IllegalArgumentException(String.format(
                            "On index %d found '%s' while expected none css class", i, array.get(i).className()));
                }
            } else if (!array.get(i).classNames().contains(classes[i])) {
                throw new IllegalArgumentException(String.format("On index %d found '%s' while expected '%s'", i,
                        array.get(i).className(), classes[i]));
            }
        }
    }

    protected void validateReportLink(Elements row, String href, String name) {
        validateLink(new ElementWrapper(row.get(0)).oneBySelector("a").getElement(), href, name);
    }

    protected void validateLink(Element link, String href, String name) {
        assertThat(link.text()).isEqualTo(name);
        assertThat(link.attr("href")).isEqualTo(href);
    }

    protected void validateElementKeyword(ElementWrapper htmlElement,
            net.masterthought.cucumber.json.Element jsonElement) {
        String firstKeyword = getElementKeyword(htmlElement).text();
        assertThat(firstKeyword).isEqualTo(jsonElement.getKeyword() + ": " + jsonElement.getName());
    }
}
