package net.masterthought.cucumber.generators.helpers;

public class LeadAssertion extends ReportAssertion {

    public String getHeader() {
        return oneBySelector("h2", WebAssertion.class).text();
    }

    public String getDescription() {
        return oneBySelector("p", WebAssertion.class).text();
    }
}
