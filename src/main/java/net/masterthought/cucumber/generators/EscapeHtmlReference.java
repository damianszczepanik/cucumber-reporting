package net.masterthought.cucumber.generators;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.velocity.app.event.ReferenceInsertionEventHandler;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

/**
 * Escapes all html and xml that was provided in a reference before inserting it into a template.
 *
 * References that start with $_sanitize_ will be sanitized to allow urls.
 */
final class EscapeHtmlReference implements ReferenceInsertionEventHandler {

    private static final PolicyFactory TAGS = new HtmlPolicyBuilder()
            .allowStandardUrlProtocols().allowElements("a", "span", "img").allowAttributes("href", "onclick", "src", "id", "style")
            .onElements("a", "span", "img").requireRelNofollowOnLinks().requireRelsOnLinks("noopener", "noreferrer")
            .toFactory();

    @Override
    public Object referenceInsert(String reference, Object value) {
        if (value == null) {
            return null;
        } else if(reference.startsWith("$_sanitize_")) {
            return TAGS.sanitize(value.toString());
        } else {
            return StringEscapeUtils.escapeHtml(value.toString());
        }
    }

}
