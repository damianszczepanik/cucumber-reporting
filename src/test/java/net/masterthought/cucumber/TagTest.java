package net.masterthought.cucumber;

import static net.masterthought.cucumber.FileReaderUtil.getAbsolutePathFromResource;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.masterthought.cucumber.json.Tag;
import org.junit.Before;
import org.junit.Test;

import net.masterthought.cucumber.json.Element;
import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.json.support.TagObject;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class TagTest extends ReportGenerator{

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
