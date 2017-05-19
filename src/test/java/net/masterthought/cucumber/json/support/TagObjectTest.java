package net.masterthought.cucumber.json.support;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.apache.commons.lang.NotImplementedException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import net.masterthought.cucumber.ValidationException;
import net.masterthought.cucumber.generators.integrations.PageTest;
import net.masterthought.cucumber.json.Element;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class TagObjectTest extends PageTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        setUpWithJson(SAMPLE_JSON);
    }

    @Test
    public void TagObject_OnNullTagName_ThrowsException() {
        // given
        // nothing

        // then
        thrown.expect(ValidationException.class);
        new TagObject(null);
    }

    @Test
    public void getName_ReturnsTagName() {

        // given
        final String refName = "yourName";
        TagObject tag = new TagObject(refName);

        // when
        String name = tag.getName();

        // then
        assertThat(name).isEqualTo(refName);
    }

    @Test
    public void getReportFileName_ReturnsFileName() {

        // given
        TagObject tag = new TagObject("@client:output");

        // when
        String fileName = tag.getReportFileName();

        // then
        assertThat(fileName).isEqualTo("report-tag_client-output.html");
    }

    @Test
    public void getElements_ReturnsExactAddedElement() {

        // given
        TagObject tag = new TagObject("@checkout");
        Element[] elements = this.features.get(0).getElements();

        // when
        for (Element element : elements) {
            tag.addElement(element);
        }

        // then
        assertThat(tag.getElements().toArray()).hasSameElementsAs(Arrays.asList(elements));
    }

    @Test
    public void getFeatures_ThrowsException() {

        // given
        TagObject tag = new TagObject("@checkout");

        // then
        thrown.expect(NotImplementedException.class);
        tag.getFeatures();
    }

    @Test
    public void getPassedFeatures_ThrowsException() {

        // given
        TagObject tag = new TagObject("@checkout");

        // then
        thrown.expect(NotImplementedException.class);
        tag.getPassedFeatures();
    }

    @Test
    public void getFailedFeatures_ThrowsException() {

        // given
        TagObject tag = new TagObject("@checkout");

        // then
        thrown.expect(NotImplementedException.class);
        tag.getFailedFeatures();
    }

    @Test
    public void getScenarios_ReturnsSumOfScenarios() {

        // given
        TagObject tag = new TagObject("@checkout");
        Element[] elements = this.features.get(0).getElements();

        // when
        for (Element element : elements) {
            tag.addElement(element);
        }

        // then
        assertThat(tag.getScenarios()).isEqualTo(elements.length - 1);
    }

    @Test
    public void getPassedScenarios_ReturnsSumOfPassingScenarios() {

        // given
        TagObject tag = new TagObject("@checkout");
        Element[] elements = this.features.get(0).getElements();

        // when
        for (Element element : elements) {
            tag.addElement(element);
        }

        // then
        assertThat(tag.getPassedScenarios()).isEqualTo(2);
    }

    @Test
    public void getFailedScenarios_ReturnsSumOfFailedScenarios() {

        // given
        TagObject tag = new TagObject("@checkout");
        Element[] elements = this.features.get(0).getElements();

        // when
        for (Element element : elements) {
            tag.addElement(element);
        }

        // then
        assertThat(tag.getFailedScenarios()).isEqualTo(0);
    }

    @Test
    public void getDuration_ReturnsDuration() {

        // given
        TagObject tag = new TagObject("@checkout");
        Element[] elements = this.features.get(0).getElements();

        // when
        for (Element element : elements) {
            tag.addElement(element);
        }

        // then
        assertThat(tag.getDuration()).isEqualTo(99263122889L);
        assertThat(tag.getFormattedDuration()).isEqualTo("1m 39s 263ms");
    }

    @Test
    public void getSteps_ReturnsSumOfSteps() {

        // given
        TagObject tag = new TagObject("@checkout");
        Element[] elements = this.features.get(0).getElements();

        // when
        for (Element element : elements) {
            tag.addElement(element);
        }

        // then
        assertThat(tag.getSteps()).isEqualTo(10);
    }

    @Test
    public void getNumberOfStatus_OnStatus__ReturnsSumOfStatuses() {

        // given
        TagObject tag = new TagObject("@checkout");
        Element[] elements = this.features.get(0).getElements();

        // when
        for (Element element : elements) {
            tag.addElement(element);
        }

        // then
        assertThat(tag.getNumberOfStatus(Status.PASSED)).isEqualTo(10);
        assertThat(tag.getNumberOfStatus(Status.FAILED)).isEqualTo(0);
        assertThat(tag.getNumberOfStatus(Status.PENDING)).isEqualTo(0);
    }

    @Test
    public void getStatus_ReturnsStatus() {

        // given
        TagObject tag = new TagObject("hello");
        tag.addElement(this.features.get(0).getElements()[0]);

        // when
        Status status = tag.getStatus();

        // then
        assertThat(status).isEqualTo(Status.PASSED);
    }

    @Test
    public void getRawStatus_ReturnsRawOfFinalStatus() {

        // given
        TagObject tag = new TagObject("@checkout");
        Element[] elements = this.features.get(0).getElements();

        // when
        for (Element element : elements) {
            tag.addElement(element);
        }

        // then
        assertThat(tag.getRawStatus()).isEqualTo(Status.PASSED.getRawName());
    }

    @Test
    public void getDeviceName_ThrowsException() {

        // given
        TagObject tag = new TagObject("@checkout");

        // then
        thrown.expect(NotImplementedException.class);
        tag.getDeviceName();
    }
}
