package net.masterthought.cucumber;

import java.io.File;
import java.net.URISyntaxException;

public class FileReaderUtil {

    public static String getAbsolutePathFromResource(String resource) {
        try {
            return new File(ReportInformationTest.class.getClassLoader().getResource(resource).toURI()).getAbsolutePath();
        } catch (URISyntaxException use) {
            throw new RuntimeException("could not read resource " + resource, use);
        }
    }

}
