package net.masterthought.cucumber.generators;

import java.io.IOException;

import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.json.support.TagObject;

public class TagReportPage extends AbstractPage {

    public TagReportPage(ReportBuilder reportBuilder) {
        super(reportBuilder, "tagReport.vm");
    }

    @Override
    public void generatePage() throws IOException {
        for (TagObject tagObject : reportInformation.getTags()) {
            super.generatePage();

            contextMap.put("tag", tagObject);
            contextMap.put("report_status_colour", tagObject.getStatus().color);

            generateReport(tagObject.getReportFileName());
        }
    }

}
