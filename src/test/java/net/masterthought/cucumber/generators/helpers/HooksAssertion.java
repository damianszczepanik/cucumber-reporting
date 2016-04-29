package net.masterthought.cucumber.generators.helpers;

public class HooksAssertion extends ReportAssertion {

    @Override
    public BriefAssertion getBrief() {
        return super.getCollapseControl(BriefAssertion.class).getBrief();
    }

    public HookAssertion[] getHooks() {
        return allByClass("hook", HookAssertion.class);
    }
}
