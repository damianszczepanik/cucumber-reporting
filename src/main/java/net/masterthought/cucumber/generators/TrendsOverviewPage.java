package net.masterthought.cucumber.generators;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportResult;
import net.masterthought.cucumber.TrendTableRow;
import net.masterthought.cucumber.Trends;

import java.util.ArrayList;
import java.util.List;

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
        context.put("buildNumbers", trends.getBuildNumbers());

        context.put("failedFeatures", trends.getFailedFeatures());
        context.put("totalFeatures", trends.getTotalFeatures());
        context.put("failedScenarios", trends.getFailedScenarios());
        context.put("totalScenarios", trends.getTotalScenarios());

        context.put("passedSteps", trends.getPassedSteps());
        context.put("failedSteps", trends.getFailedSteps());
        context.put("skippedSteps", trends.getSkippedSteps());
        context.put("pendingSteps", trends.getPendingSteps());
        context.put("undefinedSteps", trends.getUndefinedSteps());

        context.put("durations", trends.getDurations());

        List<TrendTableRow> ttr = trends.collectTrendFeatureScenario();


        context.put("featuresDetail", trends.collectTrendFeatureScenario());


        /*String[][] featuresDetail = trends.getFeaturesDetail();

        int size = featuresDetail.length;


        for (int i=0; i < size ; i++){
            for (int j = 0; j < featuresDetail[i].length; j++){

            }
        }*/
    }

}
