package net.masterthought.cucumber.generators;

import java.util.ArrayList;
import java.util.List;

import org.apache.velocity.VelocityContext;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportResult;
import net.masterthought.cucumber.json.Element;
import net.masterthought.cucumber.json.Feature;

public class FailuresOverviewPage extends AbstractPage {

    public static final String WEB_PAGE = "overview-failures.html";

    public FailuresOverviewPage() {
        super("overviewFailures.vm");
    }

    @Override
    public String getWebPage() {
        return WEB_PAGE;
    }

    @Override
    public void preparePageContext(VelocityContext context, Configuration configuration, ReportResult reportResult) {
        context.put("failures", collectFailures(reportResult));
    }

    private List<Element> collectFailures(ReportResult reportResult) {
        List<Element> failures = new ArrayList<>();
        for (Feature feature : reportResult.getAllFeatures()) {
            if (feature.getStatus().isPassed()) {
                continue;
            }

            for (Element element : feature.getElements()) {
                if (!element.getStepsStatus().isPassed()) {
                    failures.add(element);
                }
            }
        }
        return failures;
    }
}
