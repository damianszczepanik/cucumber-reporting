package net.masterthought.cucumber.outputHandlers;

import net.masterthought.cucumber.ValidationException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class FilesystemOutputHandler implements OutputHandler {

    @Override
    public void handle(File reportFile, byte[] reportFileContent) {
        try {
            FileUtils.writeByteArrayToFile(reportFile, reportFileContent);
        } catch (IOException e) {
            throw new ValidationException(e);
        }
    }
}
