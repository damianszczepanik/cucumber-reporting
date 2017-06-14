package net.masterthought.cucumber;

/**
 * Basic data object containing counts of passed/failed/pending/undefined/skipped/total
 *
 * Can be used for steps, scenarios, features.
 *
 * Uses public fields rather than getter/setter.
 *
 */
public class ReportCounts {
    public final int passed;
    public final int failed;
    public final int pending;
    public final int undefined;
    public final int skipped;
    public final int total;

    public ReportCounts(int passed, int failed, int pending, int undefined, int skipped, int total) {
        this.passed = passed;
        this.failed = failed;
        this.pending = pending;
        this.undefined = undefined;
        this.skipped = skipped;
        this.total = total;
    }

    public ReportCounts() {
        this.passed = 0;
        this.failed = 0;
        this.pending = 0;
        this.undefined = 0;
        this.skipped = 0;
        this.total = 0;

    }
}
