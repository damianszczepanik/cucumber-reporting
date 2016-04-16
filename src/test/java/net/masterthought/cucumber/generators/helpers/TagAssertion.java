package net.masterthought.cucumber.generators.helpers;

public class TagAssertion extends ReportAssertion {

    public static TagAssertion[] getTags(WebAssertion report) {
        return report.childByClass("tags", WebAssertion.class).allBySelector("a", TagAssertion.class);
    }
}
