package net.masterthought.cucumber.generators.integrations.helpers;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public abstract class ReportAssertion extends WebAssertion {

    protected WebAssertion getReport() {
        return byId("report", WebAssertion.class);
    }

    protected TagAssertion[] getTags() {
        return TagAssertion.getTags(this);
    }

    protected BriefAssertion getBrief() {
        return childByClass("brief", BriefAssertion.class);
    }

    protected <T extends WebAssertion> T getCollapseControl(Class<T> clazz) {
        return childByClass("collapsable-control", clazz);
    }

    public LinkAssertion getLink() {
        return oneBySelector("a", LinkAssertion.class);
    }

    public LinkAssertion[] getLinks() {
        return allBySelector("a", LinkAssertion.class);
    }

    protected String getErrorMessage() {
        return oneByClass("message", WebAssertion.class).html();
    }

}
