package net.masterthought.cucumber;

import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.json.support.StepObject;
import net.masterthought.cucumber.json.support.TagObject;
import net.masterthought.cucumber.sorting.SortingMethod;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public abstract class ReportGenerator {

    public final static String JSON_DIRECTORY = "json/";
    public final static String CLASSIFICATIONS_DIRECTORY = "classifications/";

    protected static final String SAMPLE_JSON = "sample.json";
    protected static final String CUCUMBER_TIMESTAMPED_JSON = "timestamped/all-last-failed.json";
    protected static final String SAMPLE_FAILED_JSON = "sample_failed.json";
    public static final String SIMPLE_JSON = "simple.json";
    protected static final String EMPTY_JSON = "empty.json";
    protected static final String EMPTY_FILE_JSON = "empty-file.json";
    protected static final String INVALID_REPORT_JSON = "invalid-report.json";

    protected static final String EMPTY_PROPERTIES = "empty.properties";
    protected static final String SAMPLE_ONE_PROPERTIES = "sample_one.properties";
    protected static final String SAMPLE_TWO_PROPERTIES = "sample_two.properties";
    protected static final String DUPLICATE_PROPERTIES = "duplicate.properties";
    protected static final String SPECIAL_CHARACTERS_PROPERTIES = "special_characters.properties";

    protected static final File TRENDS_FILE = new File(pathToSampleFile("cucumber-trends.json"));

    private final File reportDirectory;

    protected Configuration configuration;
    private final String projectName = "test cucumberProject";
    protected final List<String> jsonReports = new ArrayList<>();
    protected final List<String> classificationFiles = new ArrayList<>();
    protected ReportResult reportResult;

    protected List<Feature> features;
    protected List<TagObject> tags;
    protected List<StepObject> steps;

    public ReportGenerator() {
        try {
            // points to target/test-classes output
            reportDirectory = new File(ReportGenerator.class.getClassLoader().getResource("").toURI());
            configuration = new Configuration(reportDirectory, projectName);
            configuration.setSortingMethod(SortingMethod.ALPHABETICAL);
            configuration.getEmbeddingDirectory().mkdirs();
        } catch (URISyntaxException e) {
            throw new ValidationException(e);
        }
    }

    protected void setUpWithJson(String... jsonFiles) {
        initWithJson(jsonFiles);
        createReport();
    }

    protected void initWithJson(String... jsonFiles) {
        if (jsonFiles != null) {
            for (String jsonFile : jsonFiles)
                jsonReports.add(reportFromResource(jsonFile));
        }
    }

    protected void initWithProperties(String... propertyFiles) {
        for (String propertyFile : propertyFiles) {
            this.classificationFiles.add(reportFromResourceProperties(propertyFile));
        }
    }

    public static String reportFromResource(String jsonReport) {
        return pathToSampleFile(JSON_DIRECTORY + jsonReport);
    }

    public static String reportFromResourceProperties(String propertyFile) {
        return pathToSampleFile(CLASSIFICATIONS_DIRECTORY + propertyFile);
    }

    public ReportResult getReportResult() {
        return reportResult;
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
        reportResult = new ReportResult(featuresFromJson, configuration);

        features = reportResult.getAllFeatures();
        tags = reportResult.getAllTags();
        steps = reportResult.getAllSteps();
    }
}
