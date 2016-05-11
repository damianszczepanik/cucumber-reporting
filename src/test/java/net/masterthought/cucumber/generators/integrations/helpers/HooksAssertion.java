package net.masterthought.cucumber.generators.integrations.helpers;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class HooksAssertion extends ReportAssertion {

    @Override
    public BriefAssertion getBrief() {
        return super.getCollapseControl(BriefAssertion.class).getBrief();
    }

    public HookAssertion[] getHooks() {
        return allByClass("hook", HookAssertion.class);
    }
}
