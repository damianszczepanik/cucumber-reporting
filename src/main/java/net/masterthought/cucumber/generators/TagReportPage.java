package net.masterthought.cucumber.generators;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportResult;
import net.masterthought.cucumber.json.support.TagObject;

public class TagReportPage extends AbstractPage {

    private final TagObject tagObject;

    public TagReportPage(ReportResult reportResult, Configuration configuration, TagObject tagObject) {
        super(reportResult, "tagReport.vm", configuration, tagObject.getReportFileName());
        this.tagObject = tagObject;
    }

    @Override
    public void prepareReport() {
        velocityContext.put("tag", tagObject);
    }

}
