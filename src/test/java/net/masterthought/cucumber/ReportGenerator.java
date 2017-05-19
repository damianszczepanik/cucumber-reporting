package net.masterthought.cucumber;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.json.support.StepObject;
import net.masterthought.cucumber.json.support.TagObject;
import net.masterthought.cucumber.sorting.SoringMethod;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public abstract class ReportGenerator {

    public final static String JSON_DIRECTORY = "json/";

    protected static final String SAMPLE_JSON = "sample.json";
    public static final String SIMPLE_JSON = "simple.json";
    protected static final String EMPTY_JSON = "empty.json";
    protected static final String INVALID_JSON = "invalid.json";
    protected static final String INVALID_REPORT_JSON = "invalid-report.json";

    protected static final File TRENDS_FILE = new File(pathToSampleFile("cucumber-trends.json"));

    private final File reportDirectory;

    protected Configuration configuration;
    private final String projectName = "test cucumberProject";
    protected final List<String> jsonReports = new ArrayList<>();
    protected ReportResult reportResult;

    protected List<Feature> features;
    protected List<TagObject> tags;
    protected List<StepObject> steps;

    public ReportGenerator() {
        try {
            // points to target/test-classes output
            reportDirectory = new File(ReportGenerator.class.getClassLoader().getResource("").toURI());
        } catch (URISyntaxException e) {
            throw new ValidationException(e);
        }
    }

    protected void setUpWithJson(String... jsonFiles) {
        initWithJSon(jsonFiles);

        createReport();
    }

    protected void initWithJSon(String... jsonFiles) {
        if (jsonFiles != null) {
            for (String jsonFile : jsonFiles)
                jsonReports.add(reportFromResource(jsonFile));
        }

        // may be already created so don't overwrite it
        if (configuration == null) {
            configuration = new Configuration(reportDirectory, projectName);
        }
        configuration.setSortingMethod(SoringMethod.ALPHABETICAL);
        createEmbeddingsDirectory();
    }

    public static String reportFromResource(String jsonReport) {
        return pathToSampleFile(JSON_DIRECTORY + jsonReport);
    }

    protected static String pathToSampleFile(String fileName) {
        try {
            URL path = ReportGenerator.class.getClassLoader().getResource(fileName);
            return new File(path.toURI()).getAbsolutePath();
        } catch (URISyntaxException e) {
            throw new ValidationException(e);
        }
    }

    private void createReport() {
        ReportParser reportParser = new ReportParser(configuration);

        List<Feature> featuresFromJson = reportParser.parseJsonFiles(jsonReports);
        reportResult = new ReportResult(featuresFromJson, configuration.getSoringMethod());

        features = reportResult.getAllFeatures();
        tags = reportResult.getAllTags();
        steps = reportResult.getAllSteps();
    }

    private void createEmbeddingsDirectory() {
        configuration.getEmbeddingDirectory().mkdirs();
    }
}
