package net.masterthought.cucumber.json.support;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import net.masterthought.cucumber.json.deserializers.StatusDeserializer;

/**
 * Defines all possible statuses provided by cucumber library. The ordering
 * of these is important, as it determines priority. A scenario is only
 * considered passing if all of its steps are passing.
 * 
 * @author Damian Szczepanik (damianszczepanik@github)
 */
@JsonDeserialize(using = StatusDeserializer.class)
public enum Status {

    SKIPPED(0),
    PASSED(1),
    PENDING(2),
    UNDEFINED(3),
    FAILED(4);

    public final int priority;

    Status(int priority) {
        this.priority = priority;
    }

    /**
     * Returns name of the status converted to lower case characters.
     * @return status name as lowercase
     */
    public String getRawName() {
        return name().toLowerCase();
    }

    /**
     * Returns name of the status formatted with first letter to uppercase.
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

    /** Returns true if status is equal to {@link #FAILED}. */
    public boolean isFailed() {
        return this == FAILED;
    }
    /** Returns true if status is equal to {@link #PENDING}. */
    public boolean isPending() {
        return this == PENDING;
    }
    /** Returns true if status is equal to {@link #UNDEFINED}. */
    public boolean isUndefined() {
        return this == UNDEFINED;
    }

}