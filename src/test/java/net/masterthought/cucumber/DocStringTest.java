package net.masterthought.cucumber;

import net.masterthought.cucumber.json.DocString;
import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.json.Step;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static net.masterthought.cucumber.FileReaderUtil.getAbsolutePathFromResource;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class DocStringTest {

    ReportParser reportParser;
    Step step;
    DocString docstring;

    @Before
    public void setUpJsonReports() throws IOException {
        List<String> jsonReports = new ArrayList<String>();
        jsonReports.add(getAbsolutePathFromResource("net/masterthought/cucumber/docstring.json"));
        reportParser = new ReportParser(jsonReports);
        Feature feature = reportParser.getFeatures().entrySet().iterator().next().getValue().get(0);
        step = feature.getElements().get(0).getSteps().get(0);
        docstring = step.getDocString();
    }

    @Test
    public void shouldReturnDocString() {
        assertThat(docstring, is(DocString.class));
    }

    @Test
    public void shouldReturnFields() {
        assertThat(docstring.getValue(), is("X _ X\nO X O\n_ O X"));
        assertThat(docstring.getLine(), is(8));
        assertThat(docstring.getContentType(), is(""));
    }

    @Test
    public void shouldFormatDocString() {
        assertThat(step.getDocStringOrNothing(), is("<div class=\"passed\">" +
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
