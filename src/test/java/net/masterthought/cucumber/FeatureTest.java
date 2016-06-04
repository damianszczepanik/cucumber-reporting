package net.masterthought.cucumber;

import static net.masterthought.cucumber.FileReaderUtil.getAbsolutePathFromResource;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.json.support.Status;

public class FeatureTest {

    private final Configuration configuration = new Configuration(new File(""), "testProject");

    private Feature passingFeature;
    private Feature failingFeature;

    @Before
    public void setUpJsonReports() throws IOException {
        List<String> jsonReports = new ArrayList<>();
        jsonReports.add(getAbsolutePathFromResource("net/masterthought/cucumber/project1.json"));
        List<Feature> features = new ReportParser(configuration).parseJsonResults(jsonReports);
        passingFeature = features.get(0);
        failingFeature = features.get(1);
    }

    @Test
    public void shouldReturnManagedFileName() {
        assertThat(passingFeature.getReportFileName()).isEqualTo("net-masterthought-example-s--ATM-local-feature.html");
    }

    @Test
    public void shouldGetDescription() {
        assertThat(passingFeature.getDescription()).isEqualTo(
                "As a Account Holder\nI want to withdraw cash from an ATM\nSo that I can get money when the bank is closed");
    }

    @Test
    public void shouldGetId() {
        assertThat(passingFeature.getId()).isEqualTo("account-holder-withdraws-cash");
    }

    @Test
    public void shouldListTheTags() {
        String name = "@featureTag";
        assertThat(passingFeature.getTags()[0].getName()).isEqualTo(name);
    }

    @Test
    public void shouldGetStatus() {
        assertThat(passingFeature.getStatus()).isEqualTo(Status.PASSED);
    }

    @Test
    public void shouldReturnName() {
        assertThat(passingFeature.getName()).isEqualTo("Account Holder withdraws cash & <MyPlace>");
    }

    @Test
    public void shouldReturnEmptyName() {
        assertThat(failingFeature.getName()).isEqualTo("");
    }

    @Test
    public void shouldReturnRawName() {
        assertThat(passingFeature.getName()).isEqualTo("Account Holder withdraws cash & <MyPlace>");
    }

    @Test
    public void shouldReturnEmptyRawName() {
        assertThat(failingFeature.getName()).isEmpty();
    }

    @Test
    public void shouldReturnRawStatus() {
        assertThat(passingFeature.getRawStatus()).isEqualTo("passed");
    }

    @Test
    public void shouldGetNumberOfSteps() {
        assertThat(passingFeature.getSteps()).isEqualTo(40);
    }

    @Test
    public void shouldGetNumberOfPassingSteps() {
        assertThat(passingFeature.getPassedSteps()).isEqualTo(40);
        assertThat(failingFeature.getPassedSteps()).isEqualTo(5);

    }

    @Test
    public void shouldGetNumberOfFailingSteps() {
        assertThat(passingFeature.getFailedSteps()).isEqualTo(0);
        assertThat(failingFeature.getFailedSteps()).isEqualTo(1);
    }

    @Test
    public void shouldGetNumberOfSkippedSteps() {
        assertThat(passingFeature.getSkippedSteps()).isEqualTo(0);
        assertThat(failingFeature.getSkippedSteps()).isEqualTo(3);
    }

    @Test
    public void shouldGetNumberOfPendingSteps() {
        assertThat(passingFeature.getPendingSteps()).isEqualTo(0);
    }

    @Test
    public void shouldGetNumberOfMissingSteps() {
        assertThat(passingFeature.getMissingSteps()).isEqualTo(0);
    }

    @Test
    public void shouldGetDuration() {
        assertThat(passingFeature.getDurations()).isEqualTo(112739000L);
    }

    @Test
    public void shouldGetFormattedDurations() {
        assertThat(passingFeature.getFormattedDurations()).contains("ms");
    }

    @Test
    public void shouldGetNumberOScenarios() {
        assertThat(passingFeature.getScenarios()).isEqualTo(4);
    }

    @Test
    public void shouldGetNumberOfPassingScenarios() {
        assertThat(passingFeature.getPassedScenarios()).isEqualTo(4);
    }

    @Test
    public void shouldGetNumberOfFailingScenarios() {
        assertThat(failingFeature.getFailedScenarios()).isEqualTo(1);
    }

}