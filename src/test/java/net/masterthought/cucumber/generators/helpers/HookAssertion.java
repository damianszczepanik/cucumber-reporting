package net.masterthought.cucumber.generators.helpers;

public class HookAssertion extends ReportAssertion {

    @Override
    public BriefAssertion getBrief() {
        return super.getBrief();
    }

    public String getMessage() {
        return oneByClass("message", WebAssertion.class).text();
    }
}
