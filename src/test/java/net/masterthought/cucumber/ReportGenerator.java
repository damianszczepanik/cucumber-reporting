package net.masterthought.cucumber;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.json.support.StepObject;
import net.masterthought.cucumber.json.support.TagObject;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public abstract class ReportGenerator {

    private final static String JSON_DIRECTORY = "json/";
    private final File reportDirectory;

    protected Configuration configuration;
    private String projectName = "test cucumberProject";
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

    public void setUpWithJson(String... jsonFiles) {
        if (ArrayUtils.isNotEmpty(jsonFiles)) {
            for (String jsonFile : jsonFiles)
                addReport(jsonFile);
        }

        configuration = new Configuration(reportDirectory, projectName);
        createReportBuilder();
    }

    private void addReport(String jsonReport) {
        try {
            URL path = ReportGenerator.class.getClassLoader().getResource(JSON_DIRECTORY + jsonReport);
            jsonReports.add(new File(path.toURI()).getAbsolutePath());
        } catch (URISyntaxException e) {
            throw new ValidationException(e);
        }
    }

    private void createReportBuilder() {
        ReportParser reportParser = new ReportParser(configuration);

        List<Feature> featuresFromJson = reportParser.parseJsonResults(jsonReports);
        reportResult = new ReportResult(featuresFromJson);

        features = reportResult.getAllFeatures();
        tags = reportResult.getAllTags();
        steps = reportResult.getAllSteps();
    }
}
