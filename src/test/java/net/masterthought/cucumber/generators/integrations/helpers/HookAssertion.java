package net.masterthought.cucumber.generators.integrations.helpers;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class HookAssertion extends ReportAssertion {

    @Override
    public BriefAssertion getBrief() {
        return super.getBrief();
    }

    @Override
    public String getErrorMessage() {
        return oneByClass("message", WebAssertion.class).oneBySelector("pre", WebAssertion.class).text();
    }

    public OutputAssertion[] getOutputs() {
        return allByClass("output", OutputAssertion.class);
    }

    public EmbeddingAssertion[] getEmbedding() {
        return oneByClass("embeddings", WebAssertion.class).allByClass("embedding", EmbeddingAssertion.class);
    }
}
