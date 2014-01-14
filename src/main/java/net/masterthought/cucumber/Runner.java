package net.masterthought.cucumber;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Runner {

    public static void main(String[] args) throws Exception {
        File rd = new File("/var/www/cucumber/builds/97");
        List<String> list = new ArrayList<String>();
        //        list.add("/Users/kings/.jenkins/jobs/aaaaa/builds/15/cucumber-html-reports/french.json");
//            list.add("/Users/kings/.jenkins/jobs/aaaaa/builds/15/cucumber-html-reports/co_cucumber.json");
        //        list.add("/Users/kings/.jenkins/jobs/aaaaa/builds/15/cucumber-html-reports/ccp_cucumber.json");
        //        list.add("/Users/kings/.jenkins/jobs/aaaaa/builds/15/cucumber-html-reports/ss_cucumber.json");
        //        list.add("/Users/kings/.jenkins/jobs/cucumber-jvm/builds/7/cucumber-html-reports/cukes.json");
//            list.add("/Users/kings/.jenkins/jobs/cucumber-jvm/builds/7/cucumber-html-reports/project1.json");
//            list.add("/Users/kings/.jenkins/jobs/cucumber-jvm/builds/7/cucumber-html-reports/project2.json");
//            list.add("/Users/kings/.jenkins/jobs/cucumber-jvm/builds/7/cucumber-html-reports/project2.json");
//            list.add("/Users/kings/.jenkins/jobs/cucumber-jvm/builds/7/cucumber-html-reports/project2.json");
        // list.add("/Users/kings/dl/integration_cucumber.json");
        list.add("/opt/polochews/automation.json");
//        list.add("/Users/kings/development/projects/cucumber-reporting/src/test/resources/net/masterthought/cucumber/project2.json");
//        list.add("/Users/kings/development/projects/cucumber-reporting/src/test/resources/net/masterthought/cucumber/tags.json");
//        list.add("/Users/kings/development/projects/cucumber-reporting/src/test/resources/net/masterthought/cucumber/project1.json");
//        list.add("/Users/kings/development/projects/cucumber-reporting/src/test/resources/net/masterthought/cucumber/chinese.json");

        ReportBuilder reportBuilder = new ReportBuilder(list, rd, "", "97", "cucumber-jvm", false, false, true, true, false, "Account has sufficient funds again~the account balance is 300~account~scenario1_psp_auth_request.xml~xml\nAccount has sufficient funds again~the card is valid~card~scenario1_psp_auth_response.xml~xml", false);
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("@TC-V2-1491", "Verify that a visit for none suggestion account is created successfully");
        headers.put("@TC-V2-1275", "Verify that agenda items can be deleted successfully");
        headers.put("@TC-V2-1487", "Verify when the user tap on Accept button after edit an item, " +
                "the item loose the highlighting");
        headers.put("@TC-V2-1340", "Verify that user is able to login to the " +
                "application using Go button from the iPad keyboard");
        headers.put("@TC-V2-1643", "Verify if the visit is created " +
                "when user clicks on Accept button on current day");
        headers.put("@TC-V2-1309", "Verify if user can set Start and " +
                "end time from the suggestion's popup");
        headers.put("@TC-V2-1497", "Verify the suggestions are sent from today + " +
                "21 days using the default rolling period on XML file");
        headers.put("@TC-V2-1244", "Verify that arrows let the " +
                "user navigate to next/previous week");

        reportBuilder.setCustomHeader(headers);
        reportBuilder.generateReports();
        //       boolean result = featureReportGenerator.getBuildStatus();
        //       System.out.println("status: " + result);

    }

}
