package net.masterthought.cucumber;

/**
 * Basic data object containing counts of passed/failed/pending/undefined/total
 *
 * Can be used for steps, scenarios, features.
 *
 */
public class ReportCounts {
    public int passed;
    public int failed;
    public int pending;
    public int undefined;
    public int skipped;
    public int total;

    public ReportCounts(int passed, int failed, int pending, int undefined, int skipped, int total) {
        this.passed = passed;
        this.failed = failed;
        this.pending = pending;
        this.undefined = undefined;
        this.skipped = skipped;
        this.total = total;
    }
}
