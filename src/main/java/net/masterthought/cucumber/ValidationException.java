package net.masterthought.cucumber;

/**
 * Thrown when report cannot be generated.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class ValidationException extends RuntimeException {

    public ValidationException(Exception e) {
        super(e);
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable exception) {
        super(message, exception);
    }
}
