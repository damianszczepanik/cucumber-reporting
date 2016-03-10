package net.masterthought.cucumber.generators;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.Charsets;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import net.masterthought.cucumber.ReportGenerator;
import net.masterthought.cucumber.ValidationException;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class Page extends ReportGenerator {

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
        return document.byClass("stats-table");
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

}
