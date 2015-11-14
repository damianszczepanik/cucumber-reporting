package net.masterthought.cucumber;

import static net.masterthought.cucumber.FileReaderUtil.getAbsolutePathFromResource;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.json.Scenario;
import net.masterthought.cucumber.json.Step;
import net.masterthought.cucumber.json.support.Status;

public class ElementTest {

    private Scenario passingElement;
    private Scenario failingElement;
    private Scenario undefinedElement;
    private Scenario skippedElement;
    private Scenario taggedElement;


    @Before
    public void setUpJsonReports() throws IOException {
        List<String> jsonReports = new ArrayList<String>();
        jsonReports.add(getAbsolutePathFromResource("net/masterthought/cucumber/project3.json"));
        Map<String, List<Feature>> features = new ReportParser().parseJsonResults(jsonReports);
        
        Feature passingFeature = features.entrySet().iterator().next().getValue().get(0);
        Feature failingFeature = features.entrySet().iterator().next().getValue().get(1);
        Feature undefinedFeature = features.entrySet().iterator().next().getValue().get(2);
        Feature skippedFeature = features.entrySet().iterator().next().getValue().get(3);

        passingFeature.processSteps();
        failingFeature.processSteps();
        undefinedFeature.processSteps();
        skippedFeature.processSteps();
        
        passingElement = passingFeature.getScenarios()[0];
        failingElement = failingFeature.getScenarios()[0];
        undefinedElement = undefinedFeature.getScenarios()[0];
        skippedElement = skippedFeature.getScenarios()[0];
        
        taggedElement = passingFeature.getScenarios()[1];
    }

    @Test
    public void shouldReturnSteps() {
        assertThat(passingElement.getSteps()[0], isA(Step.class));
    }

    @Test
    public void shouldReturnStatus() {
        assertThat(passingElement.getStatus(), is(Status.PASSED));
        assertThat(failingElement.getStatus(), is(Status.FAILED));
        assertThat(undefinedElement.getStatus(), is(Status.PASSED));
        assertThat(skippedElement.getStatus(), is(Status.PASSED));
    }

    @Test
    public void shouldReturnNameWhenConfigSkippedTurnedOn() {
        ConfigurationOptions configuration = ConfigurationOptions.instance();
        configuration.setSkippedFailsBuild(true);
    	try {
            assertThat(passingElement.getStatus(), is(Status.PASSED));
            assertThat(failingElement.getStatus(), is(Status.FAILED));
            assertThat(undefinedElement.getStatus(), is(Status.PASSED));
            assertThat(skippedElement.getStatus(), is(Status.FAILED));
    	} finally {
    		// restore the initial state for next tests
            configuration.setSkippedFailsBuild(false);
    	}
    }
    
    @Test
    public void shouldReturnNameWhenConfiUndefinedTurnedOn() {
        ConfigurationOptions configuration = ConfigurationOptions.instance();
        configuration.setUndefinedFailsBuild(true);
    	try {
            assertThat(passingElement.getStatus(), is(Status.PASSED));
            assertThat(failingElement.getStatus(), is(Status.FAILED));
            assertThat(undefinedElement.getStatus(), is(Status.FAILED));
            assertThat(skippedElement.getStatus(), is(Status.PASSED));
    	} finally {
    		// restore the initial state for next tests
            configuration.setUndefinedFailsBuild(false);
    	}
    }
    
    
    @Test
    public void shouldReturnName() {
        assertThat(passingElement.getName(), is("<div class=\"passed\"><span class=\"scenario-keyword\">Background: </span> <span class=\"scenario-name\">Activate Credit Card</span></div>"
        ));
    }

    @Test
    public void shouldReturnKeyword() {
        assertThat(passingElement.getKeyword(), is("Background"));
    }

    @Test
    public void shouldReturnType() {
        assertThat(passingElement.getType(), is("background"));
    }

    @Test
    public void shouldReturnTagList(){
        String[] expectedList = { "@fast", "@super", "@checkout" };
        for (int i = 0; i < taggedElement.getTags().length; i++) {
            assertThat(taggedElement.getTags()[i].getName(), is(expectedList[i]));
        }
    }

    @Test
    public void shouldKnowIfHasTags(){
        assertThat(taggedElement.hasTags(), is(true));
    }

    @Test
    public void shouldReturnTagsAsHtml(){
        assertThat(taggedElement.getTagsList(), is("<div class=\"feature-tags\"><a href=\"fast.html\">@fast</a>,<a href=\"super.html\">@super</a>,<a href=\"checkout.html\">@checkout</a></div>"));
    }
    
}
