package net.masterthought.cucumber.generators.integrations.helpers;

import org.apache.commons.lang.StringUtils;
import org.assertj.core.api.filter.NotInFilter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class HeadAssertion extends ReportAssertion {

    public String getTitle() {
        return oneBySelector("title", WebAssertion.class).text();
    }

    /**
     * Validates the included JS scripts in the head
     *
     * @param files js file names
     */
    public void hasAtLeastTheseJsFilesIncluded(String... files) {
        List<HeadAssertion> scripts = Arrays.asList(allBySelector("script", HeadAssertion.class));
        assertThat(scripts).hasSizeGreaterThanOrEqualTo(files.length);

        for (final String file : files) {
            scripts.stream()
                    .filter(s -> file.equals(s.attr("src")))
                    .findAny()
                    .orElseThrow(() -> new AssertionError(String.format("%s not found", file)));
        }
    }

    /**
     * Validates the included CSS scripts in the head
     *
     * @param files js file names
     */
    public void hasAtLeastTheseCssFilesIncluded(String... files) {
        List<HeadAssertion> stylesheets = Arrays.stream(allBySelector("link", HeadAssertion.class))
                .filter(l ->  "stylesheet".equalsIgnoreCase(l.attr("rel")))
                .collect(Collectors.toList());
        assertThat(stylesheets).hasSizeGreaterThanOrEqualTo(files.length);

        for (final String file : files) {
            stylesheets.stream()
                    .filter(s -> file.equals(s.attr("href")))
                    .findAny()
                    .orElseThrow(() -> new AssertionError(String.format("%s not found", file)));
        }
    }
}
