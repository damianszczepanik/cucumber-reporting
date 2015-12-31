package net.masterthought.cucumber;

import static net.masterthought.cucumber.FileReaderUtil.getAbsolutePathFromResource;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.json.Element;
import net.masterthought.cucumber.json.Step;
import net.masterthought.cucumber.json.support.Status;

public class ScenarioTest {

    private Element passingElement;
    private Element failingElement;
    private Element undefinedElement;
    private Element skippedElement;
    private Element taggedElement;


    @Before
    public void setUpJsonReports() throws IOException {
        List<String> jsonReports = new ArrayList<String>();
        jsonReports.add(getAbsolutePathFromResource("net/masterthought/cucumber/project3.json"));
        List<Feature> features = new ReportParser().parseJsonResults(jsonReports);
        
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
    public void shouldReturnId() {
        assertThat(passingElement.getId(), is((String) null));
        assertThat(failingElement.getId(), is("account-holder-withdraws-more-cash;account-has-sufficient-funds;;1"));
        assertThat(undefinedElement.getId(), is("account-holder-withdraws-more-cash;account-has-sufficient-funds;;2"));
        assertThat(skippedElement.getId(), is("account-holder-withdraws-more-cash;account-has-sufficient-funds;;3"));
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
        assertThat(passingElement.getName(), is("<div class=\"passed\"><span class=\"element-keyword\">Background: </span><span class=\"element-name\">Activate Credit Card</span></div>"
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
