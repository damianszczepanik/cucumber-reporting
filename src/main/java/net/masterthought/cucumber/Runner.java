package net.masterthought.cucumber;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Runner {

    public static void main(String[] args) throws Exception {
        File rd = new File("/Users/kings/.jenkins/jobs/cucumber-jvm/builds/1/cucumber-html-reports");
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
        list.add("/Users/kings/development/projects/cucumber-reporting/src/test/resources/net/masterthought/cucumber/project1.json");
        list.add("/Users/kings/development/projects/cucumber-reporting/src/test/resources/net/masterthought/cucumber/project2.json");

        ReportBuilder reportBuilder = new ReportBuilder(list,rd,"","1","cucumber-jvm",false,false,true,true,true,"Account has sufficient funds again~the account balance is 300~account~scenario1_psp_auth_request.xml~xml\nAccount has sufficient funds again~the card is valid~card~scenario1_psp_auth_response.xml~xml");
        reportBuilder.generateReports();
        //       boolean result = featureReportGenerator.getBuildStatus();
        //       System.out.println("status: " + result);

    }

}
