package net.masterthought.cucumber;

import static net.masterthought.cucumber.FileReaderUtil.getAbsolutePathFromResource;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import net.masterthought.cucumber.json.DocString;
import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.json.Step;


public class DocStringTest {

    private final Configuration configuration = new Configuration();

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
        assertThat(step.getDocString(), is("<div class=\"passed\">" +
                                                      "<div class=\"doc-string\">" +
                                                        "X&nbsp;_&nbsp;X<br/>" +
                                                        "O&nbsp;X&nbsp;O<br/>" +
                                                        "_&nbsp;O&nbsp;X" +
                                                      "</div>" +
                                                    "</div>"));
    }

    @Test
    public void shouldEscapeForHtml() throws NoSuchFieldException, IllegalAccessException {
        DocString ds = new DocString();
        Field field = DocString.class.getDeclaredField("value");
        field.setAccessible(true);
        field.set(ds, "<a><b>cat</b></a>");
        assertThat("&lt;a&gt;&lt;b&gt;cat&lt;/b&gt;&lt;/a&gt;", is(ds.getEscapedValue()));
    }

}
