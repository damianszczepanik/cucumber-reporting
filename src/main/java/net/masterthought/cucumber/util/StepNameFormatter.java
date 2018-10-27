package net.masterthought.cucumber.util;

import net.masterthought.cucumber.json.MatchArgument;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class StepNameFormatter {
    public static final StepNameFormatter INSTANCE = new StepNameFormatter();

    public static String format(String stepName, MatchArgument[] arguments, String preArgument, String postArgument) {
        if (ArrayUtils.isEmpty(arguments)) {
            return StringEscapeUtils.escapeHtml(stepName);
        }

        String[] chars = splitIntoCharacters(stepName);

        escape(chars);
        surroundArguments(arguments, preArgument, postArgument, chars);

        return StringUtils.join(chars);
    }

    /**
     * Splits a string into an array of individual characters (each a String).
     * splitIntoCharacters("Text") = ["T", "e", "x", "t"]
     */
    private static String[] splitIntoCharacters(String str) {
        return str.split("(?!^)");
    }

    private static void surroundArguments(MatchArgument[] arguments, String preArgument, String postArgument, String[] chars) {
        for (MatchArgument argument : arguments) {
            int start = argument.getOffset();
            int end = start + argument.getVal().length() - 1;
            chars[start] = preArgument + chars[start];
            chars[end] = chars[end] + postArgument;
        }
    }

    private static void escape(String[] chars) {
        for (int i = 0; i < chars.length; i++) {
            chars[i] = StringEscapeUtils.escapeHtml(chars[i]);
        }
    }
}
