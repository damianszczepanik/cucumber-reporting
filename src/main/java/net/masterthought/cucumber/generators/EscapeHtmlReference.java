package net.masterthought.cucumber.generators;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.velocity.app.event.ReferenceInsertionEventHandler;

/**
 * Escapes all html and xml that was provided in a reference before inserting it into a template.
 */
final class EscapeHtmlReference implements ReferenceInsertionEventHandler {

    @Override
    public Object referenceInsert(String reference, Object value) {
        if (value == null) {
            return null;
        } else {
            return StringEscapeUtils.escapeHtml(value.toString());
        }
    }

}
