package net.masterthought.cucumber.generators.integrations.helpers;

import static org.assertj.core.api.Assertions.assertThat;
/**
 * @author Sean Bucholtz (sean.bucholtz@gmail.com)
 */
public class CalloutAssertion extends DropupAssertion {
    protected CalloutAssertion getCalloutScenario() {return firstBySelector("h4", this.getClass());}

    protected CalloutAssertion getCalloutStep() {return firstBySelector("strong", this.getClass());}

    protected CalloutAssertion getCalloutError() {return firstBySelector("samp", this.getClass());}

    public LinkAssertion getScenarioStepLink() { return getCalloutStep().firstBySelector("a", LinkAssertion.class); }

    public void hasExactScenarioValue(String expectedScenarioValue) {
        WebAssertion calloutScenario = getCalloutScenario();
        assertThat(calloutScenario.text()).isEqualTo(expectedScenarioValue);
    }

    public void hasExactErrorValue(String expectedErrorValue) {
        WebAssertion calloutError = getCalloutError();
        assertThat(calloutError.text()).isEqualTo(expectedErrorValue);
    }
}
