package net.masterthought.cucumber.generators.integrations.helpers;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class TableAssertion extends ReportAssertion {

    public TableRowAssertion[] getHeaderRows() {
        return allBySelector("thead tr", TableRowAssertion.class);
    }

    public TableRowAssertion getHeaderRow() {
        return oneBySelector("thead tr", TableRowAssertion.class);
    }

    public TableRowAssertion[] getBodyRows() {
        return allBySelector("tbody tr", TableRowAssertion.class);
    }

    public TableRowAssertion getBodyRow() {
        return oneBySelector("tbody tr", TableRowAssertion.class);
    }

    public TableRowAssertion getFooterRow() {
        return oneBySelector("tfoot tr", TableRowAssertion.class);
    }

    public TableRowAssertion[] getAllFooterRows() {
        return allBySelector("tfoot tr", TableRowAssertion.class);
    }
}
