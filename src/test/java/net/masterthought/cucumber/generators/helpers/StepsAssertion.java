package net.masterthought.cucumber.generators.helpers;

public class StepsAssertion extends ReportAssertion {

    @Override
    public BriefAssertion getBrief() {
        return super.getCollapseControl(BriefAssertion.class).getBrief();
    }

    public StepAssertion[] getSteps() {
        return allByClass("step", StepAssertion.class);
    }

}
