package net.masterthought.cucumber.outputhandlers;

import net.masterthought.cucumber.ValidationException;

import java.io.File;

/**
 * OutputHandlers are used to handle the generated Report Files.
 * for every configured OutputHandler the handle method will be called during the generation process of the report.
 *
 * @author Florian Kaemmerer (FlorianKaemmerer@github)
 */
public interface OutputHandler {

    /**
     * For every configured OutputHandler the handle method will be called during the generation process of the report.
     *
     * @param file the file to be handled
     * @param fileContent content of the file to be handled
     * @throws ValidationException
     */
    void handle(File file, byte[] fileContent) throws ValidationException;
}
