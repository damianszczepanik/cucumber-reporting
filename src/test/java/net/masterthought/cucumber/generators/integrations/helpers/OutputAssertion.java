package net.masterthought.cucumber.generators.integrations.helpers;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class OutputAssertion extends ReportAssertion {

    public void hasMessages(String[] messages) {
        WebAssertion[] outputMessages = allBySelector("p", WebAssertion.class);
        assertThat(outputMessages).hasSameSizeAs(messages);
        for (int i = 0; i < messages.length; i++) {
            assertThat(outputMessages[i].text()).isEqualTo(messages[i]);
        }
    }
}
