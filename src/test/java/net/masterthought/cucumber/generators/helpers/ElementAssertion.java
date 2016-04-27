package net.masterthought.cucumber.generators.helpers;

public class ElementAssertion extends ReportAssertion {

    @Override
    public TagAssertion[] getTags() {
        return super.getTags();
    }

    @Override
    public BriefAssertion getBrief() {
        return super.getCollapseControl(BriefAssertion.class).getBrief();
    }

    public HookAssertion[] getBefore() {
        return getHooks(getCollapseDetails(HookAssertion.class).oneByClass("hooks-before", HookAssertion.class));
    }

    public HookAssertion[] getAfter() {
        return getHooks(getCollapseDetails(HookAssertion.class).oneByClass("hooks-after", HookAssertion.class));
    }

    public StepAssertion[] getSteps() {
        return allByClass("step", StepAssertion.class);
    }
    private HookAssertion[] getHooks(HookAssertion hooks) {
        return hooks.allByClass("hook", HookAssertion.class);
    }

    public String getDescription() {
        return childByClass("description", WebAssertion.class).text();
    }
}
