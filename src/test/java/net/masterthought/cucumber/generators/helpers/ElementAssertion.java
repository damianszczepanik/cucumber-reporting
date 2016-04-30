package net.masterthought.cucumber.generators.helpers;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class ElementAssertion extends ReportAssertion {

    @Override
    public TagAssertion[] getTags() {
        return super.getTags();
    }

    @Override
    public BriefAssertion getBrief() {
        return super.getCollapseControl(BriefAssertion.class).getBrief();
    }

    public HooksAssertion getBefore() {
        return oneByClass("hooks-before", HooksAssertion.class);
    }

    public HooksAssertion getAfter() {
        return oneByClass("hooks-after", HooksAssertion.class);
    }

    public StepsAssertion getSteps() {
        return oneByClass("steps", StepsAssertion.class);
    }

    public String getDescription() {
        return childByClass("description", WebAssertion.class).text();
    }
}
