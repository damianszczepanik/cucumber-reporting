package net.masterthought.cucumber.generators.integrations;

import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.ValidationException;
import net.masterthought.cucumber.generators.FeaturesOverviewPage;
import net.masterthought.cucumber.outputhandlers.OutputHandler;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class OutputHandlerConfigurationIntegrationTest extends PageTest {

    private final InMemoryOutputHandler inMemoryOutputHandler = new InMemoryOutputHandler();

    @Test
    public void addOutputHandler_addedOutputHandlerIsUsedDuringReportGeneration() {
        // Given
        // We want the default filesystemOutputHandler and a inMemoryHandler,
        // to assert that both Handlers are used and called with the same files.
        // So we add an inMemoryHandler
        configuration.addOutputHandler(inMemoryOutputHandler);

        //When
        setUpWithJson(SAMPLE_JSON);
        page = new FeaturesOverviewPage(reportResult, configuration);
        page.generatePage();

        //Then
        Map<File, byte[]> files = inMemoryOutputHandler.getFiles();

        // The FilesystemOutputHandler writes the Files to the filesystem,
        // to assert if all files that are handled by the inMemoryHandler are also
        // handled by the filesystemHandler we assert if the files exist on the filesystem.
        assertThat(files.keySet().stream().allMatch(File::exists)).isTrue();
    }


    private static class InMemoryOutputHandler implements OutputHandler {
        private final Map<File, byte[]> files = new HashMap<>();

        @Override
        public void handle(File file, byte[] fileContent) throws ValidationException {
            files.put(file, fileContent);
        }

        public Map<File, byte[]> getFiles() {
            return files;
        }
    }
}
