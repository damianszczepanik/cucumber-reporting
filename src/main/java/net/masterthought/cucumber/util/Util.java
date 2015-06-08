package net.masterthought.cucumber.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.masterthought.cucumber.json.Element;
import net.masterthought.cucumber.json.Tag;

import org.apache.commons.io.IOUtils;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import com.googlecode.totallylazy.Sequence;

public class Util {
    private static final PeriodFormatter TIME_FORMATTER = new PeriodFormatterBuilder()
            .appendDays()
            .appendSuffix("d")
            .appendSeparator(" ")
            .appendHours()
            .appendSuffix("h")
            .appendSeparator(" ")
            .minimumPrintedDigits(2)
            .appendMinutes()
            .appendSuffix("m")
            .appendSeparator(" ")
            .minimumPrintedDigits(2)
            .appendSeconds()
            .appendSuffix("s")
            .appendSeparator(" ")
            .minimumPrintedDigits(3)
            .appendMillis()
            .appendSuffix("ms")
            .toFormatter();

    public static String readFileAsString(String filePath) throws IOException {
        StringWriter writer = new StringWriter();
        try (BufferedInputStream stream = new BufferedInputStream(new FileInputStream(filePath))) {
            IOUtils.copy(stream, writer);
        }
        return writer.toString();
    }

    public static boolean itemExists(String item) {
        return item != null && !item.isEmpty();
    }

    public static boolean itemExists(List<String> listItem) {
        return !listItem.isEmpty();
    }

    public static boolean itemExists(Sequence<Element> sequence) {
        return !sequence.isEmpty();
    }

    public static boolean itemExists(Tag[] tags) {
        return tags != null && tags.length != 0;
    }

    public static String passed(boolean value) {
        return value ? "<div class=\"passed\">" : "</div>";
    }

    public static String U2U(String s) {
        final Pattern p = Pattern.compile("\\\\u\\s*([0-9(A-F|a-f)]{4})", Pattern.MULTILINE);
        String res = s;
        Matcher m = p.matcher(res);
        while (m.find()) {
            res = res.replaceAll("\\" + m.group(0),
                    Character.toString((char) Integer.parseInt(m.group(1), 16)));
            m = p.matcher(res);
        }
        return res;
    }

    public static boolean isValidCucumberJsonReport(String fileContent) {
        return fileContent.contains("\"keyword\":");
    }

    public static String formatDuration(Long duration) {
        return TIME_FORMATTER.print(new Period(0, duration / 1000000));
    }

    public static int findStatusCount(List<Status> statuses, Status statusToFind) {
        int occurrence = 0;
        for (Status status : statuses) {
            if (status == statusToFind) {
                occurrence++;
            }
        }
        return occurrence;
    }

}
