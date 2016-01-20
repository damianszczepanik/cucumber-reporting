package net.masterthought.cucumber.generators;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.generic.EscapeTool;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportResult;
import net.masterthought.cucumber.VelocityContextMap;
import net.masterthought.cucumber.json.support.Status;

/**
 * Delivers common methods for page generation.
 * 
 * @author Damian Szczepanik (damianszczepanik@github)
 *
 */
public abstract class AbstractPage {

    private static final Logger LOG = LogManager.getLogger(AbstractPage.class);

    protected final VelocityEngine ve = new VelocityEngine();
    protected final VelocityContextMap contextMap = VelocityContextMap.of(new VelocityContext());
    private Template template;

    /** Name of the html file which will be generated. */
    private final String fileName;
    protected final ReportResult report;
    protected final Configuration configuration;

    protected AbstractPage(ReportResult reportResult, String fileName, Configuration configuration) {
        this.fileName = fileName;
        this.report = reportResult;
        this.configuration = configuration;
    }

    public void generatePage() {
        ve.init(getProperties());
        template = ve.getTemplate("templates/pages/" + fileName);

        contextMap.clear();
        contextMap.putAll(getGeneralParameters());
        contextMap.put("esc", new EscapeTool());

        if (this instanceof ErrorPage) {
            contextMap.put("time_stamp", new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
        } else {
            contextMap.put("time_stamp", report.timeStamp());
        }
        contextMap.put("status_color", Arrays.asList(Status.getOrderedColors()));
    }

    protected void generateReport(String fileName) {
        VelocityContext context = contextMap.getVelocityContext();
        context.put("page_url", fileName);
        File dir = new File(configuration.getReportDirectory(), fileName);
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(dir), Charsets.UTF_8)) {
                template.merge(context, writer);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    protected Properties getProperties() {
        Properties props = new Properties();
        props.setProperty("resource.loader", "class");
        props.setProperty("class.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        props.setProperty("runtime.log", new File(configuration.getReportDirectory(), "velocity.log").getPath());

        return props;
    }

    protected Map<String, Object> getGeneralParameters() {
        Map<String, Object> result = new HashMap<>();
        result.put("jenkins_source", configuration.isRunWithJenkins());
        result.put("jenkins_base", configuration.getJenkinsBasePath());
        result.put("build_project", configuration.getProjectName());
        result.put("build_number", configuration.getBuildNumber());
        if (configuration.isRunWithJenkins()){
            int previousBuildNumber = -1;
            try {
                previousBuildNumber = Integer.parseInt(configuration.getBuildNumber());
                previousBuildNumber--;
            } catch (NumberFormatException e) {
                LOG.error("Could not parse build number: {}.", configuration.getBuildNumber(), e);
            }
            result.put("build_previous_number", previousBuildNumber);
        }

        return result;
    }

}
