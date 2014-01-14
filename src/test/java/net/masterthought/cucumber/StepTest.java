package net.masterthought.cucumber;

import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.json.Row;
import net.masterthought.cucumber.json.Step;
import net.masterthought.cucumber.util.Util;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static net.masterthought.cucumber.FileReaderUtil.getAbsolutePathFromResource;
import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class StepTest {

    Step passingStep;
    Step failingStep;
    Step skippedStep;
    Step withOutput;

    @Before
    public void setUpJsonReports() throws IOException {
        List<String> jsonReports = new ArrayList<String>();
        jsonReports.add(getAbsolutePathFromResource("net/masterthought/cucumber/project1.json"));
        ReportParser reportParser = new ReportParser(jsonReports);
        Feature passingFeature = reportParser.getFeatures().entrySet().iterator().next().getValue().get(0);
        Feature failingFeature = reportParser.getFeatures().entrySet().iterator().next().getValue().get(1);
        passingFeature.processSteps();
        failingFeature.processSteps();
        passingStep = passingFeature.getElements().first().getSteps().first();
        failingStep = failingFeature.getElements().first().getSteps().get(5);
        skippedStep = failingFeature.getElements().first().getSteps().get(6);
        withOutput = passingFeature.getElements().get(1).getSteps().first();
    }

    @Test
    public void shouldReturnRows() throws IOException {
        List<String> jsonReports = new ArrayList<String>();
        jsonReports.add(getAbsolutePathFromResource("net/masterthought/cucumber/cells.json"));
        ReportParser reportParser = new ReportParser(jsonReports);
        Feature feature = reportParser.getFeatures().entrySet().iterator().next().getValue().get(0);
        Step step = feature.getElements().get(0).getSteps().get(0);
        feature.processSteps();
        assertThat(step.getRows()[0], is(Row.class));
    }

    @Test
    public void shouldKnowIfHasRows() {
        assertThat(passingStep.hasRows(), is(false));
    }

    @Test
     public void shouldReturnOutput() {
         assertThat(withOutput.getOutput(), is("<div>some other text</div><div>wooops</div>"));
     }

    @Test
    public void shouldReturnStatus() {
        assertThat(passingStep.getStatus(), is(Util.Status.PASSED));
        assertThat(failingStep.getStatus(), is(Util.Status.FAILED));
    }

    @Test
    public void shouldReturnDuration() {
        assertThat(passingStep.getDuration(), is(107447000L));
    }

    @Test
    public void shouldReturnDataTableClass() {
        assertThat(passingStep.getDataTableClass(), is("passed"));
        assertThat(failingStep.getDataTableClass(), is("failed"));
    }

    @Test
    public void shouldReturnName() {
        assertThat(passingStep.getName(), is("<div class=\"passed\"><span class=\"step-keyword\">Given  </span><span class=\"step-name\">I have a new credit card</span></div>"
        ));
    }

    @Test
    public void shouldReturnNameWhenStepSkipped() {
        ConfigurationOptions.setSkippedFailsBuild(false);
        assertThat(skippedStep.getName(), is("<div class=\"skipped\"><span class=\"step-keyword\">And  </span><span class=\"step-name\">the card should be returned</span></div>"
        ));
    }

    @Test
    public void shouldReturnNameWhenConfigSkippedTurnedOn() {
        ConfigurationOptions.setSkippedFailsBuild(true);
        assertThat(skippedStep.getName(), is("<div class=\"failed\"><span class=\"step-keyword\">And  </span><span class=\"step-name\">the card should be returned</span><div class=\"step-error-message\"><pre>Mode: Skipped causes Failure<br/><span class=\"skipped\">This step was skipped</span></pre></div></div>"
        ));
    }

    @Test
    public void shouldNotCreateLinkToScreenshotWhenOneDoesNotExist() throws IOException {
        long screenshotTime = new DateTime().getMillis();
        DateTimeUtils.setCurrentMillisFixed(screenshotTime);
        assertThat(failingStep.getImageTags(), is(EMPTY));
    }

    @Test
    public void shouldCreateLinkToScreenshotWhenOneExists() throws IOException {
                assertThat(failingStepWithEmbeddedScreenshot().getImageTags(), is(
                "<a href=\"\" onclick=\"img=document.getElementById('16d4eeab-26ab-3bd7-a255-fb857f23474e'); img.style.display = (img.style.display == 'none' ? 'block' : 'none');return false\">Screenshot 1</a>" +
                "<img id='16d4eeab-26ab-3bd7-a255-fb857f23474e' style='display:none' src='data:image/png;base64," +
                "iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAABmJLR0QA/wD/AP+gvaeTAAAACXBI" +
                "WXMAAAsTAAALEwEAmpwYAAAAB3RJTUUH1gcBFzozgT/kfQAAAB10RVh0Q29tbWVudABDcmVhdGVk" +
                "IHdpdGggVGhlIEdJTVDvZCVuAAABgUlEQVQ4y8WTMU+UQRCGn5ldwC8GKbAywcZCKOzMNSbGGH8B" +
                "5kIiMdJRWkgDP8BrbCztoLAgGBNt7EjgriSn0dpYcHQf3x1Q3F1gZyzAky+oOWPhJps3O+/k2Z3Z" +
                "XfjfQwCqc9Wnol5z86xkqnTdZHljfePl7wDxNNFrC08WsokrEyXz4PAgW11brQF/Brh5dml0jHpj" +
                "u2RWbldw86w699DPxzWEXcQW11+/+RB/BA+Pjuj3+yVAvr/P/KP5C7u29lpT9XrjFXB9AOh0OnS7" +
                "vVJi82Pzl8eevjmNWZoalABQtNv0er2hOl+02+UeABRFMTygKC4C8jwfGpDn+c+rflxZ/Ixxy8X/" +
                "8gEJCF+iiMzcm70DQIgBVUVEcHfcHEs2mOkkYSmRkgGws/VpJlqy7bdr7++PXx4nngGCalnDuXU4" +
                "1W+tFiM69i6qyrPESfPqtUmJMaCiiAoigorAmYoKKgoIZgmP5lFDTQDu3njwPJGWcEaGql/kGHjR" +
                "+Lq58s+/8TtoKJeZGE46kQAAAABJRU5ErkJggg=='>\n" +
                "<a href=\"\" onclick=\"img=document.getElementById('9a61099d-b143-3ab7-a652-435041588fda'); img.style.display = (img.style.display == 'none' ? 'block' : 'none');return false\">Screenshot 2</a>" +
                "<img id='9a61099d-b143-3ab7-a652-435041588fda' style='display:none' src='data:image/png;base64," +
                "R0lGODlhDwAPAKECAAAAzMzM/////wAAACwAAAAADwAPAAACIISPeQHsrZ5ModrLlN48CXF8m2iQ" +
                "3YmmKqVlRtW4MLwWACH+H09wdGltaXplZCBieSBVbGVhZCBTbWFydFNhdmVyIQAAOw=='>\n"));
        DateTimeUtils.setCurrentMillisSystem();
    }

    private Step failingStepWithEmbeddedScreenshot() throws IOException {
        List<String> jsonReports = new ArrayList<String>();
        jsonReports.add(getAbsolutePathFromResource("net/masterthought/cucumber/embedded_image.json"));
        Feature failingFeatureWithEmbeddedScreenshot = new ReportParser(jsonReports).getFeatures().entrySet().iterator().next().getValue().get(0);
        failingFeatureWithEmbeddedScreenshot.processSteps();
        return failingFeatureWithEmbeddedScreenshot.getElements().get(0).getSteps().get(2);
    }
}