package net.masterthought.cucumber.generators.integrations.helpers;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class LinkAssertion extends ReportAssertion {

    public void hasLabelAndAddress(String label, String address) {
        assertThat(element.attr("href")).isEqualTo(address);
        assertThat(element.text()).isEqualTo(label);
    }
}
