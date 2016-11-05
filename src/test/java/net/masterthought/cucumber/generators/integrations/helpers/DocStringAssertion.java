package net.masterthought.cucumber.generators.integrations.helpers;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.EntityArrays;
import org.apache.commons.lang3.text.translate.LookupTranslator;

import net.masterthought.cucumber.json.DocString;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fang Yuan (fayndee@github)
 */
public class DocStringAssertion extends ReportAssertion {


	/**
	 * Jsoup seems to unescape double-quote, so we create a translator keeping double-quotes intact
	 */
	public static final CharSequenceTranslator ASSERTION_ESCAPE_HTML4 = 
			new AggregateTranslator(
					new LookupTranslator(new String[][]  {
							{"&", "&amp;"},   // & - ampersand
							{"<", "&lt;"},    // < - less-than
							{">", "&gt;"},    // > - greater-than
					}),
					new LookupTranslator(EntityArrays.ISO8859_1_ESCAPE()),
					new LookupTranslator(EntityArrays.HTML40_EXTENDED_ESCAPE())
				);

	public void hasDocString(DocString docString) {
		assertThat(docString).isNotNull();
		assertThat(oneBySelector("pre", WebAssertion.class).html()).isEqualTo(ASSERTION_ESCAPE_HTML4.translate(docString.getValue()));
	}
}
