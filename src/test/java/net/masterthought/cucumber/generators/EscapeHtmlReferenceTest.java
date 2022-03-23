package net.masterthought.cucumber.generators;

import org.apache.velocity.app.event.ReferenceInsertionEventHandler;
import org.junit.Test;

import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author M.P. Korstanje (mpkorstanje@github)
 */
public class EscapeHtmlReferenceTest {

    private static final String SOME_REFERENCE = "someReference";
    private final ReferenceInsertionEventHandler insertionEventHandler = new EscapeHtmlReference();

    @Test
    public void referenceInsert_returnNormalText(){
        // given
        String normalText = "a plain statement";

        // when
        Object result = insertionEventHandler.referenceInsert(null, SOME_REFERENCE, normalText);

        // then
        assertThat(result).isEqualTo(normalText);
    }

    @Test
    public void referenceInsert_shouldEscapeHtmlForAnyLabel(){
        // given
        String html = "<b>a bold statement</b>";

        // when
        Object result = insertionEventHandler.referenceInsert(null, SOME_REFERENCE, html);

        // then
        assertThat(result).isEqualTo(escapeHtml(html));
    }

    @Test
    public void referenceInsert_shouldNotEscapeWithSpecialTag(){
        // given
        String html = "<b>a bold statement</b>";

        // when
        Object result = insertionEventHandler.referenceInsert(null, "$_noescape_" + SOME_REFERENCE, html);

        // then
        assertThat(result).isEqualTo(html);
    }

    @Test
    public void referenceInsert_shouldReturnNullForNull(){
        // given
        String html = null;

        // when
        Object result = insertionEventHandler.referenceInsert(null, SOME_REFERENCE, html);

        // then
        assertThat(result).isNull();
    }

    @Test
    public void referenceInsert_shouldSanitize(){
        // given
        String html = "<a href=\"www.example.com\" rel=\"nofollow noopener noreferrer\">a hyper web reference</a>";

        // when
        Object result = insertionEventHandler.referenceInsert(null, "$_sanitize_" + SOME_REFERENCE, html);

        // result
        assertThat(result).isEqualTo(html);
    }
}
