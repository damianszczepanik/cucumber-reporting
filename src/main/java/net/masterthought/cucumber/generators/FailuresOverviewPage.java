package net.masterthought.cucumber.generators;

import java.util.ArrayList;
import java.util.List;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportResult;
import net.masterthought.cucumber.json.Element;
import net.masterthought.cucumber.json.Feature;

public class FailuresOverviewPage extends AbstractPage {

    public static final String WEB_PAGE = "failures-overview.html";

    public FailuresOverviewPage(ReportResult reportResult, Configuration configuration) {
        super(reportResult, "failuresOverview.vm", configuration);
    }

    @Override
    public String getWebPage() {
        return WEB_PAGE;
    }

    @Override
    public void prepareReport() {
        List<Element> failures = new ArrayList<>();
        for (Feature feature : report.getAllFeatures()) {
            if (feature.getStatus().isPassed()) continue;

            for (Element element : feature.getElements()) {
                if (!element.getStepsStatus().isPassed()) {
                    failures.add(element);
                }
            }
        }

        context.put("failures", failures);
    }
}
