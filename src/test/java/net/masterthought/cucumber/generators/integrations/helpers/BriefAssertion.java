package net.masterthought.cucumber.generators.integrations.helpers;

import static org.assertj.core.api.Assertions.assertThat;

import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.util.Util;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class BriefAssertion extends ReportAssertion {

    public String getKeyword() {
        return oneByClass("keyword", WebAssertion.class).text();
    }

    public String getName() {
        return oneByClass("name", WebAssertion.class).text();
    }

    public String getLocation() {
        return oneByClass("location", WebAssertion.class).text();
    }

    public void hasDuration(long duration) {
        String found = oneByClass("duration", WebAssertion.class).text();
        assertThat(found).isEqualTo(Util.formatDuration(duration));
    }

    public void hasStatus(Status status) {
        assertThat(classNames()).contains(status.getRawName());
    }
}
