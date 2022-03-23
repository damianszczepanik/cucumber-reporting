package net.masterthought.cucumber.generators.integrations.helpers;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class FeatureAssertion extends ReportAssertion {

    public ElementAssertion[] getElements() {
        return allByClass("element", ElementAssertion.class);
    }

    @Override
    public TagAssertion[] getTags() {
        return super.getTags();
    }

    @Override
    public BriefAssertion getBrief() {
        return super.getBrief();
    }

    public String getDescription() {
        return childByClass("description", WebAssertion.class).text();
    }
}
