package net.masterthought.cucumber.generators;

import org.apache.velocity.VelocityContext;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportResult;
import net.masterthought.cucumber.json.support.TagObject;

public class TagReportPage extends AbstractPage {

    private final TagObject tagObject;

    public TagReportPage(TagObject tagObject) {
        super("reportTag.vm");
        this.tagObject = tagObject;
    }

    @Override
    public String getWebPage() {
        return tagObject.getReportFileName();
    }

    @Override
    public void preparePageContext(VelocityContext context, Configuration configuration, ReportResult reportResult) {
        context.put("tag", tagObject);
    }

}
