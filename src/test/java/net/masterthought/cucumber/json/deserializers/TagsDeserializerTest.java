package net.masterthought.cucumber.json.deserializers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.json.Tag;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(value = JsonNode.class)
@PowerMockIgnore("jdk.internal.reflect.*")
public class TagsDeserializerTest {

    @Test
    public void deserialize_returnsTags() {

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
    public void deserialize_OnExcludedTags_returnsTags() {

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
