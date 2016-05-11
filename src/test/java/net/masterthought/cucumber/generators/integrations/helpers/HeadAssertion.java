package net.masterthought.cucumber.generators.integrations.helpers;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class HeadAssertion extends ReportAssertion {

    public String getTitle() {
        return oneBySelector("title", WebAssertion.class).text();
    }
}
