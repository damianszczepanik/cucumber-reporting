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
import net.masterthought.cucumber.generators.OverviewReport;
import net.masterthought.cucumber.json.Feature;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class ReportBuilderTest extends ReportGenerator {

    private File reportDirectory;
    private File trendsFileTmp;

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
        trendsFileTmp = new File(reportDirectory, "trends-tmp.json");

        FileUtils.copyFile(TRENDS_FILE, trendsFileTmp);
    }


    @After
    public void cleanUp() throws IOException {
        FileUtils.deleteDirectory(reportDirectory);
    }

    @Test
    public void ReportBuilder_storesFilesAndConfiguration() {

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
    public void generateReports_GeneratesPages() {

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

    @Test
    public void generateReports_WithTrendsFile_GeneratesPages() {

        // given
        List<String> jsonReports = Arrays.asList(ReportGenerator.reportFromResource(ReportGenerator.SAMPLE_JSON));

        Configuration configuration = new Configuration(reportDirectory, "myProject");
        ReportBuilder builder = new ReportBuilder(jsonReports, configuration);
        configuration.setTrendsStatsFile(trendsFileTmp);

        // when
        Reportable result = builder.generateReports();

        // then
        assertThat(countHtmlFiles()).hasSize(10);
        assertThat(result).isNotNull();
    }

    @Test
    public void generateReports_OnException_AppendsBuildToTrends() {

        // given
        List<String> jsonReports = Arrays.asList(ReportGenerator.reportFromResource(ReportGenerator.SAMPLE_JSON));

        Configuration configuration = new Configuration(reportDirectory, "myProject") {
            @Override
            public File getEmbeddingDirectory() {
                throw new IllegalStateException();
            }
        };
        ReportBuilder reportBuilder = new ReportBuilder(jsonReports, configuration);
        configuration.setTrendsStatsFile(trendsFileTmp);

        // when
        reportBuilder.generateReports();

        // then
        assertPageExists(reportDirectory, ReportBuilder.HOME_PAGE);
        assertThat(countHtmlFiles()).hasSize(1);

        Trends trends = Deencapsulation.invoke(reportBuilder, "loadTrends", trendsFileTmp);
        assertThat(trends.getBuildNumbers()).hasSize(4);
    }

    @Test
    public void generateReports_OnException_StoresEmptyTrendsFile() {

        // given
        List<String> jsonReports = Arrays.asList(ReportGenerator.reportFromResource(ReportGenerator.SAMPLE_JSON));

        Configuration configuration = new Configuration(reportDirectory, "myProject") {
            @Override
            public File getEmbeddingDirectory() {
                throw new IllegalStateException();
            }
        };
        ReportBuilder builder = new ReportBuilder(jsonReports, configuration);
        configuration.setTrendsStatsFile(trendsFileTmp);

        // when
        Reportable result = builder.generateReports();

        // then
        assertPageExists(reportDirectory, ReportBuilder.HOME_PAGE);
        assertThat(countHtmlFiles()).hasSize(1);
        assertThat(result).isNull();
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
        assertThat(files).hasSize(21);
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
    public void collectPages_CollectsPages() {

        // given
        setUpWithJson(SAMPLE_JSON);

        ReportBuilder builder = new ReportBuilder(jsonReports, configuration);
        Deencapsulation.setField(builder, "reportResult", new ReportResult(features, configuration.getSoringMethod()));

        // when
        List<AbstractPage> pages = Deencapsulation.invoke(builder, "collectPages", new Trends());

        // then
        assertThat(pages).hasSize(9);
    }

    @Test
    public void collectPages_OnExistingTrendsFile_CollectsPages() {

        // given
        setUpWithJson(SAMPLE_JSON);
        configuration.setTrendsStatsFile(trendsFileTmp);

        ReportBuilder builder = new ReportBuilder(jsonReports, configuration);
        Deencapsulation.setField(builder, "reportResult", new ReportResult(features, configuration.getSoringMethod()));

        // when
        List<AbstractPage> pages = Deencapsulation.invoke(builder, "collectPages", new Trends());

        // then
        assertThat(pages).hasSize(10);
    }

    @Test
    public void generatePages_CallsGeneratePagesOverPassedPages() {

        // given
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
                // only to satisfy abstract class contract
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
    public void updateAndSaveTrends_ReturnsUpdatedTrends() {

        // given
        setUpWithJson(SAMPLE_JSON);
        final String buildNumber = "my build";
        configuration.setBuildNumber(buildNumber);
        configuration.setTrendsStatsFile(trendsFileTmp);
        ReportBuilder builder = new ReportBuilder(jsonReports, configuration);
        Deencapsulation.setField(builder, "reportResult", reportResult);

        // when
        Trends trends = Deencapsulation.invoke(builder, "updateAndSaveTrends", reportResult.getFeatureReport());

        // then
        assertThat(trends.getBuildNumbers()).hasSize(4);
        assertThat(trends.getBuildNumbers()).endsWith(buildNumber);
        assertThat(trends.getFailedScenarios()).hasSize(4);
        assertThat(trends.getFailedScenarios()).endsWith(reportResult.getFeatureReport().getFailedScenarios());
    }

    @Test
    public void updateAndSaveTrends_OnTrendsLimit_ReturnsUpdatedTrends() {

        // given
        final int trendsLimit = 2;
        setUpWithJson(SAMPLE_JSON);
        final String buildNumber = "my build";
        configuration.setBuildNumber(buildNumber);
        configuration.setTrends(trendsFileTmp, trendsLimit);
        ReportBuilder builder = new ReportBuilder(jsonReports, configuration);
        Deencapsulation.setField(builder, "reportResult", reportResult);

        // when
        Trends trends = Deencapsulation.invoke(builder, "updateAndSaveTrends", reportResult.getFeatureReport());

        // then
        assertThat(trends.getBuildNumbers()).hasSize(trendsLimit);
        assertThat(trends.getBuildNumbers()).endsWith(buildNumber);
        assertThat(trends.getFailedScenarios()).hasSize(trendsLimit);
        assertThat(trends.getFailedScenarios()).endsWith(reportResult.getFeatureReport().getFailedScenarios());
    }

    @Test
    public void loadTrends_ReturnsTrends() {

        // when
        Trends trends = Deencapsulation.invoke(ReportBuilder.class, "loadTrends", TRENDS_FILE);

        // then
        assertThat(trends.getBuildNumbers()).containsExactly("01_first", "other build", "05last");

        assertThat(trends.getPassedFeatures()).containsExactly(9, 18, 25);
        assertThat(trends.getFailedFeatures()).containsExactly(1, 2, 5);
        assertThat(trends.getTotalFeatures()).containsExactly(10, 20, 30);

        assertThat(trends.getPassedScenarios()).containsExactly(10, 20, 20);
        assertThat(trends.getFailedScenarios()).containsExactly(10, 20, 20);
        assertThat(trends.getTotalScenarios()).containsExactly(10, 2, 5);

        assertThat(trends.getPassedFeatures()).containsExactly(9, 18, 25);
        assertThat(trends.getFailedSteps()).containsExactly(10, 30, 50);
        assertThat(trends.getSkippedSteps()).containsExactly(100, 300, 500);
        assertThat(trends.getPendingSteps()).containsExactly(1000, 3000, 5000);
        assertThat(trends.getUndefinedSteps()).containsExactly(10000, 30000, 50000);
        assertThat(trends.getTotalSteps()).containsExactly(100000, 300000, 500000);
    }

    @Test
    public void loadTrends_OnMissingTrendsFile_ThrowsException() {

        // given
        File noExistingTrendsFile = new File("anyNoExisting?File");

        // when
        thrown.expect(ValidationException.class);
        Deencapsulation.invoke(ReportBuilder.class, "loadTrends", noExistingTrendsFile);
    }

    @Test
    public void loadOrCreateTrends_ReturnsLoadedTrends() {

        // given
        Configuration configuration = new Configuration(reportDirectory, "myProject");
        configuration.setTrendsStatsFile(trendsFileTmp);
        ReportBuilder builder = new ReportBuilder(jsonReports, configuration);

        // when
        Trends trends = Deencapsulation.invoke(builder, "loadOrCreateTrends");

        // then
        assertThat(trends.getBuildNumbers()).containsExactly("01_first", "other build", "05last");
    }

    @Test
    public void loadOrCreateTrends_OnMissingTrendsFile_ReturnsEmptyTrends() {

        // given
        Configuration configuration = new Configuration(reportDirectory, "myProject");
        ReportBuilder builder = new ReportBuilder(jsonReports, configuration);
        configuration.setTrendsStatsFile(new File("missing?file"));

        // when
        Trends trends = Deencapsulation.invoke(builder, "loadOrCreateTrends");

        // then
        assertThat(trends.getBuildNumbers()).hasSize(0);
    }

    @Test
    public void loadOrCreateTrends_OnInvalidTrendsFile_ReturnsEmptyTrends() {

        // given
        Configuration configuration = new Configuration(reportDirectory, "myProject");
        ReportBuilder builder = new ReportBuilder(jsonReports, configuration);

        // when
        Trends trends = Deencapsulation.invoke(builder, "loadOrCreateTrends");

        // then
        assertThat(trends.getBuildNumbers()).hasSize(0);
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
    public void appendToTrends_AppendsDataToTrends() {

        // given
        final String buildNumber = "1";
        final int failedFeature = 1;
        final int totalFeature = 2;
        final int failedScenario = 3;
        final int totalScenario = 4;
        final int failedStep = 5;
        final int totalStep = 6;
        configuration = new Configuration(null, null);
        configuration.setBuildNumber(buildNumber);

        final Reportable reportable = new OverviewReport() {
            @Override
            public int getFailedFeatures() {
                return failedFeature;
            }

            @Override
            public int getFeatures() {
                return totalFeature;
            }

            @Override
            public int getFailedScenarios() {
                return failedScenario;
            }

            @Override
            public int getScenarios() {
                return totalScenario;
            }

            @Override
            public int getFailedSteps() {
                return failedStep;
            }

            @Override
            public int getSteps() {
                return totalStep;
            }
        };

        ReportResult reportResult = new ReportResult(Collections.<Feature>emptyList(), configuration.getSoringMethod()) {
            @Override
            public Reportable getFeatureReport() {
                return reportable;
            }
        };

        ReportBuilder reportBuilder = new ReportBuilder(null, configuration);
        Deencapsulation.setField(reportBuilder, "reportResult", reportResult);
        Trends trends = new Trends();

        // when
        Deencapsulation.invoke(reportBuilder, "appendToTrends", trends, reportable);

        // then
        assertThat(trends.getBuildNumbers()).containsExactly(buildNumber);
        assertThat(trends.getFailedFeatures()).containsExactly(failedFeature);
        assertThat(trends.getTotalFeatures()).containsExactly(totalFeature);
        assertThat(trends.getFailedScenarios()).containsExactly(failedScenario);
        assertThat(trends.getTotalScenarios()).containsExactly(totalScenario);
        assertThat(trends.getFailedSteps()).containsExactly(failedStep);
        assertThat(trends.getTotalSteps()).containsExactly(totalStep);
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
    public void generateErrorPage_GeneratesErrorPage() {

        // given
        Configuration configuration = new Configuration(reportDirectory, "myProject");
        ReportBuilder builder = new ReportBuilder(Collections.<String>emptyList(), configuration);

        // when
        Deencapsulation.invoke(builder, "generateErrorPage", new Exception());

        // then
        assertPageExists(reportDirectory, ReportBuilder.HOME_PAGE);
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
