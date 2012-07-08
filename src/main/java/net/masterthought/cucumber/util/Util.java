package net.masterthought.cucumber.util;

import net.masterthought.cucumber.ScenarioTag;
import net.masterthought.cucumber.json.*;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    public static enum Status {
        PASSED, FAILED, SKIPPED, UNDEFINED, MISSING
    }

    public static Map<String, Status> resultMap = new HashMap<String, Status>() {{
        put("passed", Util.Status.PASSED);
        put("failed", Util.Status.FAILED);
        put("skipped", Util.Status.SKIPPED);
        put("undefined", Util.Status.UNDEFINED);
        put("missing", Util.Status.MISSING);
    }};

    public static String result(Status status) {
        String result = "<div>";
        if (status == Status.PASSED) {
            result = "<div class=\"passed\">";
        } else if (status == Status.FAILED) {
            result = "<div class=\"failed\">";
        } else if (status == Status.SKIPPED) {
            result = "<div class=\"skipped\">";
        } else if (status == Status.UNDEFINED) {
            result = "<div class=\"undefined\">";
        } else if (status == Status.MISSING) {
            result = "<div class=\"missing\">";
        }
        return result;
    }

    public static String readFileAsString(String filePath) throws java.io.IOException {
        byte[] buffer = new byte[(int) new File(filePath).length()];
        BufferedInputStream f = null;
        try {
            f = new BufferedInputStream(new FileInputStream(filePath));
            f.read(buffer);
        } finally {
            if (f != null) try {
                f.close();
            } catch (IOException ignored) {
            }
        }
        return new String(buffer);
    }

    public static <T> boolean itemExists(T[] tags) {
        boolean result = false;
        if (tags != null) {
            result = tags.length != 0;
        }
        return result;
    }

    public static boolean itemExists(String item) {
        return !(item.isEmpty() || item == null);
    }

    public static boolean itemExists(List<String> listItem) {
        return listItem.size() != 0;
    }

    public static boolean itemExists(Tag[] tags) {
        boolean result = false;
        if (tags != null) {
            result = tags.length != 0;
        }
        return result;
    }

    public static String passed(boolean value) {
        return value ? "<div class=\"passed\">" : "</div>";
    }

    public static String closeDiv() {
        return "</div>";
    }

    public static <T, R> List<R> collectScenarios(Element[] list, Closure<String, Element> clo) {
        List<R> res = new ArrayList<R>();
        for (final Element t : list) {
            res.add((R) clo.call(t));
        }
        return res;
    }

    public static <T, R> List<R> collectSteps(Step[] list, Closure<String, Step> clo) {
        List<R> res = new ArrayList<R>();
        for (final Step t : list) {
            res.add((R) clo.call(t));
        }
        return res;
    }

    public static <T, R> List<R> collectTags(Tag[] list, StringClosure<String, Tag> clo) {
        List<R> res = new ArrayList<R>();
        for (final Tag t : list) {
            res.add((R) clo.call(t));
        }
        return res;
    }

    public static String U2U(String s) {
        final Pattern p = Pattern.compile("\\\\u\\s*([0-9(A-F|a-f)]{4})", Pattern.MULTILINE);
        String res = s;
        Matcher m = p.matcher(res);
        while (m.find()) {
            res = res.replaceAll("\\" + m.group(0),
                    Character.toString((char) Integer.parseInt(m.group(1), 16)));
        }
        return res;
    }

    public static boolean isValidCucumberJsonReport(String fileContent) {
        return fileContent.contains("Feature");
    }

    public static String formatDuration(Long duration) {
        PeriodFormatter formatter = new PeriodFormatterBuilder()
                .appendDays()
                .appendSuffix(" day", " days")
                .appendSeparator(" and ")
                .appendHours()
                .appendSuffix(" hour", " hours")
                .appendSeparator(" and ")
                .appendMinutes()
                .appendSuffix(" min", " mins")
                .appendSeparator(" and ")
                .appendSeconds()
                .appendSuffix(" sec", " secs")
                .appendSeparator(" and ")
                .appendMillis()
                .appendSuffix(" ms", " ms")
                .toFormatter();
        return formatter.print(new Period(0, duration / 1000000));
    }

    public static List<Step> setStepStatus(List<Step> steps, Step step, Util.Status stepStatus, Util.Status status) {
        if (stepStatus == status) {
            steps.add(step);
        }
        return steps;
    }

    public static List<Element> setScenarioStatus(List<Element> scenarios, Element scenario, Util.Status scenarioStatus, Util.Status status) {
        if (scenarioStatus == status) {
            scenarios.add(scenario);
        }
        return scenarios;
    }

    public static int findStatusCount(List<Util.Status> statuses, Status statusToFind) {
        int occurrence = 0;
        for (Util.Status status : statuses) {
            if (status == statusToFind) {
                occurrence++;
            }
        }
        return occurrence;
    }

    public static boolean hasSteps(Element element) {
        boolean result = element.getSteps() == null || element.getSteps().length == 0;
        if (result) {
            System.out.println("[WARNING] scenario has no steps:  " + element.getName());
        }
        return !result;
    }

    public static boolean hasSteps(ScenarioTag scenario) {
        boolean result = scenario.getScenario().getSteps() == null || scenario.getScenario().getSteps().length == 0;
        if (result) {
            System.out.println("[WARNING] scenario tag has no steps:  " + scenario.getScenario().getName());
        }
        return !result;
    }

    public static boolean hasScenarios(Feature feature) {
        boolean result = feature.getElements() == null || feature.getElements().length == 0;
        if (result) {
            System.out.println("[WARNING] feature has no scenarios:  " + feature.getName());
        }
        return !result;
    }



}
