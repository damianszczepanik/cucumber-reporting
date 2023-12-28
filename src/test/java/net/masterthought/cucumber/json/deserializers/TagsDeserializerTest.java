package net.masterthought.cucumber.json.deserializers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.json.Tag;
import org.junit.jupiter.api.Test;

class TagsDeserializerTest {

    @Test
    void deserialize_returnsTags() {

        // given
        TagsDeserializer tagsDeserializer = new TagsDeserializer();

        String tagName = "@bestTag";
        JsonNode tagNode = buildNode(tagName);

        JsonNode rootNode = mock(JsonNode.class);
        List<JsonNode> tagNodes = new ArrayList();
        tagNodes.add(tagNode);
        when(rootNode.iterator()).thenReturn(tagNodes.iterator());

        Configuration configuration = new Configuration(null, null);

        // when
        Tag[] tags = tagsDeserializer.deserialize(rootNode, configuration);

        // then
        assertThat(tags).hasSize(1);
        assertThat(tags[0].getName()).isEqualTo(tagName);
    }

    @Test
    void deserialize_OnExcludedTags_returnsTags() {

        // given
        TagsDeserializer tagsDeserializer = new TagsDeserializer();

        String tagName = "@bestTag";
        JsonNode tagNode = buildNode(tagName);
        String excludedTagName = "@superTag";
        JsonNode excludedTagNode = buildNode(excludedTagName);

        JsonNode rootNode = mock(JsonNode.class);
        List<JsonNode> tagNodes = new ArrayList();
        tagNodes.add(tagNode);
        tagNodes.add(excludedTagNode);
        when(rootNode.iterator()).thenReturn(tagNodes.iterator());

        Configuration configuration = new Configuration(null, null);
        configuration.setTagsToExcludeFromChart(excludedTagName);

        // when
        Tag[] tags = tagsDeserializer.deserialize(rootNode, configuration);

        // then
        assertThat(tags).hasSize(1);
        assertThat(tags[0].getName()).isEqualTo(tagName);
    }

    private JsonNode buildNode(String tagName) {
        JsonNode tagNode = mock(JsonNode.class);
        JsonNode tagValue = mock(JsonNode.class);
        when(tagValue.asText()).thenReturn(tagName);
        when(tagNode.get("name")).thenReturn(tagValue);

        return tagNode;
    }
}
