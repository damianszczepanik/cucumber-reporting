package net.masterthought.cucumber;

public final class Configuration {

    private boolean failsIfSkipped;
    private boolean failsIFPending;
    private boolean failsIfUndefined;
    private boolean failsIfMissing;

    private boolean parallelTesting;

    public void setStatusFlags(boolean failsIfSkipped, boolean failsIFPending, boolean failsIfUndefined,
            boolean failsIfMissing) {
        this.failsIfSkipped = failsIfSkipped;
        this.failsIFPending = failsIFPending;
        this.failsIfUndefined = failsIfUndefined;
        this.failsIfMissing = failsIfMissing;
    }

    public boolean failsIfSkipped() {
        return failsIfSkipped;
    }

    public boolean failsIFPending() {
        return failsIFPending;
    }

    public boolean failsIfUndefined() {
        return failsIfUndefined;
    }

    public boolean failsIfMissing() {
        return failsIfMissing;
    }

    public boolean isParallelTesting() {
        return parallelTesting;
    }

    public void setParallelTesting(boolean parallelTesting) {
        this.parallelTesting = parallelTesting;
    }
}
