package net.masterthought.cucumber.json.support;

import net.masterthought.cucumber.json.Embedded;
import net.masterthought.cucumber.json.Match;
import net.masterthought.cucumber.json.Result;

/**
 * Ensures that class delivers method for counting results and matches.
 * 
 * @author Damian Szczepanik (damianszczepanik@github)
 *
 */
public interface ResultsWithMatch {

    public Result getResult();

    public Match getMatch();

    public Embedded[] getEmbeddings();

    public String getAttachments();
}
