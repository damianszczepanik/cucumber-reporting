package net.masterthought.cucumber;

import java.io.File;
import java.net.URISyntaxException;

public class FileReaderUtil {

    public static String getAbsolutePathFromResource(String resource) {
        try {
            return new File(FileReaderUtil.class.getClassLoader().getResource(resource).toURI()).getAbsolutePath();
        } catch (URISyntaxException e) {
            throw new ValidationException("Could not read resource: " + resource, e);
        }
    }

}
