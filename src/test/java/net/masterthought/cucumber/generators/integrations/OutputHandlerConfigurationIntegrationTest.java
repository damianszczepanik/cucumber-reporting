package net.masterthought.cucumber.generators.integrations;

import net.masterthought.cucumber.ValidationException;
import net.masterthought.cucumber.generators.FeaturesOverviewPage;
import net.masterthought.cucumber.outputhandlers.OutputHandler;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class OutputHandlerConfigurationIntegrationTest extends PageTest {

    private InMemoryOutputHandler inMemoryOutputHandler = new InMemoryOutputHandler();

    @Test
    public void addOutputHandler_addedOutputHandlerIsUsedDuringReportGeneration() {
        // Given
        // we only want the inMemoryHandler to be used, so we clear the List of handlers first
        configuration.clearOutputHandlers();
        //Add the inMemoryHandler
        configuration.addOutputHandler(inMemoryOutputHandler);

        //When
        setUpWithJson(SAMPLE_JSON);
        page = new FeaturesOverviewPage(reportResult, configuration);
        page.generatePage();

        //Then
        Map<File, byte[]> files = inMemoryOutputHandler.getFiles();

        // Feature-overview-page + 14 Embeddings -> Files should contain 15 entries
        assertThat(files).hasSize(15);
        assertThat(files.keySet().stream().anyMatch(file -> file.getName().equals("overview-features.html"))).isTrue();
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
