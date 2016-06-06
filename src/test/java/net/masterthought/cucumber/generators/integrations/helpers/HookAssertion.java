package net.masterthought.cucumber.generators.integrations.helpers;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class HookAssertion extends ReportAssertion {

    @Override
    public BriefAssertion getBrief() {
        return super.getBrief();
    }

    public String getErrorMessage() {
        return oneByClass("message", WebAssertion.class).text();
    }

    public EmbeddingAssertion[] getEmbedding() {
        return oneByClass("embeddings", WebAssertion.class).allByClass("embedding", EmbeddingAssertion.class);
    }
}
