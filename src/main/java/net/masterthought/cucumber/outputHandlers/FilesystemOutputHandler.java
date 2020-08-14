package net.masterthought.cucumber.outputHandlers;

import net.masterthought.cucumber.ValidationException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author Florian Kaemmerer (FlorianKaemmerer@github)
 */
public class FilesystemOutputHandler implements OutputHandler {

    @Override
    public void handle(File file, byte[] fileContent) {
        try {
            FileUtils.writeByteArrayToFile(file, fileContent);
        } catch (IOException e) {
            throw new ValidationException(e);
        }
    }
}
