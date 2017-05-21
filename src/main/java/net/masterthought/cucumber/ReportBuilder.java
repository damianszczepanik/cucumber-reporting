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

    /**
     * Flag used to detect if the file with updated trends is saved.
     * If the report crashes and the trends was not saved then it tries to save trends again with empty data
     * to mark that the build crashed.
     */
    private boolean wasTrendsFileSaved = false;

    public ReportBuilder(List<String> jsonFiles, Configuration configuration) {
        this.jsonFiles = jsonFiles;
        this.configuration = configuration;
        reportParser = new ReportParser(configuration);
    }

    /**
     * Parses provided files and generates the report. When generating process fails
     * report with information about error is provided.
     * @return stats for the generated report
     */
    public Reportable generateReports() {
        Trends trends = null;

        try {
            // first copy static resources so ErrorPage is displayed properly
            copyStaticResources();

            // create directory for embeddings before files are generated
            createEmbeddingsDirectory();

            // parse json files for results
            List<Feature> features = reportParser.parseJsonFiles(jsonFiles);
            reportResult = new ReportResult(features, configuration.getSoringMethod());
            Reportable reportable = reportResult.getFeatureReport();

            if (configuration.isTrendsStatsFile()) {
                // prepare data required by generators, collect generators and generate pages
                trends = updateAndSaveTrends(reportable);
            }

            List<AbstractPage> pages = collectPages(trends);
            generatePages(pages);

            return reportable;

            // whatever happens we want to provide at least error page instead of incomplete report or exception
        } catch (Exception e) {
            generateErrorPage(e);
            // update trends so there is information in history that the build failed

            // if trends was not created then something went wrong
            // and information about build failure should be saved
            if (!wasTrendsFileSaved && configuration.isTrendsStatsFile()) {
                Reportable reportable = new EmptyReportable();
                updateAndSaveTrends(reportable);
            }

            // something went wrong, don't pass result that might be incomplete
            return null;
        }
    }

    private void copyStaticResources() {
        copyResources("css", "cucumber.css", "bootstrap.min.css", "font-awesome.min.css");
        copyResources("js", "jquery.min.js", "jquery.tablesorter.min.js", "bootstrap.min.js", "Chart.min.js",
                "moment.min.js");
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

    private List<AbstractPage> collectPages(Trends trends) {
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

        if (configuration.isTrendsStatsFile()) {
            pages.add(new TrendsOverviewPage(reportResult, configuration, trends));
        }

        return pages;
    }

    private void generatePages(List<AbstractPage> pages) {
        for (AbstractPage page : pages) {
            page.generatePage();
        }
    }

    private Trends updateAndSaveTrends(Reportable reportable) {
        Trends trends = loadOrCreateTrends();
        appendToTrends(trends, reportable);

        // save updated trends so it contains all history
        saveTrends(trends, configuration.getTrendsStatsFile());

        // display only last n items - don't skip items if limit is not defined
        if (configuration.getTrendsLimit() > 0) {
            trends.limitItems(configuration.getTrendsLimit());
        }

        return trends;
    }

    private Trends loadOrCreateTrends() {
        File trendsFile = configuration.getTrendsStatsFile();
        if (trendsFile != null && trendsFile.exists()) {
            return loadTrends(trendsFile);
        } else {
            return new Trends();
        }
    }

    private static Trends loadTrends(File file) {
        try (Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            return mapper.readValue(reader, Trends.class);
        } catch (JsonMappingException e) {
            throw new ValidationException(String.format("File '%s' could not be parsed as file with trends!", file), e);
        } catch (IOException e) {
            // IO problem - stop generating and re-throw the problem
            throw new ValidationException(e);
        }
    }

    private void appendToTrends(Trends trends, Reportable result) {
        trends.addBuild(configuration.getBuildNumber(), result);
    }

    private void saveTrends(Trends trends, File file) {
        ObjectWriter objectWriter = mapper.writer().with(SerializationFeature.INDENT_OUTPUT);
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            objectWriter.writeValue(writer, trends);
            wasTrendsFileSaved = true;
        } catch (IOException e) {
            wasTrendsFileSaved = false;
            throw new ValidationException("Could not save updated trends in file: " + file.getAbsolutePath(), e);
        }
    }

    private void generateErrorPage(Exception exception) {
        LOG.info(exception);
        ErrorPage errorPage = new ErrorPage(reportResult, configuration, exception, jsonFiles);
        errorPage.generatePage();
    }
}
