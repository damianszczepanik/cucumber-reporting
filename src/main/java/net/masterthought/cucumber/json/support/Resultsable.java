package net.masterthought.cucumber.json.support;

import net.masterthought.cucumber.json.Match;
import net.masterthought.cucumber.json.Output;
import net.masterthought.cucumber.json.Result;

/**
 * Ensures that class delivers method for counting results and matches.
 * 
 * @author Damian Szczepanik (damianszczepanik@github)
 *
 */
public interface Resultsable {

    Result getResult();

    Match getMatch();

    Output[] getOutputs();
}
