package net.masterthought.cucumber.generators;

import java.io.IOException;
import java.util.Map;

import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.TagObject;

public class TagReportPage extends AbstractPage {

    public TagReportPage(ReportBuilder reportBuilder) {
        super(reportBuilder, "tagReport.vm");
    }

    @Override
    public void generatePage() throws IOException {
        for (TagObject tagObject : reportInformation.getTags()) {
            super.generatePage();
            Map<String, String> header = this.reportBuilder.getCustomHeader();
            contextMap.put("tag", tagObject);
            contextMap.put("report_status_colour", reportInformation.getTagReportStatusColour(tagObject));
            contextMap.put("hasCustomHeader", false);
            if (header != null && header.get(tagObject.getTagName()) != null) {
                contextMap.put("hasCustomHeader", true);
                contextMap.put("customHeader", header.get(tagObject.getTagName()));
            }

            generateReport(tagObject.getTagName().replace("@", "").trim() + ".html");
        }
    }

}
