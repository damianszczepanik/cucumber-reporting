package net.masterthought.cucumber.json;

import static com.googlecode.totallylazy.Option.option;

import java.util.ArrayList;
import java.util.List;

import net.masterthought.cucumber.ConfigurationOptions;
import net.masterthought.cucumber.util.Status;
import net.masterthought.cucumber.util.Util;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;

public class Element {

    /** Refers to background step. Is defined in json file. */
    private final static String BACKGROUND_KEYWORD = "Background";

    private String name;
    private String description;
    private String keyword;
    private Step[] steps;
    private Tag[] tags;

    public Element() {
    }

    public Sequence<Step> getSteps() {
        return Sequences.sequence(option(steps).getOrElse(new Step[]{})).realise();
    }

    public Sequence<Tag> getTags() {
        return Sequences.sequence(option(tags).getOrElse(new Tag[]{})).realise();
    }

    public Status getStatus() {
    	// can be optimized to retrieve only the count of elements and not the all list
        int results = getSteps().filter(Step.predicates.hasStatus(Status.FAILED)).size();
        
        if (results == 0 && ConfigurationOptions.skippedFailsBuild()) {
        	results = getSteps().filter(Step.predicates.hasStatus(Status.SKIPPED)).size();
        }

        if (results == 0 && ConfigurationOptions.undefinedFailsBuild()) {
        	results = getSteps().filter(Step.predicates.hasStatus(Status.UNDEFINED)).size();
        }
        
        return results == 0 ? Status.PASSED : Status.FAILED;
    }

    public String getRawName() {
        return name;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getName() {
        List<String> contentString = new ArrayList<String>();

        if (Util.itemExists(keyword)) {
            contentString.add("<span class=\"scenario-keyword\">" + StringEscapeUtils.escapeHtml(keyword) + ": </span>");
        }

        if (Util.itemExists(name)) {
            contentString.add("<span class=\"scenario-name\">" + StringEscapeUtils.escapeHtml(name) + "</span>");
        }

        return Util.itemExists(contentString) ? getStatus().toHtmlClass()
                + StringUtils.join(contentString.toArray(), " ") + Util.closeDiv() : "";
    }

    public Sequence<String> getTagList() {
        return processTags();
    }

    public boolean hasTags() {
        return Util.itemExists(tags);
    }

    public boolean hasSteps() {
        return !getSteps().isEmpty();
    }

    private Sequence<String> processTags() {
        return getTags().map(Tag.functions.getName());
    }

    public boolean isBackground() {
        return keyword.equals(BACKGROUND_KEYWORD);
    }

    public String getTagsList() {
        String result = "<div class=\"feature-tags\"></div>";
        if (Util.itemExists(tags)) {
            List<String> str = getTagList().toList();
            List<String> tagList = new ArrayList<String>();
            for(String s : str) {
                String link = s.replace("@", "").trim() + ".html";
                String ref = "<a href=\"" + link + "\">" + s + "</a>";
                tagList.add(ref);
            }
            result = "<div class=\"feature-tags\">" + StringUtils.join(tagList.toArray(), ",") + "</div>";
        }
        return result;
    }

    public static class Functions {
        public static Function1<Element, Status> status() {
            return new Function1<Element, Status>() {
                @Override
                public Status call(Element element) throws Exception {
                    return element.getStatus();
                }
            };
        }
    }

}
