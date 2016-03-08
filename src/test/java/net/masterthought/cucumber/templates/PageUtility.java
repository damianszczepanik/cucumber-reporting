package net.masterthought.cucumber.templates;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.Charsets;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;

import net.masterthought.cucumber.ReportGenerator;
import net.masterthought.cucumber.ValidationException;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class PageUtility extends ReportGenerator {

    protected ElementWrapper documentFrom(String pageName) {
        File input = new File(configuration.getReportDirectory(), pageName);
        try {
            return new ElementWrapper(Jsoup.parse(input, Charsets.UTF_8.name(), StringUtils.EMPTY));
        } catch (IOException e) {
            throw new ValidationException(e);
        }
    }
}
