package net.masterthought.cucumber.generators;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Properties;

import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportResult;
import net.masterthought.cucumber.ValidationException;

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
    private final String templateFileName;
    protected final ReportResult report;
    protected final Configuration configuration;

    protected AbstractPage(ReportResult reportResult, String templateFileName, Configuration configuration) {
        this.templateFileName = templateFileName;
        this.report = reportResult;
        this.configuration = configuration;

        buildGeneralParameters();
    }

    public final void generatePage() {
        ve.init(getProperties());
        template = ve.getTemplate("templates/pages/" + templateFileName);

        prepareReport();
        generateReport();
    }

    /** Returns HTML file name (with extension) for this report. */
    public abstract String getWebPage();

    protected abstract void prepareReport();

    private void generateReport() {
        velocityContext.put("report_file", getWebPage());

        File reprotFile = new File(configuration.getReportDirectory(), getWebPage());
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(reprotFile), Charsets.UTF_8)) {
            template.merge(velocityContext, writer);
        } catch (IOException e) {
            throw new ValidationException(e);
        }
    }

    private Properties getProperties() {
        Properties props = new Properties();
        props.setProperty("resource.loader", "class");
        props.setProperty("class.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        props.setProperty("runtime.log", new File(configuration.getReportDirectory(), "velocity.log").getPath());

        return props;
    }

    private void buildGeneralParameters() {
        velocityContext.put("jenkins_source", configuration.isRunWithJenkins());
        velocityContext.put("jenkins_base", configuration.getJenkinsBasePath());
        velocityContext.put("build_project_name", configuration.getProjectName());
        velocityContext.put("build_number", configuration.getBuildNumber());

        // if report generation fails then report is null
        if (report != null) {
            velocityContext.put("build_time", report.getBuildTime());
        }

        // build number is not mandatory
        String buildNumber = configuration.getBuildNumber();
        if (buildNumber != null) {
            try {
                int buildValue = Integer.parseInt(buildNumber);
                velocityContext.put("build_previous_number", --buildValue);
            } catch (NumberFormatException e) {
                LOG.error("Could not parse build number: {}.", configuration.getBuildNumber(), e);
            }
        }
    }

}
