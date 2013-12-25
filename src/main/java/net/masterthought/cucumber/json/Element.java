package net.masterthought.cucumber.json;

import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;

import net.masterthought.cucumber.ConfigurationOptions;
import net.masterthought.cucumber.util.Util;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.googlecode.totallylazy.Option.option;

public class Element {

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

    public Util.Status getStatus() {
    	// can be optimized to retrieve only the count of elements and not the all list
        int results = getSteps().filter(Step.predicates.hasStatus(Util.Status.FAILED)).size();
        
        if (results == 0 && ConfigurationOptions.skippedFailsBuild()) {
        	results = getSteps().filter(Step.predicates.hasStatus(Util.Status.SKIPPED)).size();
        }

        if (results == 0 && ConfigurationOptions.undefinedFailsBuild()) {
        	results = getSteps().filter(Step.predicates.hasStatus(Util.Status.UNDEFINED)).size();
        }
        
        return results == 0 ? Util.Status.PASSED : Util.Status.FAILED;
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
            contentString.add("<span class=\"scenario-keyword\">" + keyword + ": </span>");
        }

        if (Util.itemExists(name)) {
            contentString.add("<span class=\"scenario-name\">" + name + "</span>");
        }

        return Util.itemExists(contentString) ? Util.result(getStatus()) + StringUtils.join(contentString.toArray(), " ") + Util.closeDiv() : "";
    }

    public Sequence<String> getTagList() {
        return processTags();
    }

    public boolean hasTags() {
        return Util.itemExists(tags);
    }

    private Sequence<String> processTags() {
        return getTags().map(Tag.functions.getName());
    }

    public String getTagsList() {
        String result = "<div class=\"feature-tags\"></div>";
        if (Util.itemExists(tags)) {
            String tagList = StringUtils.join(processTags().toList().toArray(), ",");
            result = "<div class=\"feature-tags\">" + tagList + "</div>";
        }
        return result;
    }

    public static class functions {
        public static Function1<Element, Util.Status> status() {
            return new Function1<Element, Util.Status>() {
                @Override
                public Util.Status call(Element element) throws Exception {
                    return element.getStatus();
                }
            };
        }
    }

}
