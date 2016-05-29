package net.masterthought.cucumber.json.support;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import net.masterthought.cucumber.ValidationException;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class TagObjectTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void TagObjectOnNullTagNameThrowsException() {
        // given
        // nothing

        // then
        thrown.expect(ValidationException.class);
        new TagObject(null);
    }

    @Test
    public void getNameReturnsTagName() {

        // given
        final String refName = "yourName";
        TagObject tag = new TagObject(refName);

        // when
        String name = tag.getName();

        // then
        assertThat(name).isEqualTo(refName);
    }

    @Test
    public void getReportFileNameReturnsFileName() {

        // given
        TagObject tag = new TagObject("@client:output");

        // when
        String fileName = tag.getReportFileName();

        // then
        assertThat(fileName).isEqualTo("client-output.html");
    }

    @Test
    public void compareToOnDifferentTagNameReturnsNoneZero() {

        // given
        TagObject tag1 = new TagObject("one");
        TagObject tag2 = new TagObject("two");

        // when
        int result = tag1.compareTo(tag2);

        // then
        assertThat(result).isNotEqualTo(0);
    }

    @Test
    public void compareToOnSameLocationReturnsZero() {

        // given
        TagObject tag1 = new TagObject("one");
        TagObject tag2 = new TagObject("one");

        // when
        int result = tag1.compareTo(tag2);

        // then
        assertThat(result).isEqualTo(0);
    }
}
