package net.masterthought.cucumber.generators.integrations.helpers;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class StepAssertion extends ReportAssertion {

    @Override
    public BriefAssertion getBrief() {
        return super.getBrief();
    }

    public TableAssertion getArgumentsTable() {
        return oneByClass("step-arguments", TableAssertion.class);
    }

    public EmbeddingAssertion[] getEmbedding() {
        return oneByClass("embeddings", WebAssertion.class).allByClass("embedding", EmbeddingAssertion.class);
    }

    public OutputAssertion getOutput() {
        return oneByClass("outputs", OutputAssertion.class);
    }

    public DocStringAssertion getDocString() {
        return oneByClass("docstring", DocStringAssertion.class);
    }

    public WebAssertion getMessage() {
        return oneByClass("message", WebAssertion.class);
    }

    public HooksAssertion getBefore() {
        return oneByClass("hooks-step-before", HooksAssertion.class);
    }

    public HooksAssertion getAfter() {
        return oneByClass("hooks-step-after", HooksAssertion.class);
    }

    public void hasComments(List<String> expectedComments) {
        Elements elements = element.getElementsByClass("comment");
        List<String> comments = elements.stream()
                .map(Element::text)
                .collect(toList());
        assertThat(comments).isEqualTo(expectedComments);
    }
}
