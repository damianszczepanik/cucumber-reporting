package net.masterthought.cucumber.json.deserializers;

import com.fasterxml.jackson.databind.JsonNode;
import net.masterthought.cucumber.Configuration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(value = JsonNode.class)
@PowerMockIgnore("jdk.internal.reflect.*")
public class CommentsDeserializerTest {

    @Test
    public void deserialize_returnsCommentsFromArrayOfString() {
        checkCommentDeserialization(false);
    }

    @Test
    public void deserialize_returnsCommentsFromArrayOfCommentObject() {
        checkCommentDeserialization(true);
    }

    @Test
    public void deserialize_returnsEmptyCommentsWhenOtherFormat() {
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
        assertThat(comments).hasSize(0);
    }

    private void checkCommentDeserialization(boolean isObject) {
        // given
        CommentsDeserializer commentsDeserializer = new CommentsDeserializer();

        String comment = "# Comment";
        JsonNode commentNode = buildNode(comment, isObject);

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


    private JsonNode buildNode(String comment, boolean isObject) {
        JsonNode commentNode = mock(JsonNode.class);
        JsonNode commentValue = mock(JsonNode.class);
        if (isObject) {
            when(commentNode.isTextual()).thenReturn(false);
            when(commentNode.isObject()).thenReturn(true);
            when(commentNode.has("value")).thenReturn(true);
            when(commentNode.get("value")).thenReturn(commentValue);
            when(commentValue.asText()).thenReturn(comment);
        } else {
            when(commentNode.isTextual()).thenReturn(true);
            when(commentNode.asText()).thenReturn(comment);
        }

        return commentNode;
    }
}
