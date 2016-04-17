package net.masterthought.cucumber.generators.helpers;

import static org.assertj.core.api.Assertions.assertThat;

public class LinkAssertion extends ReportAssertion {

    public void hasLabelAndAddress(String label, String address) {
        assertThat(element.attr("href")).isEqualTo(address);
        assertThat(element.text()).isEqualTo(label);
    }
}
