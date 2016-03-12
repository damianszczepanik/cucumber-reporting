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
public class Page extends ReportGenerator {

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

    protected ElementWrapper extractHeaderStatsTable(ElementWrapper document) {
        return getTableStats(document).bySelector("thead");
    }

    protected Elements extractBodyStatsTable(ElementWrapper document) {
        return getTableStats(document).bySelectors("tbody tr");
    }

    protected Elements extractFooterCellsInStatsTable(ElementWrapper document) {
        return getTableStats(document).bySelectors("tfoot tr td");
    }

    private ElementWrapper getTableStats(ElementWrapper document) {
        return getStatistics(document).byClass("stats-table");
    }

    protected Elements getEmptyReportMessage(ElementWrapper document) {
        return getStatistics(document).bySelectors("div div p");
    }

    protected ElementWrapper getStatistics(ElementWrapper document) {
        return document.byId("statistics");
    }

    protected Elements getRows(ElementWrapper statsTable) {
        return statsTable.bySelectors("tr");
    }

    protected Elements getHeaderCells(Element row) {
        return new ElementWrapper(row).bySelectors("th");
    }

    protected Elements getCells(Element row) {
        return new ElementWrapper(row).bySelectors("td");
    }

    protected void validateElements(Elements array, String... values) {
        if (array.size() != values.length) {
            throw new IllegalArgumentException(
                    String.format("Found %d elements but expected to compare with %d.", array.size(), values.length));
        }

        for (int i = 0; i < values.length; i++) {
            if (!array.get(i).text().equals(values[i])) {
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
        validateLink(new ElementWrapper(row.get(0)).bySelector("a").getElement(), href, name);
    }

    protected void validateLink(Element link, String href, String name) {
        assertThat(link.text()).isEqualTo(name);
        assertThat(link.attr("href")).isEqualTo(href);
    }
}
