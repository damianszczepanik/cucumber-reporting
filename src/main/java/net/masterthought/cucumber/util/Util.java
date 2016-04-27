package net.masterthought.cucumber.util;

import java.text.NumberFormat;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

public final class Util {

    // provide Locale so tests can validate . (instead of ,) separator
    public static final NumberFormat PERCENT_FORMATTER = NumberFormat.getPercentInstance(Locale.US);
    static {
        PERCENT_FORMATTER.setMinimumFractionDigits(2);
        PERCENT_FORMATTER.setMaximumFractionDigits(2);
    }

    private static final PeriodFormatter TIME_FORMATTER = new PeriodFormatterBuilder()
            .appendDays()
            .appendSuffix("d")
            .appendSeparator(" ")
            .appendHours()
            .appendSuffix("h")
            .appendSeparator(" ")
            .appendMinutes()
            .appendSuffix("m")
            .appendSeparator(" ")
            .appendSeconds()
            .appendSuffix("s")
            .appendSeparator(" ")
            .minimumPrintedDigits(3)
            .appendMillis()
            .appendSuffix(" ms")
            .toFormatter();

    public static String formatDuration(long duration) {
        return TIME_FORMATTER.print(new Period(0, duration / 1000000));
    }

    /**
     * Converts message into expandable HTML code.
     * 
     * @param messageName
     *            message title
     * @param content
     *            error message
     * @param contentId
     *            id of the message which should be unique per page so expand/collapse works correctly
     * @return expandable message
     */
    public static String formatMessage(String messageName, String content, String contentId) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotEmpty(content)) {
            sb.append(String.format(
                    "<a onclick=\"message=document.getElementById('%s'); message.className = (message.className == 'hidden' ? 'visible' : 'hidden'); return false\" href=\"#\">"
                            + "%s</a><div id=\"%s\" class=\"hidden\"><pre>%s</pre></div>",
                    contentId, messageName, contentId, content));
        }

        return sb.toString();
    }

    /**
     * Converts characters of passed string by replacing to dash (-) each character that might not be accepted as file
     * name such as / ? or &gt;.
     * 
     * @param value
     *            sequence that should be converted
     * @return converted string
     */
    public static String toValidFileName(String value) {
        return value.replaceAll("[^\\d\\w]", "-");
    }
}