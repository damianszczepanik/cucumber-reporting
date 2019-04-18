package net.masterthought.cucumber.json.support;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import net.masterthought.cucumber.json.deserializers.StatusDeserializer;

/**
 * Defines all possible statuses provided by cucumber library.
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
     * @return status name as lowercase
     */
    public String getRawName() {
        return name().toLowerCase();
    }

    /**
     * Returns name of the status formatted with first letter to uppercase and lowercase others.
     *
     * @return status formatted with first letter to uppercase
     */
    public String getLabel() {
        return name().substring(0, 1).toUpperCase() + name().substring(1).toLowerCase();
    }

    /**
     * Returns true if status is equal to {@link #PASSED}.
     *
     * @return <code>true</code> if the status is <code>PASSED</code>, otherwise <code>false</code>
     */
    public boolean isPassed() {
        return this == PASSED;
    }
}