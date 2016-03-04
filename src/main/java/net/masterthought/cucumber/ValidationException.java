package net.masterthought.cucumber;

/**
 * Notifies when report cannot be generated.
 * 
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class ValidationException extends RuntimeException {

    public ValidationException(Exception e) {
        super(e);
    }
}
