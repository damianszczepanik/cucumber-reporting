package net.masterthought.cucumber;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.core.StringEndsWith.endsWith;
import static org.hamcrest.core.StringStartsWith.startsWith;

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
import org.apache.commons.lang3.mutable.MutableInt;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import net.masterthought.cucumber.generators.AbstractPage;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class ReportBuilderTest extends ReportGenerator {

    private File reportDirectory;
    private final File TRENDS_TMP_FILE = new File(TRENDS_FILE.getAbsolutePath() + "-tmp");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws IOException {
        reportDirectory = new File("target" + File.separatorChar + System.currentTimeMillis());
        // random temp directory
        reportDirectory.mkdir();
        // root report directory
        new File(reportDirectory, ReportBuilder.BASE_DIRECTORY).mkdir();

        // refresh the file if it was already copied by another/previous test
        FileUtils.copyFile(TRENDS_FILE, TRENDS_TMP_FILE);
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
    public void collectPages_CollectsPages() {

        // given
        setUpWithJson(SAMPLE_JSON);

        ReportBuilder builder = new ReportBuilder(jsonReports, configuration);
        Deencapsulation.setField(builder, "reportResult", new ReportResult(features));

        // when
        List<AbstractPage> pages = Deencapsulation.invoke(builder, "collectPages");

        // then
        assertThat(pages).hasSize(9);
    }

    @Test
    public void collectPages_OnTrendsFile_CollectsPages() {

        // given
        setUpWithJson(SAMPLE_JSON);
        configuration.setTrendsStatsFile(TRENDS_TMP_FILE);

        ReportBuilder builder = new ReportBuilder(jsonReports, configuration);
        Deencapsulation.setField(builder, "reportResult", new ReportResult(features));

        // when
        List<AbstractPage> pages = Deencapsulation.invoke(builder, "collectPages");

        // then
        assertThat(pages).hasSize(10);
    }

    @Test
    public void generatePages_CallsGeneratePagesOverPassedPages() {

        // given00
        Configuration configuration = new Configuration(null, null);
        ReportBuilder builder = new ReportBuilder(jsonReports, configuration);

        final MutableInt counter = new MutableInt();
        AbstractPage page = new AbstractPage(null, null, configuration) {
            @Override
            public String getWebPage() {
                return null;
            }

            @Override
            protected void prepareReport() {

            }

            @Override
            public void generatePage() {
                counter.increment();
            }
        };
        List<AbstractPage> pages = Arrays.asList(page, page, page);

        // when
        Deencapsulation.invoke(builder, "generatePages", pages);

        // then
        assertThat(counter.getValue()).isEqualTo(pages.size());
    }

    @Test
    public void updateAndGenerateTrends_StoresUpdatedFile() {

        // given
        setUpWithJson(SAMPLE_JSON);
        final String buildNumber = "my build";
        configuration.setBuildNumber(buildNumber);
        ReportBuilder builder = new ReportBuilder(jsonReports, configuration);
        Deencapsulation.setField(builder, "reportResult", reportResult);

        // when
        Trends trends = Deencapsulation.invoke(builder, "updateAndGenerateTrends", TRENDS_TMP_FILE);

        // then
        assertThat(trends.getBuildNumbers()).hasSize(4);
        assertThat(trends.getBuildNumbers()).endsWith(buildNumber);
        assertThat(trends.getFailedScenarios()).hasSize(4);
        assertThat(trends.getFailedScenarios()).endsWith(reportResult.getFeatureReport().getFailedScenarios());
    }

    @Test
    public void loadTrends_ReturnsTrends() {

        // when
        Trends trends = Deencapsulation.invoke(ReportBuilder.class, "loadTrends", TRENDS_FILE);

        // then
        assertThat(trends.getBuildNumbers()).containsExactly("01_first", "other build", "05last");
        assertThat(trends.getFailedFeatures()).containsExactly(1, 2, 5);
        assertThat(trends.getTotalFeatures()).containsExactly(10, 20, 30);
        assertThat(trends.getFailedScenarios()).containsExactly(10, 20, 20);
        assertThat(trends.getTotalScenarios()).containsExactly(10, 2, 5);
        assertThat(trends.getFailedSteps()).containsExactly(100, 20, 30);
        assertThat(trends.getTotalSteps()).containsExactly(150, 200, 300);
    }

    @Test
    public void loadTrends_OnMissingTrendsFile_ReturnsEmptyTrends() {

        // given
        File noExistingTrendsFile = new File("anyNoExisting?File");

        // when
        Trends trend = Deencapsulation.invoke(ReportBuilder.class, "loadTrends", noExistingTrendsFile);

        // then
        assertThat(trend).isNotNull();
        assertThat(trend.getBuildNumbers()).isEmpty();
        assertThat(trend.getFailedFeatures()).isEmpty();
        assertThat(trend.getTotalFeatures()).isEmpty();
        assertThat(trend.getFailedScenarios()).isEmpty();
        assertThat(trend.getTotalScenarios()).isEmpty();
        assertThat(trend.getFailedSteps()).isEmpty();
        assertThat(trend.getTotalSteps()).isEmpty();
    }

    @Test
    public void loadTrends_OnInvalidTrendsFormatFile_ThrowsExceptions() {

        // given
        File notTrendJsonFile = new File(reportFromResource(SAMPLE_JSON));

        // then
        thrown.expect(ValidationException.class);
        thrown.expectMessage(endsWith("could not be parsed as file with trends!"));
        Deencapsulation.invoke(ReportBuilder.class, "loadTrends", notTrendJsonFile);
    }

    @Test
    public void loadTrends_OnInvalidTrendsFile_ThrowsExceptions() {

        // given
        File directoryFile = new File(".");

        // then
        thrown.expect(ValidationException.class);
        thrown.expectMessage(containsString("java.io.FileNotFoundException"));
        Deencapsulation.invoke(ReportBuilder.class, "loadTrends", directoryFile);
    }

    @Test
    public void saveTrends_OnInvalidFile_ThrowsException() {

        // given
        ReportBuilder builder = new ReportBuilder(jsonReports, configuration);

        Trends trends = new Trends();
        File directoryFile = new File(".");

        // when
        thrown.expect(ValidationException.class);
        thrown.expectMessage(startsWith("Could not save updated trends "));
        Deencapsulation.invoke(builder, "saveTrends", trends, directoryFile);
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
        Reportable result = builder.generateReports();

        // then
        assertPageExists(reportDirectory, ReportBuilder.HOME_PAGE);
        assertThat(countHtmlFiles()).hasSize(1);
        assertThat(result).isNull();
    }

    @Test
    public void generateReports_GeneratesErrorPage() {

        // given
        List<String> jsonReports = Arrays.asList(ReportGenerator.reportFromResource(ReportGenerator.SAMPLE_JSON));

        Configuration configuration = new Configuration(reportDirectory, "myProject");
        ReportBuilder builder = new ReportBuilder(jsonReports, configuration);

        // when
        Reportable result = builder.generateReports();

        // then
        assertThat(countHtmlFiles()).hasSize(9);
        assertThat(result).isNotNull();
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
