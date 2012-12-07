package net.masterthought.cucumber.json;

import net.masterthought.cucumber.ConfigurationOptions;
import net.masterthought.cucumber.util.Util;
import com.google.gson.internal.StringMap;
import org.joda.time.DateTime;

import static org.apache.commons.lang.StringUtils.EMPTY;

public class Step {

    private String name;
    private String keyword;
    private String line;
    private Result result;
    private Row[] rows;
    private Match match;
    private Object[] embeddings;

    public Step() {

    }

    public Row[] getRows() {
        return rows;
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

    private Util.Status getInternalStatus() {
        if (result == null) {
            System.out.println("[WARNING] Line " + line + " : " + "Step is missing Result: " + keyword + " : " + name);
            return Util.Status.MISSING;
        } else {
            return Util.resultMap.get(result.getStatus());
        }
    }

    public Util.Status getStatus() {
        Util.Status status = getInternalStatus();
        Util.Status result = status;

        if (ConfigurationOptions.skippedFailsBuild()) {
            if (status == Util.Status.SKIPPED || status == Util.Status.FAILED) {
                result = Util.Status.FAILED;
            }
        }

        if (ConfigurationOptions.undefinedFailsBuild()) {
            if (status == Util.Status.UNDEFINED || status == Util.Status.FAILED) {
                result = Util.Status.FAILED;
            }
        }

        if (status == Util.Status.FAILED) {
            result = Util.Status.FAILED;
        }
        return result;
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
            if (getInternalStatus() == Util.Status.SKIPPED) {
                errorMessage = "Mode: Skipped causes Failure<br/><span class=\"skipped\">This step was skipped</span>";
            }
            if (getInternalStatus() == Util.Status.UNDEFINED) {
                errorMessage = "Mode: Not Implemented causes Failure<br/><span class=\"undefined\">This step is not yet implemented</span>";
            }
            content = Util.result(getStatus()) + "<span class=\"step-keyword\">" + keyword + " </span><span class=\"step-name\">" + name + "</span>" + "<div class=\"step-error-message\"><pre>" + formatError(errorMessage) + "</pre></div>" + Util.closeDiv() + getImageTag();
        } else if (getStatus() == Util.Status.MISSING) {
            String errorMessage = "<span class=\"missing\">Result was missing for this step</span>";
            content = Util.result(getStatus()) + "<span class=\"step-keyword\">" + keyword + " </span><span class=\"step-name\">" + name + "</span>" + "<div class=\"step-error-message\"><pre>" + formatError(errorMessage) + "</pre></div>" + Util.closeDiv();
        } else {
            content = Util.result(getStatus()) + "<span class=\"step-keyword\">" + keyword + " </span><span class=\"step-name\">" + name + "</span>" + Util.closeDiv() + getImageTag();
        }
        return content;
    }

    private String formatError(String errorMessage) {
        String result = errorMessage;
        if (errorMessage != null || !errorMessage.isEmpty()) {
            result = errorMessage.replaceAll("\\\\n", "<br/>");
        }
        return result;
    }

    public void setName(String newName) {
      this.name = newName;
    }

    public String getImageTag() {
        if(noEmbeddedScreenshots()) return EMPTY;

        String imageId = Long.toString(new DateTime().getMillis());
        return "<a href=\"\" onclick=\"img=document.getElementById('"+imageId+"'); img.style.display = (img.style.display == 'none' ? 'block' : 'none');return false\">Screenshot</a>" +
                "<img id='"+imageId+"' style='display:none' src='"+ getMimeEncodedEmbeddedImage() +"'>";
    }

    private boolean noEmbeddedScreenshots() {
        return getEmbeddings() == null;
    }

    public String getMimeEncodedEmbeddedImage() {
        return "data:image/png;base64,"+((StringMap)getEmbeddings()[0]).get("data");
    }
}
