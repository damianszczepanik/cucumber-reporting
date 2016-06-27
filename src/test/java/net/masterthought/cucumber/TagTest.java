package net.masterthought.cucumber;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import net.masterthought.cucumber.json.Tag;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class TagTest extends ReportGenerator {

    @Before
    public void setUp() {
        setUpWithJson(SAMPLE_JSON);
    }

    @Test
    public void getName_ReturnsName() {

        // give
        Tag tag = features.get(0).getElements()[1].getTags()[1];

        // when
        String name = tag.getName();

        // then
        assertThat(name).isEqualTo("@featureTag");
    }

    @Test
    public void getFileName_ReturnsFileName() {

        // given
        Tag tag = features.get(0).getTags()[0];

        // when
        String fileName = tag.getFileName();

        // then
        assertThat(fileName).isEqualTo("featureTag.html");
    }
}
