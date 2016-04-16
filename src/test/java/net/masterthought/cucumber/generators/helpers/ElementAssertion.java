package net.masterthought.cucumber.generators.helpers;

public class ElementAssertion extends ReportAssertion {

    @Override
    public TagAssertion[] getTags() {
        return super.getTags();
    }

    @Override
    public BriefAssertion getBrief() {
        return super.getBrief();
    }

    public HookAssertion[] getBefore() {
        return getHooks(childByClass("hooks-before", HookAssertion.class));
    }

    public HookAssertion[] getAfter() {
        return getHooks(childByClass("hooks-after", HookAssertion.class));
    }

    private HookAssertion[] getHooks(HookAssertion hooks) {
        return hooks.allByClass("hook", HookAssertion.class);
    }

    public String getDescription() {
        return childByClass("description", WebAssertion.class).text();
    }
}
