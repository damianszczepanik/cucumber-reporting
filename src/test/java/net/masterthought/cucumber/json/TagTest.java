package net.masterthought.cucumber.json;

import static org.assertj.core.api.Assertions.assertThat;

import net.masterthought.cucumber.generators.integrations.PageTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
class TagTest extends PageTest {

    @BeforeEach
    void setUp() {
        setUpWithJson(SAMPLE_JSON);
    }

    @Test
    void getName_ReturnsFeatureTagName() {

        // given
        Tag tag = features.get(0).getTags()[0];

        // when
        String tagName = tag.getName();

        // then
        assertThat(tagName).isEqualTo("@featureTag");
    }

    @Test
    void getName_ReturnsElementTagName() {

        // given
        Tag tag = features.get(0).getElements()[1].getTags()[2];

        // when
        String tagName = tag.getName();

        // then
        assertThat(tagName).isEqualTo("@checkout");
    }

    @Test
    void getFileName_ReturnsTagFileName() {

        // given
        Tag tag = features.get(1).getElements()[0].getTags()[0];

        // when
        String fileName = tag.getFileName();

        // then
        assertThat(fileName).isEqualTo("report-tag_3971419525.html");
    }

    @Test
    void generateFileName_OnInvalidTagName_ReturnsValidFileName() {

        // given
        final String[] tags = {"@up s", "?any", "9/3"};
        final String[] names = {"2210183277", "2149457228", "2147539932"};

        // when & then
        for (int i = 0; i < tags.length; i++) {
            assertThat(Tag.generateFileName(tags[i])).isEqualTo(String.format("report-tag_%s.html", names[i]));
        }
    }

    @Test
    void hashCode_OnSameName_ReturnsHashCode() {

        // given
        final String tagName = "@superTaggggg";
        Tag tag = new Tag(tagName);

        // when
        int hashCode = tag.hashCode();

        // then
        assertThat(hashCode).isEqualTo(tagName.hashCode());
    }

    @Test
    void equals_OnSameName_ReturnsTrue() {

        // given
        final String tagName = "@superTaggggg";
        Tag tag1 = new Tag(tagName);
        Tag tag2 = new Tag(tagName);

        // when
        boolean isSame = tag1.equals(tag2);

        // then
        assertThat(isSame).isTrue();
    }

    @Test
    void equals_OnDifferentName_ReturnsFalse() {

        // given
        final String tagName = "@superTaggggg";
        Tag tag1 = new Tag(tagName);
        Tag tag2 = new Tag(tagName + tagName);

        // when
        boolean isSame = tag1.equals(tag2);

        // then
        assertThat(isSame).isFalse();
    }
}