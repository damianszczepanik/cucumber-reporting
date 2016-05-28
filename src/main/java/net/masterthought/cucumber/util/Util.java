package net.masterthought.cucumber.util;

import java.text.NumberFormat;
import java.util.Locale;

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
            .appendSuffix("ms")
            .toFormatter();

    public static String formatDuration(long duration) {
        return TIME_FORMATTER.print(new Period(0, duration / 1000000));
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