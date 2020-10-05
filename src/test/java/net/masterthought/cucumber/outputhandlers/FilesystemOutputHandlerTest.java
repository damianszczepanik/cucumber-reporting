package net.masterthought.cucumber.outputhandlers;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

public class FilesystemOutputHandlerTest {

    private File outputDirectory;

    @Before
    public void setup() throws Exception {
        outputDirectory = new File("target", UUID.randomUUID().toString());
        Files.createDirectories(outputDirectory.toPath());
    }

    @After
    public void cleanup() throws Exception {
        FileUtils.deleteDirectory(outputDirectory);
    }

    @Test
    public void handle_writesGivenFileContentToGivenFile() throws Exception {
        final File filesystemOutputHandlerTestOutputFile = new File(outputDirectory, "FilesystemOutputHandler_Testoutput.txt");
        FilesystemOutputHandler fileSystemOutputHandler = new FilesystemOutputHandler();
        final String expectedOutput = "This is the FilesystemOutputHandler output";
        fileSystemOutputHandler.handle(filesystemOutputHandlerTestOutputFile, expectedOutput.getBytes(UTF_8));

        assertThat(filesystemOutputHandlerTestOutputFile).exists();

        String writtenOutput = new String(Files.readAllBytes(filesystemOutputHandlerTestOutputFile.toPath()), UTF_8);
        assertThat(writtenOutput).isEqualTo(expectedOutput);
    }
}
