package net.masterthought.cucumber.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.masterthought.cucumber.json.Tag;

public class Util {

    private static final Logger LOG = LogManager.getLogger(Util.class);

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
            .appendSuffix("ms")
            .toFormatter();

    public static String formatDuration(long duration) {
        return TIME_FORMATTER.print(new Period(0, duration / 1000000));
    }

    public static String tagsToHtml(Tag[] tags) {
        String result = "<div class=\"feature-tags\"></div>";
        if (!ArrayUtils.isEmpty(tags)) {
            List<String> tagList = new ArrayList<>();
            for (Tag tag : tags) {
                String link = tag.getName().replace("@", "").trim() + ".html";
                String ref = "<a href=\"" + link + "\">" + tag.getName() + "</a>";
                tagList.add(ref);
            }
            result = "<div class=\"feature-tags\">" + StringUtils.join(tagList.toArray(), ",") + "</div>";
        }
        return result;
    }

    public static void unzipToFile(File srcZipFile, String destDirectory) {
        try {
            ZipFile zipFile = new ZipFile(srcZipFile);
            zipFile.extractAll(destDirectory);
        } catch (ZipException e) {
            LOG.error("Could not unzip {} into {}.", srcZipFile.getAbsolutePath(), destDirectory, e);
        }
    }

    /**
     * Converts error message into HTML code
     * 
     * @param errorMessage
     *            error message
     * @param errorID
     *            id of the message which should be unique per page so expand/collapse works correctly
     * @return formatted message
     */
    public static String formatErrorMessage(String errorMessage, int errorID) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isEmpty(errorMessage)) {
            sb.append(StringUtils.EMPTY);
        } else {
            sb.append("<pre class=\"error_message\">");
            // split the message to the header (first line) and the content (rest)
            String[] headLineAndMessage = StringUtils.split(errorMessage, "\n", 2);
            if (headLineAndMessage.length == 2) {
                sb.append(String.format("<input class=\"error_collapse\" id=\"error_%s\" type=\"checkbox\">", errorID));
                sb.append(String.format("<label for=\"error_%s\">%s</label>", errorID, headLineAndMessage[0]));
                sb.append(String.format("<div>%s</div>", headLineAndMessage[1].replaceAll("\n", "<br/>")));
            } else {
                // one-line message
                sb.append(errorMessage.replaceAll("\\\\n", "<br/>"));
            }

            sb.append("</pre>");
        }

        return sb.toString();
    }
}