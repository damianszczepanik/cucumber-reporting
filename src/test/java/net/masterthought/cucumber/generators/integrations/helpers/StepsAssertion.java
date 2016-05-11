package net.masterthought.cucumber.generators.integrations.helpers;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class StepsAssertion extends ReportAssertion {

    @Override
    public BriefAssertion getBrief() {
        return super.getCollapseControl(BriefAssertion.class).getBrief();
    }

    public StepAssertion[] getSteps() {
        return allByClass("step", StepAssertion.class);
    }

}
