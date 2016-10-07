package net.masterthought.cucumber;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.masterthought.cucumber.generators.AbstractPage;
import net.masterthought.cucumber.generators.ErrorPage;
import net.masterthought.cucumber.generators.FailuresOverviewPage;
import net.masterthought.cucumber.generators.FeatureReportPage;
import net.masterthought.cucumber.generators.FeaturesOverviewPage;
import net.masterthought.cucumber.generators.StepsOverviewPage;
import net.masterthought.cucumber.generators.TagReportPage;
import net.masterthought.cucumber.generators.TagsOverviewPage;
import net.masterthought.cucumber.generators.TrendsOverviewPage;
import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.json.support.TagObject;

public class ReportBuilder {

    private static final Logger LOG = LogManager.getLogger(ReportBuilder.class);

    /**
     * Page that should be displayed when the reports is generated. Shared between {@link FeaturesOverviewPage} and
     * {@link ErrorPage}.
     */
    public static final String HOME_PAGE = "overview-features.html";

    /**
     * Subdirectory where the report will be created.
     */
    public static final String BASE_DIRECTORY = "cucumber-html-reports";

    private static final ObjectMapper mapper = new ObjectMapper();

    private ReportResult reportResult;
    private final ReportParser reportParser;

    private Configuration configuration;
    private List<String> jsonFiles;

    public ReportBuilder(List<String> jsonFiles, Configuration configuration) {
        this.jsonFiles = jsonFiles;
        this.configuration = configuration;
        reportParser = new ReportParser(configuration);
    }

    /**
     * Parses provided files and generates whole report. When generating process fails
     * report with information about error is provided.
     */
    public Reportable generateReports() {
        try {
            // first copy static resources so ErrorPage is displayed properly
            copyStaticResources();

            // create directory for embeddings before files are generated
            createEmbeddingsDirectory();

            List<Feature> features = reportParser.parseJsonFiles(jsonFiles);
            reportResult = new ReportResult(features);

            List<AbstractPage> pages = collectPages();
            generatePages(pages);

            return reportResult.getFeatureReport();

            // whatever happens we want to provide at least error page instead of empty report
        } catch (Exception e) {
            generateErrorPage(e);
            // something went wrong, don't pass result that might be incomplete
            return null;
        }
    }

    private void copyStaticResources() {
        copyResources("css", "cucumber.css", "bootstrap.min.css", "font-awesome.min.css");
        copyResources("js", "jquery.min.js", "jquery.tablesorter.min.js", "bootstrap.min.js", "Chart.min.js");
        copyResources("fonts", "FontAwesome.otf", "fontawesome-webfont.svg", "fontawesome-webfont.woff",
                "fontawesome-webfont.eot", "fontawesome-webfont.ttf", "fontawesome-webfont.woff2",
                "glyphicons-halflings-regular.eot", "glyphicons-halflings-regular.eot",
                "glyphicons-halflings-regular.woff2", "glyphicons-halflings-regular.woff",
                "glyphicons-halflings-regular.ttf", "glyphicons-halflings-regular.svg");
        copyResources("images", "favicon.png");
    }

    private void createEmbeddingsDirectory() {
        configuration.getEmbeddingDirectory().mkdirs();
    }

    private void copyResources(String resourceLocation, String... resources) {
        for (String resource : resources) {
            File tempFile = new File(configuration.getReportDirectory().getAbsoluteFile(),
                    BASE_DIRECTORY + File.separatorChar + resourceLocation + File.separatorChar + resource);
            // don't change this implementation unless you verified it works on Jenkins
            try {
                FileUtils.copyInputStreamToFile(
                        this.getClass().getResourceAsStream("/" + resourceLocation + "/" + resource), tempFile);
            } catch (IOException e) {
                // based on FileUtils implementation, should never happen even is declared
                throw new ValidationException(e);
            }
        }
    }

    private List<AbstractPage> collectPages() {
        List<AbstractPage> pages = new ArrayList<>();

        pages.add(new FeaturesOverviewPage(reportResult, configuration));
        for (Feature feature : reportResult.getAllFeatures()) {
            pages.add(new FeatureReportPage(reportResult, configuration, feature));
        }

        pages.add(new TagsOverviewPage(reportResult, configuration));
        for (TagObject tagObject : reportResult.getAllTags()) {
            pages.add(new TagReportPage(reportResult, configuration, tagObject));
        }

        pages.add(new StepsOverviewPage(reportResult, configuration));
        pages.add(new FailuresOverviewPage(reportResult, configuration));
        if (configuration.getTrendsStatsFile() != null) {
            Trends trends = updateAndGenerateTrends(configuration.getTrendsStatsFile());
            pages.add(new TrendsOverviewPage(reportResult, configuration, trends));
        }

        return pages;
    }

    private void generatePages(List<AbstractPage> pages) {
        for (AbstractPage page : pages) {
            page.generatePage();
        }
    }

    private Trends updateAndGenerateTrends(File trendsFile) {
        Trends trends = loadTrends(trendsFile);
        appendCurrentReport(trends);
        // this should be removed later but for now correct features and save valid data
        applyPatchForFeatures(trends);
        // save updated trends so it contains all history...
        saveTrends(trends, trendsFile);
        // ...but display only last n items - don't skip items if limit is not defined
        if (configuration.getTrendsLimit() != 0) {
            trends.limitItems(configuration.getTrendsLimit());
        }

        return trends;
    }

    private static Trends loadTrends(File file) {
        if (!file.exists()) {
            return new Trends();
        }

        try (Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            return mapper.readValue(reader, Trends.class);
        } catch (JsonMappingException e) {
            throw new ValidationException(String.format("File '%s' could not be parsed as file with trends!", file), e);
        } catch (IOException e) {
            // IO problem - stop generating and re-throw the problem
            throw new ValidationException(e);
        }
    }

    private void appendCurrentReport(Trends trends) {
        Reportable result = reportResult.getFeatureReport();
        trends.addBuild(configuration.getBuildNumber(), result.getFailedFeatures(), result.getFeatures(),
                result.getFailedScenarios(), result.getScenarios(), result.getFailedSteps(), result.getSteps());
    }

    private void saveTrends(Trends trends, File file) {
        ObjectWriter objectWriter = mapper.writer().with(SerializationFeature.INDENT_OUTPUT);
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            objectWriter.writeValue(writer, trends);
        } catch (IOException e) {
            throw new ValidationException("Could not save updated trends in file: " + file.getAbsolutePath(), e);
        }
    }

    private void generateErrorPage(Exception exception) {
        LOG.info(exception);
        ErrorPage errorPage = new ErrorPage(reportResult, configuration, exception, jsonFiles);
        errorPage.generatePage();
    }

    /**
     * Due to the error with parameters in {@link #appendCurrentReport(Trends)} where total features
     * were passed instead of failures (and vice versa) following correction must be applied for trends generated
     * between release 3.0.0 and 3.1.0.
     */
    private static void applyPatchForFeatures(Trends trends) {
        for (int i = 0; i < trends.getTotalFeatures().length; i++) {
            int total = trends.getTotalFeatures()[i];
            int failures = trends.getFailedFeatures()[i];
            if (total < failures) {
                // this data must be changed since it was generated by invalid code
                int tmp = total;
                trends.getTotalFeatures()[i] = failures;
                trends.getFailedFeatures()[i] = tmp;
                LOG.debug("Value of total feature was smaller than for failed feature - fixed");
            }
        }
    }
}
