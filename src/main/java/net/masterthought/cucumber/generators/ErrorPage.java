package net.masterthought.cucumber.generators;

import java.io.IOException;

import net.masterthought.cucumber.ReportBuilder;

public class ErrorPage extends AbstractPage {

    private Exception exception;

    public ErrorPage(ReportBuilder reportBuilder, Exception exception) {
        super(reportBuilder, "errorPage.vm");
        this.exception = exception;
    }

    @Override
    public void generatePage() throws IOException {
        super.generatePage();

        contextMap.put("error_message", exception);

        super.generateReport("feature-overview.html");
    }

}
