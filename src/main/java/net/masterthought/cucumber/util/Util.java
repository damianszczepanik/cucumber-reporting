package net.masterthought.cucumber.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import org.apache.commons.lang3.StringEscapeUtils;
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

    private static final NumberFormat DECIMAL_FORMATTER = DecimalFormat.getInstance(Locale.US);

    static {
        DECIMAL_FORMATTER.setMinimumFractionDigits(2);
        DECIMAL_FORMATTER.setMaximumFractionDigits(2);
    }

    public static final Util INSTANCE = new Util();

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
            .appendSuffix("ms")
            .toFormatter();

    private Util() {
    }

    public static String formatDuration(long duration) {
        return TIME_FORMATTER.print(new Period(0, duration / 1000000));
    }

    /**
     * Returns value converted to percentage format.
     *
     * @param value value to convert
     * @param total sum of all values
     * @return converted values including '%' character
     */
    public static String formatAsPercentage(int value, int total) {
        // value '1F' is to force floating conversion instead of loosing decimal part
        float average = total == 0 ? 0 : 1F * value / total;
        return PERCENT_FORMATTER.format(average);
    }

    public static String formatAsDecimal(int value, int total) {
        float average = total == 0 ? 0 : 100F * value / total;
        return DECIMAL_FORMATTER.format(average);
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
        return StringEscapeUtils.escapeJava(value).replaceAll("[^\\d\\w]", "-");
    }
}