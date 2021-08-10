package net.masterthought.cucumber.generators;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.ReportResult;
import net.masterthought.cucumber.ValidationException;
import net.masterthought.cucumber.presentation.PresentationMode;
import net.masterthought.cucumber.reducers.ReducingMethod;
import net.masterthought.cucumber.util.Counter;
import net.masterthought.cucumber.util.StepNameFormatter;
import net.masterthought.cucumber.util.Util;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.app.event.EventCartridge;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

/**
 * Delivers common methods for page generation.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public abstract class AbstractPage {

    private static final Logger LOG = Logger.getLogger(AbstractPage.class.getName());

    private final VelocityEngine engine = new VelocityEngine();
    protected final VelocityContext context = new VelocityContext();

    /**
     * Name of the HTML file which will be generated.
     */
    private final String templateFileName;
    /**
     * Results of the report.
     */
    protected final ReportResult reportResult;
    /**
     * Configuration used for this report execution.
     */
    protected final Configuration configuration;

    protected AbstractPage(ReportResult reportResult, String templateFileName, Configuration configuration) {
        this.templateFileName = templateFileName;
        this.reportResult = reportResult;
        this.configuration = configuration;

        this.engine.init(buildProperties());
        buildGeneralParameters();
    }

    public void generatePage() {
        prepareReport();
        generateReport();
    }

    /**
     * Returns HTML file name (with extension) for this report.
     *
     * @return HTML file for the report
     */
    public abstract String getWebPage();

    protected abstract void prepareReport();

    private void generateReport() {
        context.put("report_file", getWebPage());

        Template template = engine.getTemplate("templates/generators/" + templateFileName);
        File reportFile = new File(configuration.getReportDirectory(),
                ReportBuilder.BASE_DIRECTORY + configuration.getDirectorySuffixWithSeparator() + File.separatorChar + getWebPage());
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(reportFile), StandardCharsets.UTF_8)) {
            template.merge(context, writer);
        } catch (IOException e) {
            throw new ValidationException(e);
        }
    }

    private Properties buildProperties() {
        Properties props = new Properties();
        props.setProperty("resource.loaders", "class");
        props.setProperty("resource.loader.class.class", ClasspathResourceLoader.class.getCanonicalName());
        props.setProperty("runtime.log", new File(configuration.getReportDirectory(), "velocity.log").getPath());

        return props;
    }

    private void buildGeneralParameters() {
        // to escape html and xml
        EventCartridge ec = new EventCartridge();
        ec.addEventHandler(new EscapeHtmlReference());
        context.attachEventCartridge(ec);

        // to provide unique ids for elements on each page
        context.put("counter", new Counter());
        context.put("util", Util.INSTANCE);
        context.put("stepNameFormatter", StepNameFormatter.INSTANCE);

        context.put("run_with_jenkins", configuration.containsPresentationMode(PresentationMode.RUN_WITH_JENKINS));
        context.put("expand_all_steps", configuration.containsPresentationMode(PresentationMode.EXPAND_ALL_STEPS));
        context.put("hide_empty_hooks", configuration.containsReducingMethod(ReducingMethod.HIDE_EMPTY_HOOKS));

        context.put("trends_available", configuration.isTrendsAvailable());
        context.put("build_project_name", configuration.getProjectName());
        context.put("build_number", configuration.getBuildNumber());
        context.put("directory_suffix", configuration.getDirectorySuffixWithSeparator());

        context.put("js_files", configuration.getCustomJsFiles().stream()
                .map(f -> new File(f).getName())
                .collect(Collectors.toList()));
        context.put("css_files", configuration.getCustomCssFiles().stream()
                .map(f -> new File(f).getName())
                .collect(Collectors.toList()));

        // if report generation fails then report is null
        String formattedTime = reportResult != null ? reportResult.getBuildTime() : ReportResult.getCurrentTime();
        context.put("build_time", formattedTime);

        // build number is not mandatory
        String buildNumber = configuration.getBuildNumber();
        if (StringUtils.isNotBlank(buildNumber) &&
                configuration.containsPresentationMode(PresentationMode.RUN_WITH_JENKINS)) {
            if (NumberUtils.isCreatable(buildNumber)) {
                context.put("build_previous_number", Integer.parseInt(buildNumber) - 1);
            } else {
                LOG.log(Level.INFO, "Could not parse build number: {0}.", configuration.getBuildNumber());
            }
        }
    }
}
