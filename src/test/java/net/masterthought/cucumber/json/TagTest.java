package net.masterthought.cucumber.json;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import net.masterthought.cucumber.generators.Page;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class TagTest extends Page {

    @Before
    public void setUp() {
        setUpWithJson(SAMPLE_JOSN);
    }

    @Test
    public void getNameReturnsFeatureTagName() {

        // give
        Tag tag = features.get(0).getTags()[0];

        // when
        String tagName = tag.getName();

        // then
        assertThat(tagName).isEqualTo("@featureTag");
    }

    @Test
    public void getNameReturnsElementTagName() {

        // give
        Tag tag = features.get(0).getElements()[1].getTags()[2];

        // when
        String tagName = tag.getName();

        // then
        assertThat(tagName).isEqualTo("@checkout");
    }

    @Test
    public void getFileNameReturnsTagFileName() {

        // give
        Tag tag = features.get(1).getElements()[0].getTags()[0];

        // when
        String fileName = tag.getFileName();

        // then
        assertThat(fileName).isEqualTo("checkout.html");
    }
}