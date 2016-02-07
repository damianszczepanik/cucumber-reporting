package net.masterthought.cucumber.generators;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportResult;
import net.masterthought.cucumber.json.support.TagObject;

public class TagReportPage extends AbstractPage {

    public TagReportPage(ReportResult reportResult, Configuration configuration) {
        super(reportResult, "tagReport.vm", configuration);
    }

    @Override
    public void generatePage() {
        for (TagObject tagObject : report.getAllTags()) {
            super.generatePage();

            contextMap.put("tag", tagObject);

            generateReport(tagObject.getReportFileName());
        }
    }

}
