package net.masterthought.cucumber.json.deserializers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import net.masterthought.cucumber.Configuration;
import org.junit.jupiter.api.Test;

class CommentsDeserializerTest {

    @Test
    void deserialize_returnsCommentsFromArrayOfString() {
        // given
        CommentsDeserializer commentsDeserializer = new CommentsDeserializer();

        String comment = "# Comment";
        JsonNode commentNode = mock(JsonNode.class);
        JsonNode commentValue = mock(JsonNode.class);
        when(commentNode.isTextual()).thenReturn(false);
        when(commentNode.isObject()).thenReturn(true);
        when(commentNode.has("value")).thenReturn(true);
        when(commentNode.get("value")).thenReturn(commentValue);
        when(commentValue.asText()).thenReturn(comment);

        JsonNode rootNode = mock(JsonNode.class);
        List<JsonNode> commentsNodes = new ArrayList<>();
        commentsNodes.add(commentNode);
        when(rootNode.iterator()).thenReturn(commentsNodes.iterator());

        Configuration configuration = new Configuration(null, null);

        // when
        List<String> comments = commentsDeserializer.deserialize(rootNode, configuration);

        // then
        assertThat(comments).hasSize(1);
        assertThat(comments.get(0)).isEqualTo(comment);
    }

    @Test
    void deserialize_returnsCommentsFromArrayOfCommentObject() {
        // given
        CommentsDeserializer commentsDeserializer = new CommentsDeserializer();

        String comment = "# Comment";
        JsonNode commentNode = mock(JsonNode.class);
        when(commentNode.isTextual()).thenReturn(true);
        when(commentNode.asText()).thenReturn(comment);

        JsonNode rootNode = mock(JsonNode.class);
        List<JsonNode> commentsNodes = new ArrayList<>();
        commentsNodes.add(commentNode);
        when(rootNode.iterator()).thenReturn(commentsNodes.iterator());

        Configuration configuration = new Configuration(null, null);

        // when
        List<String> comments = commentsDeserializer.deserialize(rootNode, configuration);

        // then
        assertThat(comments).hasSize(1);
        assertThat(comments.get(0)).isEqualTo(comment);
    }

    @Test
    void deserialize_returnsEmptyCommentsWhenOtherFormat() {
        // given
        CommentsDeserializer commentsDeserializer = new CommentsDeserializer();

        JsonNode commentNode = mock(JsonNode.class);
        when(commentNode.isTextual()).thenReturn(false);
        when(commentNode.isObject()).thenReturn(true);
        when(commentNode.has("value")).thenReturn(false);

        JsonNode rootNode = mock(JsonNode.class);
        List<JsonNode> commentsNodes = new ArrayList<>();
        commentsNodes.add(commentNode);
        when(rootNode.iterator()).thenReturn(commentsNodes.iterator());

        Configuration configuration = new Configuration(null, null);

        // when
        List<String> comments = commentsDeserializer.deserialize(rootNode, configuration);

        // then
        assertThat(comments).isEmpty();
    }

}
