package net.masterthought.cucumber.generators.integrations.helpers;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class ElementAssertion extends ReportAssertion {

    public LinkAssertion getFeatureName() {
        WebAssertion[] webs = allBySelector("div", WebAssertion.class);
        // get the first nested <div> which has no its class but contains the feature name
        return webs[0].oneBySelector("a", LinkAssertion.class);
    }

    @Override
    public TagAssertion[] getTags() {
        return super.getTags();
    }

    @Override
    public BriefAssertion getBrief() {
        return super.getCollapseControl(BriefAssertion.class).getBrief();
    }

    public HooksAssertion getBefore() {
        return oneByClass("hooks-element-before", HooksAssertion.class);
    }

    public HooksAssertion getAfter() {
        return oneByClass("hooks-element-after", HooksAssertion.class);
    }

    public StepsAssertion getStepsSection() {
        return oneByClass("steps", StepsAssertion.class);
    }

    public String getDescription() {
        return childByClass("description", WebAssertion.class).text();
    }
}
