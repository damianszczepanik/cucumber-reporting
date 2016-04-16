package net.masterthought.cucumber.generators.helpers;

public abstract class ReportAssertion extends WebAssertion {

    protected WebAssertion getReport() {
        return byId("report", WebAssertion.class);
    }

    protected TagAssertion[] getTags() {
        return TagAssertion.getTags(this);
    }

    public BriefAssertion getBrief() {
        return childByClass("brief", BriefAssertion.class);
    }

    public LinkAssertion getLink() {
        return oneBySelector("a", LinkAssertion.class);
    }
}
