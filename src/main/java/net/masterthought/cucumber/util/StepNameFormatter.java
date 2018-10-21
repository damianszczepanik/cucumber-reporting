package net.masterthought.cucumber.util;

import net.masterthought.cucumber.json.MatchArgument;
import org.apache.commons.lang3.StringUtils;

public class StepNameFormatter {
    public static final StepNameFormatter INSTANCE = new StepNameFormatter();

    public static String format(String stepName, MatchArgument[] arguments, String preArgument, String postArgument) {
        String[] chars = stepName.split("(?!^)");
        escape(chars);

        for (MatchArgument argument : arguments) {
            int start = argument.getOffset();
            int end = start + argument.getVal().length() - 1;
            chars[start] = preArgument + chars[start];
            chars[end] = chars[end] + postArgument;
        }
        return StringUtils.join(chars);
    }

    private static void escape(String[] chars) {
        for (int i = 0; i < chars.length; i++) {
            if (chars[i].equals("<")) {
                chars[i] = "&lt;";
            } else if (chars[i].equals(">")) {
                chars[i] = "&gt;";
            } else if (chars[i].equals("&")) {
                chars[i] = "&amp;";
            }
        }
    }
}
