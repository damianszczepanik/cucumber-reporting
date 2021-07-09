package net.masterthought.cucumber.json.support;

import net.masterthought.cucumber.json.Embedding;

/**
 * Ensures that class delivers method for embeddings.
 *
 * @author Vitaliya Ryabchuk (aqaexplorer@github)
 */
public interface Embedded {

    Embedding[] getEmbeddings();

    void setEmbeddings(Embedding[] embeddings);

    boolean hasEmbeddings();

}
