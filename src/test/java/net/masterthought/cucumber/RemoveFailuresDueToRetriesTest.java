package net.masterthought.cucumber;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.presentation.PresentationMode;
import net.masterthought.cucumber.reducers.ReducingMethod;

public class RemoveFailuresDueToRetriesTest {
    private static final String WITH_RETRIES_JSON = "with-retries.json";

    File reportDir;
    File jsonFile;
    
    @Before
    public void before() throws IOException {
        File target = new File("target");
        reportDir = new File(target, UUID.randomUUID().toString());
        reportDir.mkdirs();
        jsonFile = new File(reportDir, WITH_RETRIES_JSON);
        
        FileUtils.copyInputStreamToFile(this.getClass().getResourceAsStream("/json/" + WITH_RETRIES_JSON), jsonFile);
    }
    
    @After
    public void after() throws IOException {
        FileUtils.deleteDirectory(reportDir);
    }
    
    @Test
    public void testRetryRemoval() throws IOException {
        
        File reportOutputDirectory = reportDir;
        List<String> jsonFiles = new ArrayList<>();
        jsonFiles.add(jsonFile.getAbsolutePath());

        String buildNumber = "1";
        String projectName = "cucumberProject";

        Configuration configuration = new Configuration(reportOutputDirectory, projectName);
        // optional configuration - check javadoc for details
        configuration.addPresentationModes(PresentationMode.RUN_WITH_JENKINS);
        // do not make scenario failed when step has status SKIPPED
        configuration.setNotFailingStatuses(Collections.singleton(Status.SKIPPED));
        configuration.setBuildNumber(buildNumber);
        // addidtional metadata presented on main page
        configuration.addClassifications("Platform", "Windows");
        configuration.addClassifications("Browser", "Google Chrome");
        configuration.addClassifications("Branch", "release/1.0");
        configuration.addReducingMethod(ReducingMethod.KEEP_ONLY_LATEST_SCENARIO_RUNS);
        

        ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, configuration);
        Reportable result = reportBuilder.generateReports();
        
        Assert.assertEquals("Should not report the retried steps as failures!", 0, result.getFailedSteps());
    }
}
