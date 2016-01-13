package net.masterthought.cucumber.generators;

import java.io.IOException;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportResult;
import net.masterthought.cucumber.json.support.TagObject;

public class TagReportPage extends AbstractPage {

    public TagReportPage(ReportResult reportResult, Configuration configuration) {
        super(reportResult, "tagReport.vm", configuration);
    }

    @Override
    public void generatePage() throws IOException {
        for (TagObject tagObject : report.getAllTags()) {
            super.generatePage();

            contextMap.put("tag", tagObject);
            contextMap.put("status_colour", tagObject.getStatus().color);

            generateReport(tagObject.getReportFileName());
        }
    }

}
