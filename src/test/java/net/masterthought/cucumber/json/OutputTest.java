package net.masterthought.cucumber.json;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

    @Test
    public void hashCode_ReturnHashCode() throws Exception {
        Output output = new Output(new String[] {"Test string"});
        assertEquals(1348945332, output.hashCode());
    }

    @Test
    public void equals_ReturnTrueSameInstance() throws Exception {
        Output output1 = new Output(new String[] {"Test string"});
        //noinspection EqualsWithItself
        assertTrue(output1.equals(output1));
    }

    @Test
    public void equals_ReturnTrueSameValue() throws Exception {
        Output output1 = new Output(new String[] {"Test string"});
        Output output2 = new Output(new String[] {"Test string"});
        assertTrue(output1.equals(output2));
    }

    @Test
    public void equals_ReturnFalseNotSameValue() throws Exception {
        Output output1 = new Output(new String[] {"Test string"});
        Output output2 = new Output(new String[] {"Test string 2"});

        assertFalse(output1.equals(output2));
    }

    @Test
    public void equals_ReturnFalseNotAnInstanceOf() throws Exception {
        Output output = new Output(new String[] {"Test string"});
        //noinspection EqualsBetweenInconvertibleTypes
        assertFalse(output.equals(new Hook()));
    }

}
