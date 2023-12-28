package net.masterthought.cucumber.json.deserializers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Locale;

import com.fasterxml.jackson.databind.JsonNode;
import net.masterthought.cucumber.json.support.Status;
import org.junit.jupiter.api.Test;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
class StatusDeserializerTest {

    @Test
    void deserialize_OnDefaultStatus_ReturnsStatus() {

        // given
        Status status = Status.PASSED;
        JsonNode node = mock(JsonNode.class);
        when(node.asText()).thenReturn(status.name().toLowerCase(Locale.ENGLISH));

        StatusDeserializer deserializer = new StatusDeserializer();

        // when
        Status newStatus = deserializer.deserialize(node, null);

        // then
        assertThat(newStatus).isEqualTo(status);
    }

    @Test
    void deserialize_OnFailedStatus_ReturnsStatus() {

        // given
        Status status = Status.FAILED;
        JsonNode node = mock(JsonNode.class);
        when(node.asText()).thenReturn(status.name().toLowerCase(Locale.ENGLISH));

        StatusDeserializer deserializer = new StatusDeserializer();

        // when
        Status newStatus = deserializer.deserialize(node, null);

        // then
        assertThat(newStatus).isEqualTo(status);
    }


    @Test
    void deserialize_OnAdditionalStatus_ReturnsUndefinedStatus() {

        // given
        Status status = Status.UNDEFINED;
        JsonNode node = mock(JsonNode.class);
        // test any status from supported list
        when(node.asText()).thenReturn(StatusDeserializer.UNKNOWN_STATUSES.get(0));

        StatusDeserializer deserializer = new StatusDeserializer();

        // when
        Status newStatus = deserializer.deserialize(node, null);

        // then
        assertThat(newStatus).isEqualTo(status);
    }

    @Test
    void deserialize_OnUnknownStatus_ThrowsException() {

        // given
        String status = "thisIsNotStatus";
        JsonNode node = mock(JsonNode.class);
        when(node.asText()).thenReturn(status);

        StatusDeserializer deserializer = new StatusDeserializer();

        // when & then
        assertThatThrownBy(() -> deserializer.deserialize(node, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(String.format("No enum constant %s.THISISNOTSTATUS", Status.class.getName()));
    }
}
