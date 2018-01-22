package net.masterthought.cucumber.generators;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.app.event.EventCartridge;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.ReportResult;
import net.masterthought.cucumber.ValidationException;
import net.masterthought.cucumber.util.Counter;
import net.masterthought.cucumber.util.Util;

public class PageGenerator {

    private static final Logger LOG = LogManager.getLogger(PageGenerator.class);

    private final VelocityEngine engine;

    final VelocityContext globalContext;

    private final Configuration configuration;

    private final ReportResult reportResult;

    private final File reportDirectory;

    public PageGenerator(Configuration configuration, ReportResult reportResult) {

        this.engine = new VelocityEngine();
        this.globalContext = newGlobalContext(configuration, reportResult);

        this.configuration = configuration;
        this.reportResult = reportResult;

        this.engine.init(buildProperties());
        this.reportDirectory = new File(configuration.getReportDirectory(), ReportBuilder.BASE_DIRECTORY);

        createDirectories();
    }

    private VelocityContext newGlobalContext(Configuration configuration, ReportResult reportResult) {

        VelocityContext context = new VelocityContext();

        // to escape html and xml
        EventCartridge ec = new EventCartridge();
        ec.addEventHandler(new EscapeHtmlReference());
        context.attachEventCartridge(ec);

        context.put("util", Util.INSTANCE);

        context.put("run_with_jenkins", configuration.isRunWithJenkins());
        context.put("trends_present", configuration.getTrendsStatsFile() != null);
        context.put("build_project_name", configuration.getProjectName());
        context.put("build_number", configuration.getBuildNumber());

        // if report generation fails then report is null
        String formattedTime = reportResult != null ? reportResult.getBuildTime() : ReportResult.getCurrentTime();
        context.put("build_time", formattedTime);

        // build number is not mandatory
        String buildNumber = configuration.getBuildNumber();
        if (StringUtils.isNotBlank(buildNumber) && configuration.isRunWithJenkins()) {
            if (NumberUtils.isCreatable(buildNumber)) {
                context.put("build_previous_number", Integer.parseInt(buildNumber) - 1);
            } else {
                LOG.info("Could not parse build number: {}.", configuration.getBuildNumber());
            }
        }

        return context;
    }

    private Properties buildProperties() {
        Properties props = new Properties();
        props.setProperty("resource.loader", "class");
        props.setProperty("class.resource.loader.class", ClasspathResourceLoader.class.getCanonicalName());
        props.setProperty("runtime.log", new File(configuration.getReportDirectory(), "velocity.log").getPath());

        return props;
    }

    private void createDirectories() {
        try {
            Files.createDirectories(reportDirectory.toPath());
        } catch (IOException e) {
            throw new ValidationException(e);
        }
    }

    public void generatePage(AbstractPage page) {

        VelocityContext context = newPageContext();
        page.preparePageContext(context, configuration, reportResult);
        context.put("report_file", page.getWebPage());

        writePage(page.getTemplateName(), page.getWebPage(), context);
    }

    private VelocityContext newPageContext() {

        VelocityContext context = new VelocityContext(globalContext);

        // to provide unique ids for elements on each page
        context.put("counter", new Counter());

        return context;
    }

    private void writePage(String templateName, String webPage, VelocityContext context) {

        Template template = engine.getTemplate(templateName);
        File pageFile = new File(reportDirectory, webPage);

        try (Writer writer = new OutputStreamWriter(new FileOutputStream(pageFile), StandardCharsets.UTF_8)) {
            template.merge(context, writer);
        } catch (IOException e) {
            throw new ValidationException(e);
        }

    }

}