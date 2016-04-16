package net.masterthought.cucumber.generators.helpers;

import static org.assertj.core.api.Assertions.assertThat;

import net.masterthought.cucumber.json.support.Status;

public class BriefAssertion extends ReportAssertion {

    public String getKeyword() {
        return oneByClass("keyword", WebAssertion.class).text();
    }

    public String getName() {
        return oneByClass("name", WebAssertion.class).text();
    }

    public String getDuration() {
        return oneByClass("duration", WebAssertion.class).text();
    }

    public void hasStatus(Status status) {
        assertThat(classNames()).contains(status.getRawName());
    }
}
