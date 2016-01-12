package net.masterthought.cucumber.json;

import net.masterthought.cucumber.json.support.ResultsWithMatch;

public class Hook implements ResultsWithMatch {

    // Start: attributes from JSON file report
    private final Result result = null;
    private final Match match = null;
    private final Embedded[] embeddings = new Embedded[0];
    // End: attributes from JSON file report

    private String attachments;

    @Override
    public Result getResult() {
        return result;
    }

    @Override
    public Match getMatch() {
        return match;
    }

    @Override
    public String getAttachments() {
        return attachments;
    }
    
    public void setMedaData() {
        calculateEmbeddings();
    }

    private void calculateEmbeddings() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < embeddings.length; i++) {
            sb.append(embeddings[i].render(i));
        }
        attachments = sb.toString();
    }

}
