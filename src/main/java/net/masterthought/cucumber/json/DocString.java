package net.masterthought.cucumber.json;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * Doc Strings are handy for specifying a larger piece of text. This is inspired from Python’s Docstring syntax.
 *
 * In your step definition, there’s no need to find this text and match it in your Regexp. It will automatically be
 * passed as the last parameter in the step definition.
 */
public class DocString {

    /**
     * The contents of the docstring
     */
    private final String value = null;

    private final String content_type = null;

    /**
     * Line on which docstring occurs
     */
    private final Integer line = 0;

    public String getValue() {
        return value;
    }

    public String getContentType() {
        return content_type;
    }

    public int getLine() {
        return line;
    }

    /**
     * Returns getValue but escaped for HTML and to preserve whitespace
     */
    public String getEscapedValue() {
        String html = StringEscapeUtils.escapeHtml(this.getValue());
        return html.replaceAll("\n", "<br/>").replaceAll(" ", "&nbsp;");
    }

    /**
     * @return Returns true if value has content
     */
    public boolean hasValue() {
        return value.trim().length() > 0;
    }
}
