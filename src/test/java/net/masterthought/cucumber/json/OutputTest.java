package net.masterthought.cucumber.json;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class OutputTest {

    @Test
    public void getMessages_ReturnsMessages() {

        // given
        String[] messages = { "a", "b", "c", "a" };

        // when
        Output output = new Output(messages);

        // then
        assertThat(output.getMessages()).containsExactly(messages);
    }

}
