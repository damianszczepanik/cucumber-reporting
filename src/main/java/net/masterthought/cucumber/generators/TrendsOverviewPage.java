package net.masterthought.cucumber.generators;

import org.apache.commons.lang3.StringUtils;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportResult;
import net.masterthought.cucumber.Trends;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class TrendsOverviewPage extends AbstractPage {

    public static final String WEB_PAGE = "overview-trends.html";

    private final Trends trends;

    public TrendsOverviewPage(ReportResult reportResult, Configuration configuration, Trends trends) {
        super(reportResult, "overviewTrends.vm", configuration);
        this.trends = trends;
    }

    @Override
    public String getWebPage() {
        return WEB_PAGE;
    }

    @Override
    public void prepareReport() {
        context.put("buildNumbers", toJavaScriptArray(trends.getBuildNumbers()));

        context.put("failedFeatures", toJavaScriptArray(trends.getFailedFeatures()));
        context.put("totalFeatures", toJavaScriptArray(trends.getTotalFeatures()));
        context.put("failedScenarios", toJavaScriptArray(trends.getFailedScenarios()));
        context.put("totalScenarios", toJavaScriptArray(trends.getTotalScenarios()));

        context.put("passedSteps", toJavaScriptArray(trends.getPassedSteps()));
        context.put("failedSteps", toJavaScriptArray(trends.getFailedSteps()));
        context.put("skippedSteps", toJavaScriptArray(trends.getSkippedSteps()));
        context.put("pendingSteps", toJavaScriptArray(trends.getPendingSteps()));
        context.put("undefinedSteps", toJavaScriptArray(trends.getUndefinedSteps()));
    }

    private static String toJavaScriptArray(String[] array) {
        int itemCount = array.length;
        String[] names = new String[itemCount];

        for (int i = 0; i < itemCount; i++) {
            names[i] = StringUtils.wrap(array[i], "\"");
        }
        return "[" + StringUtils.join(names, ",") + "]";
    }

    private static String toJavaScriptArray(int[] array) {
        return "[" + StringUtils.join(array, ',') + "]";
    }
}
