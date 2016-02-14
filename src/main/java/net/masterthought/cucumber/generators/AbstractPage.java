package net.masterthought.cucumber.generators;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportResult;

/**
 * Delivers common methods for page generation.
 * 
 * @author Damian Szczepanik (damianszczepanik@github)
 *
 */
public abstract class AbstractPage {

    private static final Logger LOG = LogManager.getLogger(AbstractPage.class);

    protected final VelocityEngine ve = new VelocityEngine();
    protected final VelocityContext velocityContext = new VelocityContext();
    private Template template;

    /** Name of the html file which will be generated. */
    private final String fileName;
    protected final ReportResult report;
    protected final Configuration configuration;

    protected AbstractPage(ReportResult reportResult, String fileName, Configuration configuration) {
        this.fileName = fileName;
        this.report = reportResult;
        this.configuration = configuration;

        buildGeneralParameters();
    }

    public void generatePage() {
        ve.init(getProperties());
        template = ve.getTemplate("templates/pages/" + fileName);

        if (this instanceof ErrorPage) {
            velocityContext.put("time_stamp", new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
        } else {
            velocityContext.put("time_stamp", report.timeStamp());
        }
    }

    protected void generateReport(String fileName) {
        velocityContext.put("page_url", fileName);
        File dir = new File(configuration.getReportDirectory(), fileName);
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(dir), Charsets.UTF_8)) {
            template.merge(velocityContext, writer);
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

    protected void buildGeneralParameters() {
        velocityContext.put("jenkins_source", configuration.isRunWithJenkins());
        velocityContext.put("jenkins_base", configuration.getJenkinsBasePath());
        velocityContext.put("build_project", configuration.getProjectName());
        velocityContext.put("build_number", configuration.getBuildNumber());
        if (configuration.isRunWithJenkins()){
            int buildNumber = -1;
            try {
                buildNumber = Integer.parseInt(configuration.getBuildNumber());
            } catch (NumberFormatException e) {
                LOG.error("Could not parse build number: {}.", configuration.getBuildNumber(), e);
            }
            velocityContext.put("build_previous_number", --buildNumber);
        }
    }

}
