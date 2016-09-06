package net.masterthought.cucumber;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import mockit.Deencapsulation;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.masterthought.cucumber.generators.FailuresOverviewPage;
import net.masterthought.cucumber.generators.FeaturesOverviewPage;
import net.masterthought.cucumber.generators.StepsOverviewPage;
import net.masterthought.cucumber.generators.TagsOverviewPage;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class ReportBuilderTest {

    final File reportDirectory = new File("target" + File.separatorChar + System.currentTimeMillis());

    @Before
    public void setUp() {
        // random temp directory
        reportDirectory.mkdir();
        // main report directory
        new File(reportDirectory, ReportBuilder.BASE_DIRECTORY).mkdir();
    }

    @After
    public void cleanUp() throws IOException {
        FileUtils.deleteDirectory(reportDirectory);
    }

    @Test
    public void ReportBuilder_storesFilesAndConfiguratio() {

        // given
        final List<String> jsonFiles = new ArrayList<>();
        final Configuration configuration = new Configuration(null, null);

        // when
        ReportBuilder builder = new ReportBuilder(jsonFiles, configuration);

        // then
        List<String> assignedJsonReports = Deencapsulation.getField(builder, "jsonFiles");
        Configuration assignedConfiguration = Deencapsulation.getField(builder, "configuration");

        assertThat(assignedJsonReports).isSameAs(jsonFiles);
        assertThat(assignedConfiguration).isSameAs(configuration);
    }

    @Test
    public void createEmbeddingsDirectory_CreatesDirectory() {

        // given
        File subDirectory = new File(reportDirectory, "sub");

        Configuration configuration = new Configuration(subDirectory, "myProject");
        ReportBuilder builder = new ReportBuilder(Collections.<String>emptyList(), configuration);

        // when
        Deencapsulation.invoke(builder, "createEmbeddingsDirectory");

        // then
        assertThat(subDirectory).exists();
    }

    @Test
    public void copyStaticResources_CopiesRequiredFiles() {

        // given
        Configuration configuration = new Configuration(reportDirectory, "myProject");
        ReportBuilder builder = new ReportBuilder(Collections.<String>emptyList(), configuration);

        // when
        Deencapsulation.invoke(builder, "copyStaticResources");

        // then
        Collection<File> files = FileUtils.listFiles(reportDirectory, null, true);
        assertThat(files).hasSize(19);
    }

    @Test
    public void generateErrorPage_GeneratesErrorPage() {

        // given
        Configuration configuration = new Configuration(reportDirectory, "myProject");
        ReportBuilder builder = new ReportBuilder(Collections.<String>emptyList(), configuration);

        // when
        Deencapsulation.invoke(builder, "generateErrorPage", new Exception());

        // then
        assertErrorPageExists(reportDirectory);
    }

    @Test
    public void generateAllPages_GeneratesAllPages() throws URISyntaxException {

        // given
        List<String> jsonReports = new ArrayList<>();
        URL path = ReportGenerator.class.getClassLoader().getResource(ReportGenerator.JSON_DIRECTORY + ReportGenerator.SAMPLE_JSON);
        jsonReports.add(new File(path.toURI()).getAbsolutePath());

        Configuration configuration = new Configuration(reportDirectory, "myProject");
        configuration.getEmbeddingDirectory().mkdirs();
        ReportBuilder builder = new ReportBuilder(jsonReports, configuration);

        ReportParser reportParser = new ReportParser(configuration);
        ReportResult reportResult = new ReportResult(reportParser.parseJsonFiles(jsonReports));
        Deencapsulation.setField(builder, "reportResult", reportResult);

        // when
        Deencapsulation.invoke(builder, "generateAllPages");

        // then
        String[] pages = {
                FeaturesOverviewPage.WEB_PAGE,
                TagsOverviewPage.WEB_PAGE,
                StepsOverviewPage.WEB_PAGE,
                FailuresOverviewPage.WEB_PAGE
        };

        for (String page : pages) {
            assertThat(new File(reportDirectory, ReportBuilder.BASE_DIRECTORY + File.separatorChar + page)).exists();
        }
    }

    @Test
    public void generateReports_OnException_GeneratesErrorPage() throws URISyntaxException {

        // given
        List<String> jsonReports = new ArrayList<>();
        URL path = ReportGenerator.class.getClassLoader().getResource(ReportGenerator.JSON_DIRECTORY + ReportGenerator.SAMPLE_JSON);
        jsonReports.add(new File(path.toURI()).getAbsolutePath());

        final String errorMessage = "ups!";
        Configuration configuration = new Configuration(reportDirectory, "myProject") {
            @Override
            public File getEmbeddingDirectory() {
                throw new IllegalStateException(errorMessage);
            }
        };
        ReportBuilder builder = new ReportBuilder(jsonReports, configuration);

        ReportParser reportParser = new ReportParser(configuration);
        ReportResult reportResult = new ReportResult(reportParser.parseJsonFiles(jsonReports));
        Deencapsulation.setField(builder, "reportResult", reportResult);

        // when
        builder.generateReports();

        // then
        assertErrorPageExists(reportDirectory);
    }

    private static void assertErrorPageExists(File reportDirectory) {
        File errorPage = new File(reportDirectory,
                ReportBuilder.BASE_DIRECTORY + File.separatorChar + ReportBuilder.HOME_PAGE);
        assertThat(errorPage).exists();
    }
}
