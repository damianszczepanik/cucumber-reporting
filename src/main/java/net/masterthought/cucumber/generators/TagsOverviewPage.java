package net.masterthought.cucumber.generators;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportResult;
import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.json.support.TagObject;

public class TagsOverviewPage extends AbstractPage {

    private static final NumberFormat DECIMAL_FORMATTER = DecimalFormat.getInstance(Locale.US);
    static {
        DECIMAL_FORMATTER.setMinimumFractionDigits(2);
        DECIMAL_FORMATTER.setMaximumFractionDigits(2);
    }

    public static final String WEB_PAGE = "overview-tags.html";

    public TagsOverviewPage(ReportResult reportResult, Configuration configuration) {
        super(reportResult, "overviewTags.vm", configuration);
    }

    @Override
    public String getWebPage() {
        return WEB_PAGE;
    }

    @Override
    public void prepareReport() {
        List<TagObject> tags = report.getAllTags();
        context.put("all_tags", tags);
        context.put("report_summary", report.getTagReport());

        context.put("chart_categories", generateTagLabels(filterExcludedTags(tags)));
        context.put("chart_data", generateTagValues(filterExcludedTags(tags)));
    }

    static String generateTagLabels(List<TagObject> tagsObjectList) {
        int tagCount = tagsObjectList.size();
        String[] tagNames = new String[tagCount];

        for (int i = 0; i < tagCount; i++) {
            tagNames[i] = StringUtils.wrap(tagsObjectList.get(i).getName(), "\"");
        }
        return "[" + StringUtils.join(tagNames, ",") + "]";
    }

	private List<TagObject> filterExcludedTags(List<TagObject> tagsObjectList) {
		List<TagObject> filteredTags = new ArrayList<>();
		for (TagObject tagObject : tagsObjectList) {
			String tagName = tagObject.getName();
			if (shouldIncludeTag(tagName)) {
				filteredTags.add(tagObject);
			}
		}
		return filteredTags;
	}

	private boolean shouldIncludeTag(String tagName) {
		for (Pattern pattern : configuration.getTagsToExcludeFromChart()) {
			if (tagName.matches(pattern.pattern())) {
				return false;
			}
		}
		return true;
	}

    static List<String> generateTagValues(List<TagObject> tagsObjectList) {
        int tagsCount = tagsObjectList.size();
        String[][] values = new String[Status.values().length][tagsCount];
        for (int i = 0; i < tagsCount; i++) {
            final TagObject tagObject = tagsObjectList.get(i);
            final int allSteps = tagObject.getSteps();
            values[0][i] = formatAsPercentage(tagObject.getPassedSteps(), allSteps);
            values[1][i] = formatAsPercentage(tagObject.getFailedSteps(), allSteps);
            values[2][i] = formatAsPercentage(tagObject.getSkippedSteps(), allSteps);
            values[3][i] = formatAsPercentage(tagObject.getPendingSteps(), allSteps);
            values[4][i] = formatAsPercentage(tagObject.getUndefinedSteps(), allSteps);
        }

        List<String> statuses = new ArrayList<>();
        for (int i = 0; i < Status.values().length; i++) {
            statuses.add("[" + StringUtils.join(values[i], ", ") + "]");
        }

        return statuses;
    }

    static String formatAsPercentage(int value, int sum) {
        return DECIMAL_FORMATTER.format(100F * value / sum);
    }
}
