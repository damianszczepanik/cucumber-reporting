package net.masterthought.cucumber.json.support;

import net.masterthought.cucumber.json.Element;
import net.masterthought.cucumber.json.Match;
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

    /**
     * The ID of the class instance to be used as the 'id' property value of the
     * containing HTML element.
     * @return an ID string
     */
    String getId();

    /**
     * Sets the class instance ID.
     * @param id a unique ID string
     */
    void setId(String id);

    /**
     * Generates a unique ID value beginning with the parent element's 'index' class-member.
     * @param parentElement the parent (Scenario) Element used to create the ID prefix
     * @return the unique class instance ID
     */
    String generateId(Element parentElement);

    /**
     * The name used to describe the Resultable implementation instance. Should correspond
     * to the 'name' class-member or match.location.
     * @return the descriptor name
     */
    String getResultableName();
}
