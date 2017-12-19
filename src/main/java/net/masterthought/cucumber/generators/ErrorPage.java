package net.masterthought.cucumber.generators;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.velocity.VelocityContext;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.ReportResult;

public class ErrorPage extends AbstractPage {

    private final Exception exception;
    private final List<String> jsonFiles;

    public ErrorPage(Exception exception, List<String> jsonFiles) {
        super("errorpage.vm");
        this.exception = exception;
        this.jsonFiles = jsonFiles;
    }

    @Override
    public String getWebPage() {
        return ReportBuilder.HOME_PAGE;
    }

    @Override
    public void preparePageContext(VelocityContext context, Configuration configuration, ReportResult reportResult) {
        context.put("output_message", ExceptionUtils.getStackTrace(exception));
        context.put("json_files", jsonFiles);
    }
}
