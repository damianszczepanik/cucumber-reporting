package net.masterthought.cucumber;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
class ValidationExceptionTest {

    @Test
    void constructor_PassesCause() {

        // given
        Exception cause = new Exception();

        // when
        ValidationException exception = new ValidationException(cause);

        // then
        assertThat(exception).hasCause(cause);
    }

    @Test
    void constructor_PassesMessage() {

        // given
        String message = "ups...";

        // when
        ValidationException exception = new ValidationException(message);

        // then
        assertThat(exception).hasMessage(message);
    }

    @Test
    void constructor_PassesMessageAndCause() {

        // given
        Exception cause = new Exception();
        String message = "ups...";


        // when
        ValidationException exception = new ValidationException(message, cause);

        // then
        assertThat(exception)
                .hasCause(cause)
                .hasMessage(message);
    }

}
