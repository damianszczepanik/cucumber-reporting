package net.masterthought.cucumber.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

import net.masterthought.cucumber.json.support.Resultsable;
import org.apache.commons.lang3.StringEscapeUtils;
import net.masterthought.cucumber.json.Element;
import net.masterthought.cucumber.json.support.Status;
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
     * @param fileName
     *            sequence that should be converted
     * @return converted string
     */
    public static String toValidFileName(String fileName) {
        return StringEscapeUtils.escapeJava(fileName).replaceAll("[^\\d\\w]", "-");
    }

    /**
     * Searches the given Element array for scenarios that failed and returns a Map containing
     * "error" arrays holding information--scenario name, step/hook name/match.location,
     * (HTML element) ID, and error message--about the failures.
     * @param elements the array of elements containing the scenario and the Resultable
     *                 implementation (step, hook) data
     * @return a Map object containing the failure information
     */
    public static List<String[]> getFailedCauseList(Element[] elements) {
        List<String[]> failedList = new ArrayList<>();
        for(Element element : elements) {
            if(element.isScenario() && (!element.getStatus().isPassed())) {
                Resultsable[] resultsables =
                        joinResultables(element.getBefore(), element.getSteps(), element.getAfter());
                for(Resultsable resultsable : resultsables) {
                    if(resultsable.getResult().getStatus().equals(Status.FAILED)) {
                        String errorMessage = resultsable.getResult().getErrorMessage();
                        String[] info = {
                                element.getName(),
                                resultsable.getResultableName(),
                                resultsable.getId(),
                                errorMessage == null ? "Error message not found." : errorMessage
                        };
                        failedList.add(info);
                    }
                }
            }
        }
        return failedList;
    }

    /**
     * Combines all elements contained within a Resultable array from the given
     * Resultable arrays into one singular array.
     * @param resultsables the Resultable arrays to join
     * @return a single Resultable array containing all elements of the given
     * Resultable arrays
     */
    public static Resultsable[] joinResultables(Resultsable[]... resultsables) {
        Resultsable[] resultsablesArr;
        int resultablesArrLength = 0;
        for(Resultsable[] r : resultsables) {
            resultablesArrLength += r.length;
        }
        resultsablesArr = new Resultsable[resultablesArrLength];
        int currentIndex = 0;
        for(Resultsable[] r : resultsables) {
            System.arraycopy(r, 0, resultsablesArr, currentIndex, r.length);
            currentIndex += r.length;
        }
        return resultsablesArr;
    }
}