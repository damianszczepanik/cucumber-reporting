package net.masterthought.cucumber.generators;

import static org.apache.commons.text.StringEscapeUtils.escapeHtml4;
import static org.assertj.core.api.Assertions.assertThat;

import org.apache.velocity.app.event.ReferenceInsertionEventHandler;
import org.junit.jupiter.api.Test;

/**
 * @author M.P. Korstanje (mpkorstanje@github)
 */
class EscapeHtmlReferenceTest {

    private static final String SOME_REFERENCE = "someReference";
    private final ReferenceInsertionEventHandler insertionEventHandler = new EscapeHtmlReference();

    @Test
    void referenceInsert_returnNormalText() {
        // given
        String normalText = "a plain statement";

        // when
        Object result = insertionEventHandler.referenceInsert(null, SOME_REFERENCE, normalText);

        // then
        assertThat(result).isEqualTo(normalText);
    }

    @Test
    void referenceInsert_shouldEscapeHtmlForAnyLabel() {
        // given
        String html = "<b>a bold statement</b>";

        // when
        Object result = insertionEventHandler.referenceInsert(null, SOME_REFERENCE, html);

        // then
        assertThat(result).isEqualTo(escapeHtml4(html));
    }

    @Test
    void referenceInsert_shouldNotEscapeWithSpecialTag() {
        // given
        String html = "<b>a bold statement</b>";

        // when
        Object result = insertionEventHandler.referenceInsert(null, "$_noescape_" + SOME_REFERENCE, html);

        // then
        assertThat(result).isEqualTo(html);
    }

    @Test
    void referenceInsert_shouldReturnNullForNull() {
        // given
        String html = null;

        // when
        Object result = insertionEventHandler.referenceInsert(null, SOME_REFERENCE, html);

        // then
        assertThat(result).isNull();
    }
}
