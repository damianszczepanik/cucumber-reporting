package net.masterthought.cucumber.generators;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.ReportInformation;
import net.masterthought.cucumber.VelocityContextMap;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.generic.EscapeTool;

import com.google.common.base.Charsets;

public abstract class AbstractPage {

    protected final VelocityEngine ve = new VelocityEngine();
    protected final VelocityContextMap contextMap = VelocityContextMap.of(new VelocityContext());
    private Template template;
    /** Name of the html file which will be generated. */
    private final String fileName;

    protected final ReportBuilder reportBuilder;
    protected final ReportInformation reportInformation;

    protected AbstractPage(ReportBuilder reportBuilder, String fileName) {
        this.reportBuilder = reportBuilder;
        this.fileName = fileName;
        this.reportInformation = reportBuilder.getReportInformation();
    }

    public void generatePage() throws IOException {
        ve.init(getProperties());
        template = ve.getTemplate("templates/pages/" + fileName);
        contextMap.putAll(getGeneralParameters());
        contextMap.put("esc", new EscapeTool());

        if (this instanceof ErrorPage) {
            contextMap.put("time_stamp", new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
        } else {
            contextMap.put("time_stamp", reportInformation.timeStamp());
        }
    }

    protected void generateReport(String fileName) throws IOException {
        VelocityContext context = contextMap.getVelocityContext();
        context.put("pageUrl", fileName);
        File dir = new File(this.reportBuilder.getReportDirectory(), fileName);
        try (FileOutputStream fileStream = new FileOutputStream(dir)) {
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fileStream, Charsets.UTF_8))) {
                template.merge(context, writer);
            }
        }
    }

    protected Properties getProperties() {
        Properties props = new Properties();
        props.setProperty("resource.loader", "class");
        props.setProperty("class.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        props.setProperty("runtime.log", new File(this.reportBuilder.getReportDirectory(), "velocity.log").getPath());
        return props;
    }

    protected Map<String, Object> getGeneralParameters() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("version", ReportBuilder.VERSION);
        result.put("fromJenkins", this.reportBuilder.isRunWithJenkins());
        result.put("jenkins_base", this.reportBuilder.getPluginUrlPath());
        result.put("build_project", this.reportBuilder.getBuildProject());
        result.put("build_number", this.reportBuilder.getBuildNumber());
        int previousBuildNumber = -1;
        try {
            previousBuildNumber = Integer.parseInt(this.reportBuilder.getBuildNumber());
            previousBuildNumber--;
        } catch (NumberFormatException e) {
            // could not parse build number, probably not valid int value
        }
        result.put("previous_build_number", previousBuildNumber);

        return result;
    }

}
