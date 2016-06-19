package net.masterthought.cucumber.generators;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportResult;
import net.masterthought.cucumber.json.Element;
import net.masterthought.cucumber.json.Feature;

import java.util.ArrayList;
import java.util.List;

public class FailuresOverviewPage extends AbstractPage {

    public FailuresOverviewPage(ReportResult reportResult, Configuration configuration) {
        super(reportResult, "failuresOverview.vm", configuration);
    }

    @Override
    public String getWebPage() {
        return "failures-overview.html";
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
