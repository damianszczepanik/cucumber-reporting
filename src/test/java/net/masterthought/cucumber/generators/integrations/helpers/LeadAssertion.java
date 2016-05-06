package net.masterthought.cucumber.generators.integrations.helpers;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class LeadAssertion extends ReportAssertion {

    public String getHeader() {
        return oneBySelector("h2", WebAssertion.class).text();
    }

    public String getDescription() {
        return oneBySelector("p", WebAssertion.class).text();
    }
}
