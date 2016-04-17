package net.masterthought.cucumber.generators.helpers;

public class HeadAssertion extends ReportAssertion {

    public String getTitle() {
        return oneBySelector("title", WebAssertion.class).text();
    }
}
