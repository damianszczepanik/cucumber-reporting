package net.masterthought.cucumber.generators.integrations.helpers;

import org.jsoup.nodes.Element;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class DocumentAssertion extends ReportAssertion {

    public DocumentAssertion(Element element) {
        this.element = element;
    }

    public HeadAssertion getHead() {
        return oneBySelector("head", HeadAssertion.class);
    }

    public NavigationAssertion getNavigation() {
        return byId("navigation", NavigationAssertion.class);
    }

    public LeadAssertion getLead() {
        return byId("report-lead", LeadAssertion.class);
    }

    public BuildInfoAssertion getBuildInfo() {
        return byId("build-info", BuildInfoAssertion.class);
    }

    public SummaryAssertion getSummary() {
        return byId("summary", SummaryAssertion.class);
    }

    public FeatureAssertion getFeature() {
        return oneByClass("feature", FeatureAssertion.class);
    }

    public ElementAssertion[] getElements() {
        return oneByClass("elements", WebAssertion.class).allByClass("element", ElementAssertion.class);
    }
}
