package net.masterthought.cucumber;

import static net.masterthought.cucumber.FileReaderUtil.getAbsolutePathFromResource;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import net.masterthought.cucumber.json.Element;
import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.json.Step;
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
    public void shouldReturnSteps() throws IOException {
        setUpJsonReports(false, false, false, false);
        assertThat(passingElement.getSteps()[0], isA(Step.class));
    }

    @Test
    public void shouldReturnStatus() throws IOException {
        setUpJsonReports(false, false, false, false);
        assertThat(passingElement.getStatus(), is(Status.PASSED));
        assertThat(failingElement.getStatus(), is(Status.FAILED));
        assertThat(undefinedElement.getStatus(), is(Status.PASSED));
        assertThat(skippedElement.getStatus(), is(Status.PASSED));
    }

    @Test
    public void shouldReturnId() throws IOException {
        setUpJsonReports(false, false, false, false);
        assertThat(passingElement.getId(), is((String) null));
        assertThat(failingElement.getId(), is("account-holder-withdraws-more-cash;account-has-sufficient-funds;;1"));
        assertThat(undefinedElement.getId(), is("account-holder-withdraws-more-cash;account-has-sufficient-funds;;2"));
        assertThat(skippedElement.getId(), is("account-holder-withdraws-more-cash;account-has-sufficient-funds;;3"));
    }

    @Test
    public void shouldReturnNameWhenConfigSkippedTurnedOn() throws IOException {
        setUpJsonReports(true, false, false, false);

        assertThat(passingElement.getStatus(), is(Status.PASSED));
        assertThat(failingElement.getStatus(), is(Status.FAILED));
        assertThat(undefinedElement.getStatus(), is(Status.PASSED));
        assertThat(skippedElement.getStatus(), is(Status.FAILED));
    }

    @Test
    public void shouldReturnNameWhenConfiUndefinedTurnedOn() throws IOException {
        setUpJsonReports(false, false, true, false);

        assertThat(passingElement.getStatus(), is(Status.PASSED));
        assertThat(failingElement.getStatus(), is(Status.FAILED));
        assertThat(undefinedElement.getStatus(), is(Status.FAILED));
        assertThat(skippedElement.getStatus(), is(Status.PASSED));
    }

    @Test
    public void shouldReturnName() throws IOException {
        setUpJsonReports(false, false, false, false);
        assertThat(passingElement.getName(), is("Activate Credit Card"));
    }

    @Test
    public void shouldReturnKeyword() throws IOException {
        setUpJsonReports(false, false, false, false);
        assertThat(passingElement.getKeyword(), is("Background"));
    }

    @Test
    public void shouldReturnType() throws IOException {
        setUpJsonReports(false, false, false, false);
        assertThat(passingElement.getType(), is("background"));
    }

    @Test
    public void shouldReturnTagList() throws IOException {
        setUpJsonReports(false, false, false, false);
        String[] expectedList = { "@fast", "@super", "@checkout" };
        for (int i = 0; i < taggedElement.getTags().length; i++) {
            assertThat(taggedElement.getTags()[i].getName(), is(expectedList[i]));
        }
    }

    @Test
    public void shouldKnowIfHasTags() throws IOException {
        setUpJsonReports(false, false, false, false);
        assertThat(taggedElement.hasTags(), is(true));
    }

    @Test
    public void shouldReturnTagsAsHtml() throws IOException {
        setUpJsonReports(false, false, false, false);
        assertThat(taggedElement.getTagsList(), is("<div class=\"feature-tags\"><a href=\"fast.html\">@fast</a>, <a href=\"super.html\">@super</a>, <a href=\"checkout.html\">@checkout</a></div>"));
    }
    
}
