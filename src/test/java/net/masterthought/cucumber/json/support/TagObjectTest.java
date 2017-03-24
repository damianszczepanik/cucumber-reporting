package net.masterthought.cucumber.json.support;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.masterthought.cucumber.json.Feature;
import org.apache.commons.lang.NotImplementedException;
import org.junit.Assert;
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
    public void getDurations_ReturnsDurations() {

        // given
        TagObject tag = new TagObject("@checkout");
        Element[] elements = this.features.get(0).getElements();

        // when
        for (Element element : elements) {
            tag.addElement(element);
        }

        // then
        assertThat(tag.getDurations()).isEqualTo(99263122889L);
        assertThat(tag.getFormattedDurations()).isEqualTo("1m 39s 263ms");
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

    @Test
    public void compareTo_OnDifferentTagName_ReturnsNoneZero() {

        // given
        TagObject tag1 = new TagObject("one");
        TagObject tag2 = new TagObject("two");

        // when
        int result = tag1.compareTo(tag2);

        // then
        assertThat(result).isNotEqualTo(0);
    }

    @Test
    public void compareTo_OnSameLocation_ReturnsZero() {

        // given
        TagObject tag1 = new TagObject("one");
        TagObject tag2 = new TagObject("one");

        // when
        int result = tag1.compareTo(tag2);

        // then
        assertThat(result).isEqualTo(0);
    }

    @Test
    public void getFailedCause_ReturnsFailuresMap() {

        String expectedFailedScenarioName = "Account may not have sufficient funds";
        String expectedFailedStepName = "the card is valid";
        String expectedFailedStepResultErrorMessage = "Error message not found.";
        String[] expectedFailureData = {
                expectedFailedScenarioName,
                expectedFailedStepName,
                expectedFailedStepResultErrorMessage
        };
        Map<String, String[]> expectedFailedScenariosMap = new HashMap<>(1);
        expectedFailedScenariosMap.put("0", expectedFailureData);

        // given
        TagObject failedTag = new TagObject("@checkout");
        Element[] elements = this.features.get(1).getElements();

        // when
        for (Element element : elements) {
            failedTag.addElement(element);
        }

        Map<String, String[]> returnedFailedScenariosMap = failedTag.getFailedCause();

        // first validate that both expected and returned maps are the same size
        Assert.assertEquals(returnedFailedScenariosMap.size(), expectedFailedScenariosMap.size());

        String[] expectedFailedScenariosMapKeys = new String[expectedFailedScenariosMap.size()];
        expectedFailedScenariosMap.keySet().toArray(expectedFailedScenariosMapKeys);

        String[] returnedFailedScenariosMapKeys = new String[returnedFailedScenariosMap.size()];
        returnedFailedScenariosMap.keySet().toArray(returnedFailedScenariosMapKeys);

        // then validate that the expected and returned map keys are equal
        Assert.assertArrayEquals(returnedFailedScenariosMapKeys, expectedFailedScenariosMapKeys);

        String[][] expectedFailedScenariosMapValues = new String[expectedFailedScenariosMap.values().size()][2];
        expectedFailedScenariosMap.values().toArray(expectedFailedScenariosMapValues);

        String[][] returnedFailedScenariosMapValues = new String[returnedFailedScenariosMap.values().size()][2];
        returnedFailedScenariosMap.values().toArray(returnedFailedScenariosMapValues);

        // finally validate that the expected and returned map values are equal
        Assert.assertArrayEquals(returnedFailedScenariosMapValues, expectedFailedScenariosMapValues);
    }
}
