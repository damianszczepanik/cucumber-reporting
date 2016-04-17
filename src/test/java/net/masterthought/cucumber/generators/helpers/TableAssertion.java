package net.masterthought.cucumber.generators.helpers;

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
}
