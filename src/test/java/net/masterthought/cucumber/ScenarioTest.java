package net.masterthought.cucumber;

import static net.masterthought.cucumber.FileReaderUtil.getAbsolutePathFromResource;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import net.masterthought.cucumber.json.Element;
import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.json.support.Status;

public class ScenarioTest {

    private final Configuration configuration = new Configuration(new File(""), "testProject");

    private Element passingElement;
    private Element failingElement;
    private Element undefinedElement;
    private Element skippedElement;
    private Element taggedElement;

    private void setUpJsonReports(boolean failsIfSkipped, boolean failsIFPending, boolean failsIfUndefined,
            boolean failsIfMissing) throws IOException {
        configuration.setStatusFlags(failsIfSkipped, failsIFPending, failsIfUndefined, failsIfMissing);

        List<String> jsonReports = new ArrayList<>();
        jsonReports.add(getAbsolutePathFromResource("net/masterthought/cucumber/project3.json"));
        List<Feature> features = new ReportParser(configuration).parseJsonResults(jsonReports);
        
        Feature passingFeature = features.get(0);
        Feature failingFeature = features.get(1);
        Feature undefinedFeature = features.get(2);
        Feature skippedFeature = features.get(3);
        
        passingElement = passingFeature.getElements()[0];
        failingElement = failingFeature.getElements()[0];
        undefinedElement = undefinedFeature.getElements()[0];
        skippedElement = skippedFeature.getElements()[0];
        
        taggedElement = passingFeature.getElements()[1];
    }

    @Test
    public void shouldReturnStatus() throws IOException {
        setUpJsonReports(false, false, false, false);
        assertThat(passingElement.getElementStatus()).isEqualTo(Status.PASSED);
        assertThat(failingElement.getElementStatus()).isEqualTo(Status.FAILED);
        assertThat(undefinedElement.getElementStatus()).isEqualTo(Status.PASSED);
        assertThat(skippedElement.getElementStatus()).isEqualTo(Status.PASSED);
    }

    @Test
    public void shouldReturnId() throws IOException {
        setUpJsonReports(false, false, false, false);
        assertThat(passingElement.getId()).isNull();
        assertThat(failingElement.getId())
                .isEqualTo("account-holder-withdraws-more-cash;account-has-sufficient-funds;;1");
        assertThat(undefinedElement.getId())
                .isEqualTo("account-holder-withdraws-more-cash;account-has-sufficient-funds;;2");
        assertThat(skippedElement.getId())
                .isEqualTo("account-holder-withdraws-more-cash;account-has-sufficient-funds;;3");
    }

    @Test
    public void shouldReturnNameWhenConfigSkippedTurnedOn() throws IOException {
        setUpJsonReports(true, false, false, false);

        assertThat(passingElement.getElementStatus()).isEqualTo(Status.PASSED);
        assertThat(failingElement.getElementStatus()).isEqualTo(Status.FAILED);
        assertThat(undefinedElement.getElementStatus()).isEqualTo(Status.PASSED);
        assertThat(skippedElement.getElementStatus()).isEqualTo(Status.FAILED);
    }

    @Test
    public void shouldReturnNameWhenConfiUndefinedTurnedOn() throws IOException {
        setUpJsonReports(false, false, true, false);

        assertThat(passingElement.getElementStatus()).isEqualTo(Status.PASSED);
        assertThat(failingElement.getElementStatus()).isEqualTo(Status.FAILED);
        assertThat(undefinedElement.getElementStatus()).isEqualTo(Status.FAILED);
        assertThat(skippedElement.getElementStatus()).isEqualTo(Status.PASSED);
    }

    @Test
    public void shouldReturnName() throws IOException {
        setUpJsonReports(false, false, false, false);
        assertThat(passingElement.getName()).isEqualTo("Activate Credit Card");
    }

    @Test
    public void shouldReturnKeyword() throws IOException {
        setUpJsonReports(false, false, false, false);
        assertThat(passingElement.getKeyword()).isEqualTo("Background");
    }

    @Test
    public void shouldReturnType() throws IOException {
        setUpJsonReports(false, false, false, false);
        assertThat(passingElement.getType()).isEqualTo("background");
    }

    @Test
    public void shouldReturnTagList() throws IOException {
        setUpJsonReports(false, false, false, false);
        String[] expectedList = { "@fast", "@super", "@checkout" };
        for (int i = 0; i < taggedElement.getTags().length; i++) {
            assertThat(taggedElement.getTags()[i].getName()).isEqualTo(expectedList[i]);
        }
    }

    @Test
    public void shouldKnowIfHasTags() throws IOException {
        setUpJsonReports(false, false, false, false);
        assertThat(taggedElement.hasTags()).isTrue();
    }
}
