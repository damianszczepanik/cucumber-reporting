package net.masterthought.cucumber.generators;

import org.apache.velocity.VelocityContext;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportResult;

/**
 * Delivers common methods for page generation.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public abstract class AbstractPage {

    /** Name of the HTML file which will be generated. */
    private final String templateFileName;
    
    protected AbstractPage(String templateFileName) {
        this.templateFileName = templateFileName;
    }
    
    /**
     * Returns HTML file name (with extension) for this report.
     *
     * @return HTML file for the report
     */
    public String getTemplateName() {
        return "templates/generators/" + templateFileName;
    }

    /**
     * Returns HTML file name (with extension) for this report.
     *
     * @return HTML file for the report
     */
    public abstract String getWebPage();
    
    /**
     * Populates the given context to merge it with the template
     * 
     * @param context
     */
    public abstract void preparePageContext(VelocityContext context, Configuration configuration, ReportResult reportResult);

}
