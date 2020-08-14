package net.masterthought.cucumber.outputhandlers;

import net.masterthought.cucumber.ValidationException;

import java.io.File;

/**
 * @author Florian Kaemmerer (FlorianKaemmerer@github)
 */
public interface OutputHandler {

    void handle(File file, byte[] fileContent) throws ValidationException;
}
