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
            .minimumPrintedDigits(2)
            .appendSeconds()
            .appendSuffix("s")
            .appendSeparator(" ")
            .minimumPrintedDigits(3)
            .appendMillis()
            .appendSuffix("&nbsp;ms")
            .toFormatter();

    public static String formatDuration(long duration) {
        return TIME_FORMATTER.print(new Period(0, duration / 1000000));
    }

    /**
     * Converts message into HTML code.
     * 
     * @param message
     *            error message
     * @param messageId
     *            id of the message which should be unique per page so expand/collapse works correctly
     * @return formatted message
     */
    public static String formatMessage(String message, int messageId) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isEmpty(message)) {
            sb.append(StringUtils.EMPTY);
        } else {
            sb.append("<div class=\"output_message\">");
            // split the message to the header (first line) and the content (rest)
            String[] headLineAndMessage = StringUtils.split(message, "\n", 2);
            if (headLineAndMessage.length == 2) {
                sb.append(String.format("<input class=\"output_collapse\" id=\"output_%d\" type=\"checkbox\">", messageId));
                sb.append(String.format("<label for=\"output_%d\">%s</label>", messageId, headLineAndMessage[0]));
                sb.append(String.format("<div>%s</div>", headLineAndMessage[1].replaceAll("\n", "<br/>")));
            } else {
                // one-line message
                sb.append(message.replaceAll("\\\\n", "<br/>"));
            }

            sb.append("</div>");
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