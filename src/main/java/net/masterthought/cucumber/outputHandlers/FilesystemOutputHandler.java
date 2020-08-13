package net.masterthought.cucumber.outputHandlers;

import net.masterthought.cucumber.ValidationException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FilesystemOutputHandler implements OutputHandler {

    @Override
    public void handle(File reportFile, byte[] reportFileContent) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(reportFile)) {
            fileOutputStream.write(reportFileContent);
        } catch (IOException e) {
            throw new ValidationException(e);
        }
    }
}
