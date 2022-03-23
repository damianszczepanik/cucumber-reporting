package net.masterthought.cucumber.generators;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportResult;
import net.masterthought.cucumber.json.support.TagObject;

public class TagReportPage extends AbstractPage {

    private final TagObject tagObject;

    public TagReportPage(ReportResult reportResult, Configuration configuration, TagObject tagObject) {
        super(reportResult, "reportTag.vm", configuration);
        this.tagObject = tagObject;
    }

    @Override
    public String getWebPage() {
        return tagObject.getReportFileName();
    }

    @Override
    public void prepareReport() {
        context.put("tag", tagObject);
    }

}
