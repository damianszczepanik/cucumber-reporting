package net.masterthought.cucumber.json;

/**
 * Doc Strings are handy for specifying a larger piece of text. This is inspired from Python’s Docstring syntax.
 *
 * In your step definition, there’s no need to find this text and match it in your Regexp. It will automatically be
 * passed as the last parameter in the step definition.
 */
public class DocString {

    // Start: attributes from JSON file report
    private final String value = null;
    // End: attributes from JSON file report

    public String getValue() {
        return value;
    }
}
