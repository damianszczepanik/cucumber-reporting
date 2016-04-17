package net.masterthought.cucumber.generators;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.Charsets;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.junit.After;

import net.masterthought.cucumber.ReportGenerator;
import net.masterthought.cucumber.ValidationException;
import net.masterthought.cucumber.generators.helpers.DocumentAssertion;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public abstract class Page extends ReportGenerator {

    protected static final String SAMPLE_JOSN = "sample.json";
    protected static final String EMPTY_JOSN = "empty.json";

    protected AbstractPage page;

    @After
    public void cleanUp() {
        // delete report file if was already created by any of test
        if (page != null) {
            File report = new File(configuration.getReportDirectory(), page.getWebPage());
            report.delete();
            page = null;
        }
    }

    protected DocumentAssertion documentFrom(String pageName) {
        File input = new File(configuration.getReportDirectory(), pageName);
        try {
            return new DocumentAssertion(Jsoup.parse(input, Charsets.UTF_8.name(), StringUtils.EMPTY));
        } catch (IOException e) {
            throw new ValidationException(e);
        }
    }
}
