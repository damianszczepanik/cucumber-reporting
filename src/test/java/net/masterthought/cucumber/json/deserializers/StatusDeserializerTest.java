package net.masterthought.cucumber.json.deserializers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import net.masterthought.cucumber.json.support.Status;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(value = JsonNode.class)
public class StatusDeserializerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void deserialize_OnDefaultStatus_ReturnsStatus() {

        // given
        Status status = Status.PASSED;
        JsonNode node = mock(JsonNode.class);
        when(node.asText()).thenReturn(status.name().toLowerCase());

        StatusDeserializer deserializer = new StatusDeserializer();

        // when
        Status newStatus = deserializer.deserialize(node, null);

        // then
        assertThat(newStatus).isEqualTo(status);
    }

    @Test
    public void deserialize_OnAdditionalStatus_ReturnsUndefinedStatus() {

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
    public void deserialize_OnUnknownStatus_ThrowsException() {

        // given
        String status = "thisIsNotStatus";
        JsonNode node = mock(JsonNode.class);
        when(node.asText()).thenReturn(status);

        StatusDeserializer deserializer = new StatusDeserializer();

        // when
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(String.format("No enum constant %s.THISISNOTSTATUS", Status.class.getName()));
        deserializer.deserialize(node, null);
    }

}
