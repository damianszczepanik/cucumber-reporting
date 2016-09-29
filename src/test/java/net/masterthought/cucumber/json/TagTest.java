package net.masterthought.cucumber.json;

import static org.assertj.core.api.Assertions.assertThat;

import mockit.Deencapsulation;
import org.junit.Before;
import org.junit.Test;

import net.masterthought.cucumber.generators.integrations.PageTest;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class TagTest extends PageTest {

    @Before
    public void setUp() {
        setUpWithJson(SAMPLE_JSON);
    }

    @Test
    public void getName_ReturnsFeatureTagName() {

        // given
        Tag tag = features.get(0).getTags()[0];

        // when
        String tagName = tag.getName();

        // then
        assertThat(tagName).isEqualTo("@featureTag");
    }

    @Test
    public void getName_ReturnsElementTagName() {

        // given
        Tag tag = features.get(0).getElements()[1].getTags()[2];

        // when
        String tagName = tag.getName();

        // then
        assertThat(tagName).isEqualTo("@checkout");
    }

    @Test
    public void getFileName_ReturnsTagFileName() {

        // given
        Tag tag = features.get(1).getElements()[0].getTags()[0];

        // when
        String fileName = tag.getFileName();

        // then
        assertThat(fileName).isEqualTo("report-tag_checkout.html");
    }

    @Test
    public void generateFileName_OnInvalidTagName_ReturnsValidFileName() {

        // given
        final String[] tags = {"@up s", "?any", "9/3"};
        final String[] names = {"up-s", "-any", "9-3"};

        // then
        for (int i = 0; i < tags.length; i++) {
            assertThat(Tag.generateFileName(tags[i])).isEqualTo(String.format("report-tag_%s.html", names[i]));
        }
    }

    @Test
    public void hashCode_OnSameName_ReturnsHashCode() {

        // given
        final String tagName = "@superTaggggg";
        Tag tag = new Tag();
        Deencapsulation.setField(tag, "name", tagName);

        // when
        int hashCode = tag.hashCode();

        // then
        assertThat(hashCode).isEqualTo(tagName.hashCode());
    }

    @Test
    public void equals_OnSameName_ReturnsTrue() {

        // given
        final String tagName = "@superTaggggg";
        Tag tag1 = new Tag();
        Deencapsulation.setField(tag1, "name", tagName);
        Tag tag2 = new Tag();
        Deencapsulation.setField(tag2, "name", tagName);

        // when
        boolean isSame = tag1.equals(tag2);

        // then
        assertThat(isSame).isTrue();
    }

    @Test
    public void equals_OnDifferentName_ReturnsFalse() {

        // given
        final String tagName = "@superTaggggg";
        Tag tag1 = new Tag();
        Deencapsulation.setField(tag1, "name", tagName);
        Tag tag2 = new Tag();
        Deencapsulation.setField(tag2, "name", tagName + tagName);

        // when
        boolean isSame = tag1.equals(tag2);

        // then
        assertThat(isSame).isFalse();
    }
}