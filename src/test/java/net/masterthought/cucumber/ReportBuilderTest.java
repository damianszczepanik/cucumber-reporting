package net.masterthought.cucumber;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.fail;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.StreamReadConstraints;
import net.masterthought.cucumber.generators.OverviewReport;
import net.masterthought.cucumber.json.Feature;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.powermock.reflect.Whitebox;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
class ReportBuilderTest extends ReportGenerator {

    private File reportDirectory;
    private File trendsFileTmp;

    @BeforeEach
    void setUp() throws IOException {
        reportDirectory = new File("target", "generated-reports" + File.separatorChar + System.currentTimeMillis());
        // random temp directory
        reportDirectory.mkdirs();
        // root report directory
        new File(reportDirectory, ReportBuilder.BASE_DIRECTORY + configuration.getDirectorySuffixWithSeparator()).mkdir();

        // refresh the file if it was already copied by another/previous test
        trendsFileTmp = new File(reportDirectory, "trends-tmp.json");

        FileUtils.copyFile(TRENDS_FILE, trendsFileTmp);
    }

    @Test
    void ReportBuilder_storesFilesAndConfiguration() {

        // given
        final List<String> jsonFiles = new ArrayList<>();
        final Configuration configuration = new Configuration(null, null);

        // when
        ReportBuilder builder = new ReportBuilder(jsonFiles, configuration);

        // then
        List<String> assignedJsonReports = Whitebox.getInternalState(builder, "jsonFiles");
        Configuration assignedConfiguration = Whitebox.getInternalState(builder, "configuration");

        assertThat(assignedJsonReports).isSameAs(jsonFiles);
        assertThat(assignedConfiguration).isSameAs(configuration);
    }

    @Test
    void ReportBuilder_setsAndGetsCustomReportParser() {
        // given
        final List<String> jsonFiles = new ArrayList<>();
        final Configuration configuration = new Configuration(null, null);
        final ReportParser reportParser = new ReportParser(configuration);

        // when
        ReportBuilder builder = new ReportBuilder(jsonFiles, configuration);
        builder.setReportParser(reportParser);

        // then
        ReportParser assignedReportParser = Whitebox.getInternalState(builder, "reportParser");

        assertThat(assignedReportParser).isSameAs(builder.getReportParser());
    }

    @Test
    void generateReports_GeneratesPages() {

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
    void generateReports_WithTrendsFile_GeneratesPages() {

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
    void generateReports_OnException_AppendsBuildToTrends() throws Exception {

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
        assertPageExists(reportDirectory, configuration.getDirectorySuffixWithSeparator(), ReportBuilder.HOME_PAGE);
        assertThat(countHtmlFiles()).hasSize(1);

        Trends trends = Whitebox.invokeMethod(reportBuilder, "loadTrends", trendsFileTmp);
        assertThat(trends.getBuildNumbers()).hasSize(4);
    }

    @Test
    void generateReports_OnException_StoresEmptyTrendsFile() {

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
        assertPageExists(reportDirectory, configuration.getDirectorySuffixWithSeparator(), ReportBuilder.HOME_PAGE);
        assertThat(countHtmlFiles()).hasSize(1);
        assertThat(result).isNull();
    }

    @Test
    void generateReports_resetsDefaultStreamReadConstraints() {

        // given
        StreamReadConstraints defaultStreamReadConstraints = StreamReadConstraints.defaults();
        List<String> jsonReports = Arrays.asList(ReportGenerator.reportFromResource(ReportGenerator.SAMPLE_JSON));

        Configuration configuration = new Configuration(reportDirectory, "constraintsProject");
        configuration.setMaxStreamStringLength(defaultStreamReadConstraints.getMaxStringLength() + 1);
        ReportBuilder builder = new ReportBuilder(jsonReports, configuration);

        // when
        builder.generateReports();

        // then
        assertThat(StreamReadConstraints.defaults()).isEqualTo(defaultStreamReadConstraints);
    }

    @Test
    void copyStaticResources_CopiesRequiredFiles() throws Exception {

        // given
        Configuration configuration = new Configuration(reportDirectory, "myProject");
        ReportBuilder builder = new ReportBuilder(Collections.<String>emptyList(), configuration);

        // when
        Whitebox.invokeMethod(builder, "copyStaticResources");

        // then
        Collection<File> files = FileUtils.listFiles(reportDirectory, null, true);
        assertThat(files).hasSize(21);
    }

    @Test
    void createEmbeddingsDirectory_CreatesDirectory() throws Exception {

        // given
        File subDirectory = new File(reportDirectory, "sub");

        Configuration configuration = new Configuration(subDirectory, "myProject");
        ReportBuilder builder = new ReportBuilder(Collections.<String>emptyList(), configuration);

        // when
        Whitebox.invokeMethod(builder, "createEmbeddingsDirectory");

        // then
        assertThat(subDirectory).exists();
    }

    @Test
    void copyResources_OnInvalidPath_ThrowsException() throws Exception {

        // given
        Configuration configuration = new Configuration(reportDirectory, "myProject");
        ReportBuilder builder = new ReportBuilder(Collections.<String>emptyList(), configuration);
        File dir = new File(reportDirectory, ReportBuilder.BASE_DIRECTORY + configuration.getDirectorySuffixWithSeparator());

        // then
        try {
            Whitebox.invokeMethod(builder, "copyResources", dir.getAbsolutePath(), new String[]{"someFile"});
            fail("Copying should fail!");
            // exception depends on operating system or JVM version
        } catch (ValidationException | InvalidPathException | NullPointerException e) {
            // passed
        }
    }

    @Test
    void copyCustomResources_OnInvalidPath_DoesNotThrowsException() throws Exception {

        // given
        Configuration configuration = new Configuration(reportDirectory, "myProject");
        ReportBuilder builder = new ReportBuilder(Collections.emptyList(), configuration);
        File srcFile = new File("non-existent.file");
        File dstFile = Whitebox.invokeMethod(builder, "createTempFile", "js", srcFile.getName());

        // when
        Whitebox.invokeMethod(builder, "copyCustomResources", "js", srcFile);

        // then
        assertThat(dstFile).doesNotExist();
    }

    @Test
    void copyCustomResources_OnDirAsFile_ThrowsIOException() throws Exception {

        // given
        Configuration configuration = new Configuration(reportDirectory, "myProject");
        ReportBuilder builder = new ReportBuilder(Collections.<String>emptyList(), configuration);
        File dir = new File("src/test/resources/js");

        // when & then
        assertThatThrownBy(() -> Whitebox.invokeMethod(builder, "copyCustomResources", "js", dir))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void copyCustomResources_CheckCopiedFiles() throws Exception {

        // given
        Configuration configuration = new Configuration(reportDirectory, "myProject");
        configuration.addCustomJsFiles(Collections.singletonList("src/test/resources/js/test.js"));
        configuration.addCustomCssFiles(Collections.singletonList("src/test/resources/css/test.css"));
        ReportBuilder builder = new ReportBuilder(Collections.<String>emptyList(), configuration);

        // when
        Whitebox.invokeMethod(builder, "copyCustomJsAndCssResources");

        // then
        assertPageExists(reportDirectory, configuration.getDirectorySuffixWithSeparator(), "/js/test.js");
        assertPageExists(reportDirectory, configuration.getDirectorySuffixWithSeparator(), "/css/test.css");
    }

    @Test
    void collectPages_CollectsPages() throws Exception {

        // given
        setUpWithJson(SAMPLE_JSON);

        ReportBuilder builder = new ReportBuilder(jsonReports, configuration);
        Whitebox.setInternalState(builder, "reportResult", new ReportResult(features, configuration));

        // when
        Whitebox.invokeMethod(builder, "generatePages", new Trends());

        // then
        assertThat(countHtmlFiles(configuration)).hasSize(9);
    }

    @Test
    void collectPages_OnExistingTrendsFile_CollectsPages() throws Exception {

        // given
        setUpWithJson(SAMPLE_JSON);
        configuration.setTrendsStatsFile(trendsFileTmp);

        ReportBuilder builder = new ReportBuilder(jsonReports, configuration);
        Whitebox.setInternalState(builder, "reportResult", new ReportResult(features, configuration));

        // when
        Whitebox.invokeMethod(builder, "generatePages", new Trends());

        // then
        assertThat(countHtmlFiles(configuration)).hasSize(10);
    }

    @Test
    void updateAndSaveTrends_ReturnsUpdatedTrends() throws Exception {

        // given
        setUpWithJson(SAMPLE_JSON);
        final String buildNumber = "my build";
        configuration.setBuildNumber(buildNumber);
        configuration.setTrendsStatsFile(trendsFileTmp);
        ReportBuilder builder = new ReportBuilder(jsonReports, configuration);
        Whitebox.setInternalState(builder, "reportResult", reportResult);

        // when
        Trends trends = Whitebox.invokeMethod(builder, "updateAndSaveTrends", reportResult.getFeatureReport());

        // then
        assertThat(trends.getBuildNumbers()).hasSize(4);
        assertThat(trends.getBuildNumbers()).endsWith(buildNumber);
        assertThat(trends.getFailedScenarios()).hasSize(4);
        assertThat(trends.getFailedScenarios()).endsWith(reportResult.getFeatureReport().getFailedScenarios());
    }

    @Test
    void updateAndSaveTrends_OnTrendsLimit_ReturnsUpdatedTrends() throws Exception {

        // given
        final int trendsLimit = 2;
        setUpWithJson(SAMPLE_JSON);
        final String buildNumber = "my build";
        configuration.setBuildNumber(buildNumber);
        configuration.setTrends(trendsFileTmp, trendsLimit);
        ReportBuilder builder = new ReportBuilder(jsonReports, configuration);
        Whitebox.setInternalState(builder, "reportResult", reportResult);

        // when
        Trends trends = Whitebox.invokeMethod(builder, "updateAndSaveTrends", reportResult.getFeatureReport());

        // then
        assertThat(trends.getBuildNumbers()).hasSize(trendsLimit);
        assertThat(trends.getBuildNumbers()).endsWith(buildNumber);
        assertThat(trends.getFailedScenarios()).hasSize(trendsLimit);
        assertThat(trends.getFailedScenarios()).endsWith(reportResult.getFeatureReport().getFailedScenarios());
    }

    @Test
    void loadTrends_ReturnsTrends() throws Exception {

        // when
        Trends trends = Whitebox.invokeMethod(ReportBuilder.class, "loadTrends", TRENDS_FILE);

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
    void loadTrends_OnMissingTrendsFile_ThrowsException() {

        // given
        File noExistingTrendsFile = new File("anyNoExisting?File");

        // when & then
        assertThatThrownBy(() -> Whitebox.invokeMethod(ReportBuilder.class, "loadTrends", noExistingTrendsFile))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void loadOrCreateTrends_ReturnsLoadedTrends() throws Exception {

        // given
        Configuration configuration = new Configuration(reportDirectory, "myProject");
        configuration.setTrendsStatsFile(trendsFileTmp);
        ReportBuilder builder = new ReportBuilder(jsonReports, configuration);

        // when
        Trends trends = Whitebox.invokeMethod(builder, "loadOrCreateTrends");

        // then
        assertThat(trends.getBuildNumbers()).containsExactly("01_first", "other build", "05last");
    }

    @Test
    void loadOrCreateTrends_OnMissingTrendsFile_ReturnsEmptyTrends() throws Exception {

        // given
        Configuration configuration = new Configuration(reportDirectory, "myProject");
        ReportBuilder builder = new ReportBuilder(jsonReports, configuration);
        configuration.setTrendsStatsFile(new File("missing?file"));

        // when
        Trends trends = Whitebox.invokeMethod(builder, "loadOrCreateTrends");

        // then
        assertThat(trends.getBuildNumbers()).isEmpty();
    }

    @Test
    void loadOrCreateTrends_OnInvalidTrendsFile_ReturnsEmptyTrends() throws Exception {

        // given
        Configuration configuration = new Configuration(reportDirectory, "myProject");
        ReportBuilder builder = new ReportBuilder(jsonReports, configuration);

        // when
        Trends trends = Whitebox.invokeMethod(builder, "loadOrCreateTrends");

        // then
        assertThat(trends.getBuildNumbers()).isEmpty();
    }

    @Test
    void loadTrends_OnInvalidTrendsFormatFile_ThrowsExceptions() {

        // given
        File notTrendJsonFile = new File(reportFromResource(SAMPLE_JSON));

        // when & then
        assertThatThrownBy(() -> Whitebox.invokeMethod(ReportBuilder.class, "loadTrends", notTrendJsonFile))
                .isInstanceOf(ValidationException.class)
                .hasMessageEndingWith("could not be parsed as file with trends!");
    }

    @Test
    void loadTrends_OnInvalidTrendsFile_ThrowsExceptions() {

        // given
        File directoryFile = new File(".");

        // when & then
        assertThatThrownBy(() -> Whitebox.invokeMethod(ReportBuilder.class, "loadTrends", directoryFile))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("java.io.FileNotFoundException");
    }

    @Test
    void appendToTrends_AppendsDataToTrends() throws Exception {

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

        ReportResult reportResult = new ReportResult(Collections.<Feature>emptyList(), configuration) {
            @Override
            public Reportable getFeatureReport() {
                return reportable;
            }
        };

        ReportBuilder reportBuilder = new ReportBuilder(null, configuration);
        Whitebox.setInternalState(reportBuilder, "reportResult", reportResult);
        Trends trends = new Trends();

        // when
        Whitebox.invokeMethod(reportBuilder, "appendToTrends", trends, reportable);

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
    void saveTrends_OnInvalidFile_ThrowsException() {

        // given
        ReportBuilder builder = new ReportBuilder(jsonReports, configuration);

        Trends trends = new Trends();
        File directoryFile = new File(".");

        // when & then
        assertThatThrownBy(() -> Whitebox.invokeMethod(builder, "saveTrends", trends, directoryFile))
                .isInstanceOf(ValidationException.class)
                .hasMessageStartingWith("Could not save updated trends ");
    }

    @Test
    void generateErrorPage_GeneratesErrorPage() throws Exception {

        // given
        Configuration configuration = new Configuration(reportDirectory, "myProject");
        ReportBuilder builder = new ReportBuilder(Collections.<String>emptyList(), configuration);

        // when
        Whitebox.invokeMethod(builder, "generateErrorPage", new Exception());

        // then
        assertPageExists(reportDirectory, configuration.getDirectorySuffixWithSeparator(), ReportBuilder.HOME_PAGE);
    }

    private File[] countHtmlFiles(Configuration configuration) {
        FileFilter fileFilter = WildcardFileFilter.builder().setWildcards("*.html").get();
        File dir = new File(configuration.getReportDirectory(), ReportBuilder.BASE_DIRECTORY + configuration.getDirectorySuffixWithSeparator());
        return dir.listFiles(fileFilter);
    }

    private File[] countHtmlFiles() {
        FileFilter fileFilter = WildcardFileFilter.builder().setWildcards("*.html").get();
        File dir = new File(reportDirectory, ReportBuilder.BASE_DIRECTORY + configuration.getDirectorySuffixWithSeparator());
        return dir.listFiles(fileFilter);
    }

    private static void assertPageExists(File reportDirectory, String directorySuffix, String fileName) {
        File errorPage = new File(reportDirectory, ReportBuilder.BASE_DIRECTORY + directorySuffix + File.separatorChar + fileName);
        assertThat(errorPage).exists();
    }
}
