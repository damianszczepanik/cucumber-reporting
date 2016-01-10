package net.masterthought.cucumber;

import static net.masterthought.cucumber.FileReaderUtil.getAbsolutePathFromResource;
import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.junit.Test;

import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.json.Row;
import net.masterthought.cucumber.json.Step;
import net.masterthought.cucumber.json.support.Status;

public class StepTest {

    private Step passingStep;
    private Step failingStep;
    private Step skippedStep;
    private Step withOutput;

    @Before
    public void setUpJsonReports() throws IOException {
        List<String> jsonReports = new ArrayList<String>();
        jsonReports.add(getAbsolutePathFromResource("net/masterthought/cucumber/project1.json"));
        List<Feature> features = new ReportParser().parseJsonResults(jsonReports);
        Feature passingFeature = features.get(0);
        Feature failingFeature = features.get(1);

        passingStep = passingFeature.getElements()[0].getSteps()[0];
        failingStep = failingFeature.getElements()[0].getSteps()[5];
        skippedStep = failingFeature.getElements()[0].getSteps()[6];
        withOutput = passingFeature.getElements()[1].getSteps()[0];
    }

    @Test
    public void shouldReturnRows() throws IOException {
        List<String> jsonReports = new ArrayList<String>();
        jsonReports.add(getAbsolutePathFromResource("net/masterthought/cucumber/cells.json"));
        List<Feature> features = new ReportParser().parseJsonResults(jsonReports);
        Feature feature = features.get(0);
        Step step = feature.getElements()[0].getSteps()[0];

        assertThat(step.getRows()[0], isA(Row.class));
    }

    @Test
    public void shouldReturnRowsWhenNoResultsForStep() throws IOException {
        List<String> jsonReports = new ArrayList<String>();
        jsonReports.add(getAbsolutePathFromResource("net/masterthought/cucumber/with_no_step_results.json"));
        List<Feature> features = new ReportParser().parseJsonResults(jsonReports);
        Feature feature = features.get(0);
        Step step = feature.getElements()[0].getSteps()[0];

        assertThat(step.getDetails(), is("<div class=\"missing\"><span class=\"step-keyword\">Given  </span><span class=\"step-name\">a &quot;Big&quot; customer</span><span class=\"step-duration\"></span><pre class=\"error_message\"><span class=\"missing\">Result was missing for this step</span></pre></div>"));
    }

    @Test
    public void shouldKnowIfHasRows() {
        assertThat(passingStep.hasRows(), is(false));
    }

    @Test
    public void shouldReturnOutput() {
        assertThat(withOutput.getOutput(), is(new String[] { "[\"some other text\",\"wooops\"]", "[\"matchedColumns\"]", "[]" }));
    }

    @Test
    public void shouldReturnStatus() {
        assertThat(passingStep.getStatus(), is(Status.PASSED));
        assertThat(failingStep.getStatus(), is(Status.FAILED));
    }

    @Test
    public void shouldReturnDuration() {
        assertThat(passingStep.getDuration(), is(107447000L));
    }

    @Test
    public void shouldReturnName() {
        assertThat(passingStep.getDetails(), is("<div class=\"passed\"><span class=\"step-keyword\">Given  </span><span class=\"step-name\">I have a new credit card</span><span class=\"step-duration\">107ms</span></div>"
        ));
    }

    @Test
    public void shouldReturnNameWhenStepSkipped() {
        ConfigurationOptions.instance().setSkippedFailsBuild(false);
        assertThat(
                skippedStep.getDetails(),
                is("<div class=\"skipped\"><span class=\"step-keyword\">And  </span><span class=\"step-name\">the card should be returned</span><span class=\"step-duration\">000ms</span></div>"
        ));
    }

    @Test
    public void shouldReturnNameWhenConfigSkippedTurnedOn() {
        ConfigurationOptions configuration = ConfigurationOptions.instance();
        configuration.setSkippedFailsBuild(true);
        try {
            assertThat(
                    skippedStep.getDetails(),
                    is("<div class=\"skipped\"><span class=\"step-keyword\">And  </span><span class=\"step-name\">the card should be returned</span><span class=\"step-duration\">000ms</span></div>"));
        } finally {
            // restore the initial state for next tests
            configuration.setSkippedFailsBuild(false);
        }
    }

    @Test
    public void shouldNotCreateLinkToScreenshotWhenOneDoesNotExist() throws IOException {
        long screenshotTime = new DateTime().getMillis();
        DateTimeUtils.setCurrentMillisFixed(screenshotTime);
        assertThat(failingStep.getAttachments(), is(EMPTY));
    }

    @Test
    public void shouldCreateLinkToScreenshotWhenOneExists() throws IOException {
        // ids are generated by Object.hashCode which is different each time so this must be replaced
        assertThat(failingStepWithEmbeddedScreenshot().getAttachments().replaceAll("embedding-[-\\d]+", "embedding-1234"),
                       is("<a onclick=\"attachment=document.getElementById('embedding-1234'); attachment.style.display = (attachment.style.display == 'none' ? 'block' : 'none');return false\">Attachment 1 (image)</a><a href=\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAB3RJTUUH1gcBFzozgT/kfQAAAB10RVh0Q29tbWVudABDcmVhdGVkIHdpdGggVGhlIEdJTVDvZCVuAAABgUlEQVQ4y8WTMU+UQRCGn5ldwC8GKbAywcZCKOzMNSbGGH8B5kIiMdJRWkgDP8BrbCztoLAgGBNt7EjgriSn0dpYcHQf3x1Q3F1gZyzAky+oOWPhJps3O+/k2Z3ZXfjfQwCqc9Wnol5z86xkqnTdZHljfePl7wDxNNFrC08WsokrEyXz4PAgW11brQF/Brh5dml0jHpju2RWbldw86w699DPxzWEXcQW11+/+RB/BA+Pjuj3+yVAvr/P/KP5C7u29lpT9XrjFXB9AOh0OnS7vVJi82Pzl8eevjmNWZoalABQtNv0er2hOl+02+UeABRFMTygKC4C8jwfGpDn+c+rflxZ/Ixxy8X/8gEJCF+iiMzcm70DQIgBVUVEcHfcHEs2mOkkYSmRkgGws/VpJlqy7bdr7++PXx4nngGCalnDuXU41W+tFiM69i6qyrPESfPqtUmJMaCiiAoigorAmYoKKgoIZgmP5lFDTQDu3njwPJGWcEaGql/kGHjR+Lq58s+/8TtoKJeZGE46kQAAAABJRU5ErkJggg==\"><img id=\"embedding-1234\" src=\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAB3RJTUUH1gcBFzozgT/kfQAAAB10RVh0Q29tbWVudABDcmVhdGVkIHdpdGggVGhlIEdJTVDvZCVuAAABgUlEQVQ4y8WTMU+UQRCGn5ldwC8GKbAywcZCKOzMNSbGGH8B5kIiMdJRWkgDP8BrbCztoLAgGBNt7EjgriSn0dpYcHQf3x1Q3F1gZyzAky+oOWPhJps3O+/k2Z3ZXfjfQwCqc9Wnol5z86xkqnTdZHljfePl7wDxNNFrC08WsokrEyXz4PAgW11brQF/Brh5dml0jHpju2RWbldw86w699DPxzWEXcQW11+/+RB/BA+Pjuj3+yVAvr/P/KP5C7u29lpT9XrjFXB9AOh0OnS7vVJi82Pzl8eevjmNWZoalABQtNv0er2hOl+02+UeABRFMTygKC4C8jwfGpDn+c+rflxZ/Ixxy8X/8gEJCF+iiMzcm70DQIgBVUVEcHfcHEs2mOkkYSmRkgGws/VpJlqy7bdr7++PXx4nngGCalnDuXU41W+tFiM69i6qyrPESfPqtUmJMaCiiAoigorAmYoKKgoIZgmP5lFDTQDu3njwPJGWcEaGql/kGHjR+Lq58s+/8TtoKJeZGE46kQAAAABJRU5ErkJggg==\" style=\"max-width:250px; display:none;\"/></a></br>"
                        + "<a onclick=\"attachment=document.getElementById('embedding-1234'); attachment.style.display = (attachment.style.display == 'none' ? 'block' : 'none');return false\">Attachment 2 (image)</a><a href=\"data:image/png;base64,R0lGODlhDwAPAKECAAAAzMzM/////wAAACwAAAAADwAPAAACIISPeQHsrZ5ModrLlN48CXF8m2iQ3YmmKqVlRtW4MLwWACH+H09wdGltaXplZCBieSBVbGVhZCBTbWFydFNhdmVyIQAAOw==\"><img id=\"embedding-1234\" src=\"data:image/png;base64,R0lGODlhDwAPAKECAAAAzMzM/////wAAACwAAAAADwAPAAACIISPeQHsrZ5ModrLlN48CXF8m2iQ3YmmKqVlRtW4MLwWACH+H09wdGltaXplZCBieSBVbGVhZCBTbWFydFNhdmVyIQAAOw==\" style=\"max-width:250px; display:none;\"/></a></br>"
                        + "<a onclick=\"attachment=document.getElementById('embedding-1234'); attachment.style.display = (attachment.style.display == 'none' ? 'block' : 'none');return false\">Attachment 3 (plain text)</a><pre id=\"embedding-1234\" style=\"max-width:250px; display:none;\">java.lang.Throwable</pre><br>"
                        + "<a onclick=\"attachment=document.getElementById('embedding-1234'); attachment.style.display = (attachment.style.display == 'none' ? 'block' : 'none');return false\">Attachment 4 (HTML text)</a><span id=\"embedding-1234\" style=\"max-width:250px; display:none;\"><i>Hello</i> <b>World!<b></span><br><span style=\"color:red\">Attachment number 4, has unsupported mimetype 'unknown'.<br>File the bug <a href=\"https://github.com/damianszczepanik/cucumber-reporting/issues\">here</a> so support will be added!</span>"));
    }

    private Step failingStepWithEmbeddedScreenshot() throws IOException {
        List<String> jsonReports = new ArrayList<String>();
        jsonReports.add(getAbsolutePathFromResource("net/masterthought/cucumber/embedded_image.json"));
        List<Feature> features = new ReportParser().parseJsonResults(jsonReports);
        Feature failingFeatureWithEmbeddedScreenshot = features.get(0);
        return failingFeatureWithEmbeddedScreenshot.getElements()[0].getSteps()[2];
    }
}