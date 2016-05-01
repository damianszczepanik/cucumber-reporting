package net.masterthought.cucumber.generators.helpers;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class StepsAssertion extends ReportAssertion {

    @Override
    public BriefAssertion getBrief() {
        return super.getBrief();
    }

    public StepAssertion[] getSteps() {
        return allByClass("step", StepAssertion.class);
    }

}
