package net.masterthought.cucumber;

import net.masterthought.cucumber.json.Element;
import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.json.Step;
import net.masterthought.cucumber.util.Util;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static net.masterthought.cucumber.FileReaderUtil.getAbsolutePathFromResource;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ElementTest {

    ReportParser reportParser;
    Element passingElement;
    Element failingElement;
    Element taggedElement;


    @Before
    public void setUpJsonReports() throws IOException {
        List<String> jsonReports = new ArrayList<String>();
        jsonReports.add(getAbsolutePathFromResource("net/masterthought/cucumber/project1.json"));
        reportParser = new ReportParser(jsonReports);
        Feature passingFeature = reportParser.getFeatures().entrySet().iterator().next().getValue().get(0);
        Feature failingFeature = reportParser.getFeatures().entrySet().iterator().next().getValue().get(1);
        passingFeature.processSteps();
        failingFeature.processSteps();
        passingElement = passingFeature.getElements()[0];
        failingElement = failingFeature.getElements()[0];
        taggedElement = passingFeature.getElements()[1];

    }

    @Test
    public void shouldReturnSteps() {
        assertThat(passingElement.getSteps()[0], is(Step.class));
    }

    @Test
    public void shouldReturnStatus() {
        assertThat(passingElement.getStatus(), is(Util.Status.PASSED));
        assertThat(failingElement.getStatus(), is(Util.Status.FAILED));
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
    public void shouldReturnTagList(){
        List<String> expectedList = new ArrayList<String>();
        expectedList.add("@fast");
        expectedList.add("@super");
        expectedList.add("@checkout");
        assertThat(taggedElement.getTagList(), is(expectedList));
    }

    @Test
    public void shouldKnowIfHasTags(){
        assertThat(taggedElement.hasTags(), is(true));
    }

    @Test
    public void shouldReturnTagsAsHtml(){
        assertThat(taggedElement.getTags(), is("<div class=\"feature-tags\">@fast,@super,@checkout</div>"));
    }

}
