package net.masterthought.cucumber.generators;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.velocity.app.event.ReferenceInsertionEventHandler;
import org.apache.velocity.context.Context;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

/**
 * Escapes all html and xml that was provided in a reference before inserting it into a template.
 *
 * References that start with $_sanitize_ will be sanitized to allow urls.
 */
final class EscapeHtmlReference implements ReferenceInsertionEventHandler {

    private static final PolicyFactory LINKS = new HtmlPolicyBuilder()
            .allowStandardUrlProtocols().allowElements("a").allowAttributes("href")
            .onElements("a").requireRelNofollowOnLinks().requireRelsOnLinks("noopener", "noreferrer")
            .toFactory();

    @Override
    public Object referenceInsert(Context context, String reference, Object value) {
        if (value == null) {
            return null;
        } else if(reference.startsWith("$_sanitize_")) {
            return LINKS.sanitize(value.toString());
        } else if(reference.startsWith("$_noescape_")) {
            return value.toString();
        } else {
            return StringEscapeUtils.escapeHtml(value.toString());
        }
    }

}
