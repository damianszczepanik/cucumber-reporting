package net.masterthought.cucumber.sorting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Comparator;

import org.junit.Test;

import net.masterthought.cucumber.json.support.TagObject;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class TagObjectAlphabeticalComparatorTest {

    private final Comparator<TagObject> comparator = new TagObjectAlphabeticalComparator();

    @Test
    public void compareTo_OnDifferentTagName_ReturnsNoneZero() {

        // given
        TagObject tag1 = new TagObject("one");
        TagObject tag2 = new TagObject("two");

        // when
        int result = comparator.compare(tag1, tag2);

        // then
        assertThat(result).isNotEqualTo(0);
    }

    @Test
    public void compareTo_OnSameLocation_ReturnsZero() {

        // given
        TagObject tag1 = new TagObject("one");
        TagObject tag2 = new TagObject("one");

        // when
        int result = comparator.compare(tag1, tag2);

        // then
        assertThat(result).isEqualTo(0);
    }
}
