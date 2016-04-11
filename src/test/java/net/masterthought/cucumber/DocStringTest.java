package net.masterthought.cucumber;

import static net.masterthought.cucumber.FileReaderUtil.getAbsolutePathFromResource;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.json.Step;


public class DocStringTest {

    private final Configuration configuration = new Configuration(new File(""), "testProject");

    private Step step;

    @Before
    public void setUpJsonReports() throws IOException {
        List<String> jsonReports = new ArrayList<String>();
        jsonReports.add(getAbsolutePathFromResource("net/masterthought/cucumber/docstring.json"));
        List<Feature> features = new ReportParser(configuration).parseJsonResults(jsonReports);
        Feature feature = features.get(0);
        step = feature.getElements()[0].getSteps()[0];
    }

    @Test
    public void shouldFormatDocString() {
        assertThat(step.getDocString().getValue(),
                is("X _ X\n"
                 + "O X O\n"
                 + "_ O X"));
    }
}
