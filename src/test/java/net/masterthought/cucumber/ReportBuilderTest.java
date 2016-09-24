package net.masterthought.cucumber;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import mockit.Deencapsulation;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import net.masterthought.cucumber.generators.FailuresOverviewPage;
import net.masterthought.cucumber.generators.FeaturesOverviewPage;
import net.masterthought.cucumber.generators.StepsOverviewPage;
import net.masterthought.cucumber.generators.TagsOverviewPage;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class ReportBuilderTest {

    final File reportDirectory = new File("target" + File.separatorChar + System.currentTimeMillis());

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        // random temp directory
        reportDirectory.mkdir();
        // root report directory
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
    public void copyResources_OnInvalidPath_ThrowsException() {

        // given
        Configuration configuration = new Configuration(reportDirectory, "myProject");
        ReportBuilder builder = new ReportBuilder(Collections.<String>emptyList(), configuration);
        File dir = new File(reportDirectory, ReportBuilder.BASE_DIRECTORY);

        // then
        try {
            Deencapsulation.invoke(builder, "copyResources", dir.getAbsolutePath(), new String[]{"someFile"});
            fail("Copying should fail!");
            // exception depends of operating system
        } catch (ValidationException | NullPointerException e) {
            // passed
        }
    }

    @Test
    public void generateErrorPage_GeneratesErrorPage() {

        // given
        Configuration configuration = new Configuration(reportDirectory, "myProject");
        ReportBuilder builder = new ReportBuilder(Collections.<String>emptyList(), configuration);

        // when
        Deencapsulation.invoke(builder, "generateErrorPage", new Exception());

        // then
        assertPageExists(reportDirectory, ReportBuilder.HOME_PAGE);
    }

    @Test
    public void generateAllPages_GeneratesAllPages() {

        // given
        List<String> jsonReports = Arrays.asList(ReportGenerator.reportFromResource(ReportGenerator.SAMPLE_JSON));

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
    public void generateReports_OnException_GeneratesErrorPage() {

        // given
        List<String> jsonReports = Arrays.asList(ReportGenerator.reportFromResource(ReportGenerator.SAMPLE_JSON));

        final String errorMessage = "ups!";
        Configuration configuration = new Configuration(reportDirectory, "myProject") {
            @Override
            public File getEmbeddingDirectory() {
                throw new IllegalStateException(errorMessage);
            }
        };
        ReportBuilder builder = new ReportBuilder(jsonReports, configuration);

        // when
        builder.generateReports();

        // then
        assertPageExists(reportDirectory, ReportBuilder.HOME_PAGE);
        assertThat(countHtmlFiles()).hasSize(1);
    }

    @Test
    public void generateReports_GeneratesErrorPage() {

        // given
        List<String> jsonReports = Arrays.asList(ReportGenerator.reportFromResource(ReportGenerator.SAMPLE_JSON));

        Configuration configuration = new Configuration(reportDirectory, "myProject");
        ReportBuilder builder = new ReportBuilder(jsonReports, configuration);

        // when
        builder.generateReports();

        // then
        assertThat(countHtmlFiles()).hasSize(10);
    }

    private File[] countHtmlFiles() {
        FileFilter fileFilter = new WildcardFileFilter("*.html");
        File dir = new File(reportDirectory, ReportBuilder.BASE_DIRECTORY);
        return dir.listFiles(fileFilter);
    }

    private static void assertPageExists(File reportDirectory, String fileName) {
        File errorPage = new File(reportDirectory, ReportBuilder.BASE_DIRECTORY + File.separatorChar + fileName);
        assertThat(errorPage).exists();
    }
}
