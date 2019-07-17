package net.masterthought.cucumber.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import net.masterthought.cucumber.json.Hook;

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
            .appendSeparator(" ")
            .appendHours()
            .appendSeparator(":")
            .appendMinutes()
            .appendSeparator(":")
            .printZeroAlways()
            .appendSeconds()
            .appendSeparator(".")
            .minimumPrintedDigits(3)
            .appendMillis()
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
     * Converts characters of passed string and replaces to hash which can be treated as valid file name.
     *
     * @param fileName
     *            sequence that should be converted
     * @return converted string
     */
    public static String toValidFileName(String fileName) {
        // adds MAX_VALUE to eliminate minus character which might be returned by hashCode()
        return Long.toString((long) fileName.hashCode() + Integer.MAX_VALUE);
    }

    /**
     * Helper method that removes empty hooks from passed array and packs it into new collection.
     *
     * @param hooks hooks to be reduced
     * @return no empty hooks
     */
    public static List<Hook> eliminateEmptyHooks(Hook[] hooks) {
        return Arrays.asList(hooks).stream()
                .filter(Hook::hasContent)
                .collect(Collectors.toList());
    }
}