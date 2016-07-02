package net.masterthought.cucumber.json;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import net.masterthought.cucumber.generators.integrations.PageTest;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class DocStringTest extends PageTest {

    @Before
    public void setUp() {
        setUpWithJson(SAMPLE_JSON);
    }

    @Test
    public void getName_ReturnsFeatureTagName() {

        // give
        DocString docString = features.get(0).getElements()[0].getSteps()[1].getDocString();

        // when
        String value = docString.getValue();

        // then
        assertThat(value).isEqualTo("{\n" +
                "\"issuer\": {\n" +
                "\"name\": \"Real Bank Inc.\",\n" +
                "\"isn:\": \"RB55800093842N\"\n" +
                "},\n" +
                "\"card_number\": \"4896 0215 8478 6325\",\n" +
                "\"holder\": \"A guy\"\n" +
                "}");
    }

}