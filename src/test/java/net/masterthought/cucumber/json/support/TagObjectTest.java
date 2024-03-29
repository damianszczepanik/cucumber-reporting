package net.masterthought.cucumber.json.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;

import net.masterthought.cucumber.ValidationException;
import net.masterthought.cucumber.generators.integrations.PageTest;
import net.masterthought.cucumber.json.Element;
import org.apache.commons.lang3.NotImplementedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
class TagObjectTest extends PageTest {

    @BeforeEach
    void setUp() {
        setUpWithJson(SAMPLE_JSON);
    }

    @Test
    void TagObject_OnNullTagName_ThrowsException() {
        // given
        String tagName = null;

        // when & then
        assertThatThrownBy(() -> new TagObject(tagName)).isInstanceOf(ValidationException.class);
    }

    @Test
    void getName_ReturnsTagName() {

        // given
        final String refName = "yourName";
        TagObject tag = new TagObject(refName);

        // when
        String name = tag.getName();

        // then
        assertThat(name).isEqualTo(refName);
    }

    @Test
    void getReportFileName_ReturnsFileName() {

        // given
        TagObject tag = new TagObject("@client:output");

        // when
        String fileName = tag.getReportFileName();

        // then
        assertThat(fileName).isEqualTo("report-tag_1837182799.html");
    }

    @Test
    void getElements_ReturnsExactAddedElement() {

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
    void getFeatures_ThrowsException() {

        // given
        TagObject tag = new TagObject("@checkout");

        // when & then
        assertThatThrownBy(() -> tag.getFeatures()).
                isInstanceOf(NotImplementedException.class);
    }

    @Test
    void getPassedFeatures_ThrowsException() {

        // given
        TagObject tag = new TagObject("@checkout");

        // when & then
        assertThatThrownBy(() -> tag.getPassedFeatures())
                .isInstanceOf(NotImplementedException.class);
    }

    @Test
    void getFailedFeatures_ThrowsException() {

        // given
        TagObject tag = new TagObject("@checkout");

        // when & then
        assertThatThrownBy(() -> tag.getFailedFeatures())
                .isInstanceOf(NotImplementedException.class);
    }

    @Test
    void getScenarios_ReturnsSumOfScenarios() {

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
    void getPassedScenarios_ReturnsSumOfPassingScenarios() {

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
    void getFailedScenarios_ReturnsSumOfFailedScenarios() {

        // given
        TagObject tag = new TagObject("@checkout");
        Element[] elements = this.features.get(0).getElements();

        // when
        for (Element element : elements) {
            tag.addElement(element);
        }

        // then
        assertThat(tag.getFailedScenarios()).isZero();
    }

    @Test
    void getDuration_ReturnsDuration() {

        // given
        TagObject tag = new TagObject("@checkout");
        Element[] elements = this.features.get(0).getElements();

        // when
        for (Element element : elements) {
            tag.addElement(element);
        }

        // then
        assertThat(tag.getDuration()).isEqualTo(99263122889L);
        assertThat(tag.getFormattedDuration()).isEqualTo("1:39.263");
    }

    @Test
    void getSteps_ReturnsSumOfSteps() {

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
    void getNumberOfStatus_OnStatus__ReturnsSumOfStatuses() {

        // given
        TagObject tag = new TagObject("@checkout");
        Element[] elements = this.features.get(0).getElements();

        // when
        for (Element element : elements) {
            tag.addElement(element);
        }

        // then
        assertThat(tag.getNumberOfStatus(Status.PASSED)).isEqualTo(10);
        assertThat(tag.getNumberOfStatus(Status.FAILED)).isZero();
        assertThat(tag.getNumberOfStatus(Status.PENDING)).isZero();
    }

    @Test
    void getStatus_ReturnsStatus() {

        // given
        TagObject tag = new TagObject("hello");
        tag.addElement(this.features.get(0).getElements()[0]);

        // when
        Status status = tag.getStatus();

        // then
        assertThat(status).isEqualTo(Status.PASSED);
    }

    @Test
    void getRawStatus_ReturnsRawOfFinalStatus() {

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
}
