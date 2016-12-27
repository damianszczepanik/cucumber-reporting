package net.masterthought.cucumber.generators.integrations.helpers;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class StepAssertion extends ReportAssertion {

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
}
