package net.masterthought.cucumber.json.support;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import net.masterthought.cucumber.json.deserializers.StatusDeserializer;

/**
 * Defines all possible statuses provided by cucumber-jvm library.
 * 
 * @author Damian Szczepanik (damianszczepanik@github)
 */
@JsonDeserialize(using = StatusDeserializer.class)
public enum Status {

    PASSED,
    FAILED,
    SKIPPED,
    PENDING,
    UNDEFINED;

    /**
     * Returns name of the status converted to lower case characters.
     */
    public String getRawName() {
        return name().toLowerCase();
    }

    /** Returns name of the status changing first letter to upper case character. */
    public String getLabel() {
        return name().substring(0, 1).toUpperCase() + name().substring(1).toLowerCase();
    }

    /** Returns true if status is equal to {@link #PASSED}. */
    public boolean isPassed() {
        return this == PASSED;
    }
}