package net.masterthought.cucumber.presentation;

/**
 * Defines how the report is presented.
 * This list contains supported modes which can be used to define how the report is presented.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public enum PresentationMode {

    /**
     * Defines additional menu buttons that enables integration with Jenkins.
     */
    RUN_WITH_JENKINS,

    /**
     * Expands all scenarios by default.
     */
    EXPAND_ALL_STEPS
}