package net.masterthought.cucumber.outputHandlers;

import net.masterthought.cucumber.ValidationException;

import java.io.File;

public interface OutputHandler {

    void handle(File reportFile, byte[] reportFileContent) throws ValidationException;
}
