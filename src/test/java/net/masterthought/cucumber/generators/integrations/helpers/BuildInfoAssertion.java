package net.masterthought.cucumber.generators.integrations.helpers;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class BuildInfoAssertion extends TableAssertion {

    public String getProjectName() {
        WebAssertion[] cells = getBodyRow().getCells();
        assertThat(cells).isNotEmpty();
        return cells[0].text();
    }

    public String getBuildNumber() {
        WebAssertion[] cells = getBodyRow().getCells();
        assertThat(cells.length).isGreaterThan(1);
        return cells[1].text();
    }

    public void hasBuildDate(boolean withBuildNumber, boolean withBuildLink) {
        // date format: dd MMM yyyy, HH:mm
        WebAssertion[] cells = getBodyRow().getCells();
        int columnCount = 2;
        if (withBuildNumber) columnCount++;
        if (withBuildLink) columnCount++;
        assertThat(cells).hasSize(columnCount);
        assertThat(cells[columnCount -1].text())
                .matches("^[0-3][0-9] \\w{3} \\d{4}, \\d{2}:\\d{2}$");
    }

    public void hasBuildLink(boolean withBuildNumber, String buildUrl, String buildName) {
        WebAssertion[] cells = getBodyRow().getCells();
        assertThat(cells).hasSize(withBuildNumber ? 4 : 3);
        assertThat(cells[withBuildNumber ? 2 : 1].html())
                .contains(String.format("<a href=\"%s\">%s</a>", buildUrl, buildName));
    }
}
