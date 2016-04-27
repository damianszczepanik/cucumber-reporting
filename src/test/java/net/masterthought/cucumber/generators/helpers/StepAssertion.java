package net.masterthought.cucumber.generators.helpers;

public class StepAssertion extends ReportAssertion {

    public BriefAssertion getBrief() {
        return super.getBrief();
    }

    public TableAssertion getArgumentsTable() {
        return oneByClass("step-arguments", TableAssertion.class);
    }
}
