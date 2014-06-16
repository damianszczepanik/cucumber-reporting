package net.masterthought.cucumber.json;

import com.google.common.base.Joiner;
import com.google.gson.internal.LinkedTreeMap;
import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.predicates.LogicalPredicate;
import net.masterthought.cucumber.ConfigurationOptions;
import net.masterthought.cucumber.util.Util;

import java.util.List;
import java.util.UUID;

import static com.googlecode.totallylazy.Option.option;
import static org.apache.commons.lang.StringUtils.EMPTY;

public class Step {

    private String name;
    private String keyword;
    private String line;
    private Result result;
    private Row[] rows;
    private Match match;
    private Object[] embeddings;
    private String[] output;
    private DocString doc_string;

    public Step() {

    }

    public DocString getDocString() {
        return doc_string;
    }

    public Row[] getRows() {
        return rows;
    }

    public String getOutput() {
        List<String> outputList = Sequences.sequence(option(output).getOrElse(new String[]{})).realise().toList();
        return Joiner.on("").skipNulls().join(outputList);
    }

    public Match getMatch() {
        return match;
    }

    public Object[] getEmbeddings() {
        return embeddings;
    }

    public boolean hasRows() {
        boolean result = false;
        if (rows != null) {
            if (rows.length > 0) {
                result = true;
            }
        }
        return result;
    }

    /**
     * @return - Returns true if has a sub doc-string, and that doc-string has a value
     */
    public boolean hasDocString() {
        return doc_string != null && doc_string.hasValue();
    }

    public Util.Status getStatus() {
        if (result == null) {
            System.out.println("[WARNING] Line " + line + " : " + "Step is missing Result: " + keyword + " : " + name);
            return Util.Status.MISSING;
        } else {
            return Util.resultMap.get(result.getStatus());
        }
    }

    public Long getDuration() {
        if (result == null) {
            return 1L;
        } else {
            return result.getDuration();
        }
    }

    public String getDataTableClass() {
        String content = "";
        Util.Status status = getStatus();
        if (status == Util.Status.FAILED) {
            content = "failed";
        } else if (status == Util.Status.PASSED) {
            content = "passed";
        } else if (status == Util.Status.SKIPPED) {
            content = "skipped";
        } else {
            content = "";
        }
        return content;
    }

    public String getRawName() {
        return name;
    }

    public String getName() {
        String content = "";
        if (getStatus() == Util.Status.FAILED) {
            String errorMessage = result.getErrorMessage();
            if (getStatus() == Util.Status.SKIPPED) {
                errorMessage = "Mode: Skipped causes Failure<br/><span class=\"skipped\">This step was skipped</span>";
            }
            if (getStatus() == Util.Status.UNDEFINED) {
                errorMessage = "Mode: Not Implemented causes Failure<br/><span class=\"undefined\">This step is not yet implemented</span>";
            }
            content = Util.result(getStatus()) + "<span class=\"step-keyword\">" + keyword + " </span><span class=\"step-name\">" + name + "</span><span class=\"step-duration\">" + Util.formatDuration(result.getDuration()) + "</span><div class=\"step-error-message\"><pre>" + formatError(errorMessage) + "</pre></div>" + Util.closeDiv() + getImageTags();
        } else if (getStatus() == Util.Status.MISSING) {
            String errorMessage = "<span class=\"missing\">Result was missing for this step</span>";
            content = Util.result(getStatus()) + "<span class=\"step-keyword\">" + keyword + " </span><span class=\"step-name\">" + name + "</span><span class=\"step-duration\"></span><div class=\"step-error-message\"><pre>" + formatError(errorMessage) + "</pre></div>" + Util.closeDiv();
        } else {
            content = getNameAndDuration();
        }
        return content;
    }

    private String getNameAndDuration() {
        String content = Util.result(getStatus())
                + "<span class=\"step-keyword\">" + keyword
                + " </span><span class=\"step-name\">" + name + "</span>"
                + "<span class=\"step-duration\">" + Util.formatDuration(result.getDuration()) + "</span>"
                + Util.closeDiv() + getImageTags();

        return content;
    }

    /**
     * Returns a formatted doc-string section.
     * This is formatted w.r.t the parent Step element.
     * To preserve whitespace in example, line breaks and whitespace are preserved
     *
     * @return string of html
     */
    public String getDocStringOrNothing() {
        if (!hasDocString()) {
            return "";
        }
        return Util.result(getStatus()) +
                "<div class=\"doc-string\">" +
                getDocString().getEscapedValue() +
                Util.closeDiv() +
                Util.closeDiv();
    }

    private String formatError(String errorMessage) {
        String result = errorMessage;
        if (errorMessage != null && !errorMessage.isEmpty()) {
            result = errorMessage.replaceAll("\\\\n", "<br/>");
        }
        return result;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public String getImageTags() {
        if (noEmbeddedScreenshots()) return EMPTY;

        String links = EMPTY;
        int index = 1;
        for (Object image : embeddings) {
            if (image != null) {
                String mimeEncodedImage = mimeEncodeEmbededImage(image);
                String imageId = UUID.nameUUIDFromBytes(mimeEncodedImage.getBytes()).toString();
                links = links + "<a href=\"\" onclick=\"img=document.getElementById('" + imageId + "'); img.style.display = (img.style.display == 'none' ? 'block' : 'none');return false\">Screenshot " + index++ + "</a>" +
                        "<img id='" + imageId + "' style='display:none;max-width: 250px;' src='" + mimeEncodedImage + "'>\n";
            }
        }
        return links;
    }

    private boolean noEmbeddedScreenshots() {
        return getEmbeddings() == null;
    }


    public static String mimeEncodeEmbededImage(Object image) {
        return "data:image/png;base64," + ((LinkedTreeMap) image).get("data");

    }

    public static String uuidForImage(Object image) {
        return UUID.nameUUIDFromBytes(mimeEncodeEmbededImage(image).getBytes()).toString();
    }

    public static class functions {
        public static Function1<Step, Util.Status> status() {
            return new Function1<Step, Util.Status>() {
                @Override
                public Util.Status call(Step step) throws Exception {
                    return step.getStatus();
                }
            };
        }
    }

    public static class predicates {

        public static LogicalPredicate<Step> hasStatus(final Util.Status status) {
            return new LogicalPredicate<Step>() {
                @Override
                public boolean matches(Step step) {
                    return step.getStatus().equals(status);
                }
            };
        }


        public static Function1<Step, Util.Status> status() {
            return new Function1<Step, Util.Status>() {
                @Override
                public Util.Status call(Step step) throws Exception {
                    return step.getStatus();
                }
            };
        }
    }

}
