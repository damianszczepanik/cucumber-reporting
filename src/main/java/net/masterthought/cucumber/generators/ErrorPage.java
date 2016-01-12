package net.masterthought.cucumber.generators;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;

public class ErrorPage extends AbstractPage {

    private final Exception exception;
    private final List<String> jsonFiles;

    public ErrorPage(ReportBuilder reportBuilder, Configuration configuration, Exception exception,
            List<String> jsonFiles) {
        super(reportBuilder, "errorPage.vm", configuration);
        this.exception = exception;
        this.jsonFiles = jsonFiles;
    }

    @Override
    public void generatePage() throws IOException {
        super.generatePage();

        contextMap.put("error_message", ExceptionUtils.getStackTrace(exception));
        contextMap.put("json_files", jsonFiles);

        super.generateReport("feature-overview.html");
    }
}
