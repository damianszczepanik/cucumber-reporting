package net.masterthought.cucumber.generators.helpers;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class HooksAssertion extends ReportAssertion {

    @Override
    public BriefAssertion getBrief() {
        return super.getBrief();
    }

    public HookAssertion[] getHooks() {
        return allByClass("hook", HookAssertion.class);
    }
}
