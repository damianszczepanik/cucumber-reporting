package net.masterthought.cucumber.json;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * Doc Strings are handy for specifying a larger piece of text. This is inspired from Python’s Docstring syntax.
 *
 * In your step definition, there’s no need to find this text and match it in your Regexp. It will automatically be passed as the last parameter in the step definition.
 */
public class DocString {

    /**
     * The contents of the docstring
     */
    private String value;

    /**
     * ?
     */
    private String content_type;

    /**
     * Line on which docstring occurs
     */
    private Integer line;

    public DocString() {
        // no arg constructor required for gson
    }

    public String getValue() {
        return value;
    }

    public String getContentType() {
        return content_type;
    }

    public Integer getLine() {
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
        return value != null && value.trim().length() > 0;
    }
}
