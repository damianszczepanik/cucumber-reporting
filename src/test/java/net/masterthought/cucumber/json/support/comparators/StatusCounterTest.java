package net.masterthought.cucumber.json.support.comparators;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import net.masterthought.cucumber.json.support.Resultsable;
import net.masterthought.cucumber.json.support.ResultsableBuilder;
import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.json.support.StatusCounter;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 * @author <a href="https://github.com/ghostcity">Stefan Gasterstädt</a>
 */
@RunWith(Parameterized.class)
public class StatusCounterTest {

    @Parameters(name = "{5} by {6}")
    public static Iterable<Object[]> data() {
        return asList(new Object[][] {
            { 0, 0, 0, 0, 0, Status.PASSED, asList() },
            { 1, 0, 0, 0, 0, Status.PASSED, asList(Status.PASSED) },
            { 0, 1, 0, 0, 0, Status.FAILED, asList(Status.FAILED) },
            { 0, 0, 1, 0, 0, Status.FAILED, asList(Status.SKIPPED) },
            { 0, 0, 0, 1, 0, Status.FAILED, asList(Status.PENDING) },
            { 0, 0, 0, 0, 1, Status.FAILED, asList(Status.UNDEFINED) },
            { 2, 0, 0, 0, 0, Status.PASSED, asList(Status.PASSED, Status.PASSED) },
            { 1, 1, 0, 0, 0, Status.FAILED, asList(Status.PASSED, Status.FAILED) },
            { 1, 0, 1, 0, 0, Status.FAILED, asList(Status.PASSED, Status.SKIPPED) },
            { 1, 0, 0, 1, 0, Status.FAILED, asList(Status.PASSED, Status.PENDING) },
            { 1, 0, 0, 0, 1, Status.FAILED, asList(Status.PASSED, Status.UNDEFINED) },
            { 3, 0, 0, 0, 0, Status.PASSED, asList(Status.PASSED, Status.PASSED, Status.PASSED) },
            { 2, 1, 0, 0, 0, Status.FAILED, asList(Status.PASSED, Status.FAILED, Status.PASSED) },
            { 2, 0, 1, 0, 0, Status.FAILED, asList(Status.PASSED, Status.SKIPPED, Status.PASSED) },
            { 2, 0, 0, 1, 0, Status.FAILED, asList(Status.PASSED, Status.PENDING, Status.PASSED) },
            { 2, 0, 0, 0, 1, Status.FAILED, asList(Status.PASSED, Status.UNDEFINED, Status.PASSED) },
            { 1, 2, 0, 0, 0, Status.FAILED, asList(Status.PASSED, Status.FAILED, Status.FAILED) },
            { 1, 1, 1, 0, 0, Status.FAILED, asList(Status.PASSED, Status.FAILED, Status.SKIPPED) },
            { 1, 1, 0, 1, 0, Status.FAILED, asList(Status.PASSED, Status.FAILED, Status.PENDING) },
            { 1, 1, 0, 0, 1, Status.FAILED, asList(Status.PASSED, Status.FAILED, Status.UNDEFINED) },
        });
    }

    @Parameter(0)
    public int passedCounter;

    @Parameter(1)
    public int failedCounter;

    @Parameter(2)
    public int skippedCounter;

    @Parameter(3)
    public int pendingCounter;

    @Parameter(4)
    public int undefinedCounter;

    @Parameter(5)
    public Status finalStatus;

    @Parameter(6)
    public List<Status> statuses;

    @Test
    public void StatusCounter_OnNoneNotFailingStatuses() {
        // given
        Resultsable[] resultsables = ResultsableBuilder.Resultsable(this.statuses.toArray(new Status[0]));

        // when
        StatusCounter statusCounter = new StatusCounter(resultsables);

        // then
        assertThat(statusCounter.size()).isEqualTo(this.passedCounter + this.failedCounter + this.skippedCounter + this.pendingCounter + this.undefinedCounter);
        assertThat(statusCounter.getValueFor(Status.PASSED)).isEqualTo(this.passedCounter);
        assertThat(statusCounter.getValueFor(Status.FAILED)).isEqualTo(this.failedCounter);
        assertThat(statusCounter.getValueFor(Status.SKIPPED)).isEqualTo(this.skippedCounter);
        assertThat(statusCounter.getValueFor(Status.PENDING)).isEqualTo(this.pendingCounter);
        assertThat(statusCounter.getValueFor(Status.UNDEFINED)).isEqualTo(this.undefinedCounter);
        assertThat(statusCounter.getFinalStatus()).isEqualTo(this.finalStatus);
    }

    @Test
    public void StatusCounter_OnNullNotFailingStatuses() {
        // given
        Resultsable[] resultsables = ResultsableBuilder.Resultsable(this.statuses.toArray(new Status[0]));

        // when
        StatusCounter statusCounter = new StatusCounter(resultsables, null);

        // then
        assertThat(statusCounter.size()).isEqualTo(this.passedCounter + this.failedCounter + this.skippedCounter + this.pendingCounter + this.undefinedCounter);
        assertThat(statusCounter.getValueFor(Status.PASSED)).isEqualTo(this.passedCounter);
        assertThat(statusCounter.getValueFor(Status.FAILED)).isEqualTo(this.failedCounter);
        assertThat(statusCounter.getValueFor(Status.SKIPPED)).isEqualTo(this.skippedCounter);
        assertThat(statusCounter.getValueFor(Status.PENDING)).isEqualTo(this.pendingCounter);
        assertThat(statusCounter.getValueFor(Status.UNDEFINED)).isEqualTo(this.undefinedCounter);
        assertThat(statusCounter.getFinalStatus()).isEqualTo(this.finalStatus);
    }

    @Test
    public void StatusCounter_OnEmptyNotFailingStatuses() {
        // given
        Resultsable[] resultsables = ResultsableBuilder.Resultsable(this.statuses.toArray(new Status[0]));

        // when
        StatusCounter statusCounter = new StatusCounter(resultsables, emptySet());

        // then
        assertThat(statusCounter.size()).isEqualTo(this.passedCounter + this.failedCounter + this.skippedCounter + this.pendingCounter + this.undefinedCounter);
        assertThat(statusCounter.getValueFor(Status.PASSED)).isEqualTo(this.passedCounter);
        assertThat(statusCounter.getValueFor(Status.FAILED)).isEqualTo(this.failedCounter);
        assertThat(statusCounter.getValueFor(Status.SKIPPED)).isEqualTo(this.skippedCounter);
        assertThat(statusCounter.getValueFor(Status.PENDING)).isEqualTo(this.pendingCounter);
        assertThat(statusCounter.getValueFor(Status.UNDEFINED)).isEqualTo(this.undefinedCounter);
        assertThat(statusCounter.getFinalStatus()).isEqualTo(this.finalStatus);
    }

    @Test
    public void StatusCounter_OnSkippedNotFailingStatuses() {
        // given
        Resultsable[] resultsables = ResultsableBuilder.Resultsable(this.statuses.toArray(new Status[0]));
        Set<Status> notFailingStatuses = singleton(Status.SKIPPED);

        // when
        StatusCounter statusCounter = new StatusCounter(resultsables, notFailingStatuses);

        // then
        assertThat(statusCounter.size()).isEqualTo(this.passedCounter + this.failedCounter + this.skippedCounter + this.pendingCounter + this.undefinedCounter);
        assertThat(statusCounter.getValueFor(Status.PASSED)).isEqualTo(this.passedCounter + this.skippedCounter);
        assertThat(statusCounter.getValueFor(Status.FAILED)).isEqualTo(this.failedCounter);
        assertThat(statusCounter.getValueFor(Status.SKIPPED)).isZero();
        assertThat(statusCounter.getValueFor(Status.PENDING)).isEqualTo(this.pendingCounter);
        assertThat(statusCounter.getValueFor(Status.UNDEFINED)).isEqualTo(this.undefinedCounter);
    }

    @Test
    public void StatusCounter_OnNonPassedNotFailingStatuses() {
        // given
        Resultsable[] resultsables = ResultsableBuilder.Resultsable(this.statuses.toArray(new Status[0]));
        Set<Status> notFailingStatuses = Stream.of(Status.FAILED, Status.SKIPPED, Status.PENDING, Status.UNDEFINED).collect(toSet());

        // when
        StatusCounter statusCounter = new StatusCounter(resultsables, notFailingStatuses);

        // then
        assertThat(statusCounter.size()).isEqualTo(this.passedCounter + this.failedCounter + this.skippedCounter + this.pendingCounter + this.undefinedCounter);
        assertThat(statusCounter.getValueFor(Status.PASSED)).isEqualTo(this.passedCounter + this.failedCounter + this.skippedCounter + this.pendingCounter + this.undefinedCounter);
        assertThat(statusCounter.getValueFor(Status.FAILED)).isZero();
        assertThat(statusCounter.getValueFor(Status.SKIPPED)).isZero();
        assertThat(statusCounter.getValueFor(Status.PENDING)).isZero();
        assertThat(statusCounter.getValueFor(Status.UNDEFINED)).isZero();
        assertThat(statusCounter.getFinalStatus()).isEqualTo(Status.PASSED);
    }

    @Test
    public void StatusCounter_OnAllNotFailingStatuses() {
        // given
        Resultsable[] resultsables = ResultsableBuilder.Resultsable(this.statuses.toArray(new Status[0]));
        Set<Status> notFailingStatuses = stream(Status.values()).collect(toSet());

        // when
        StatusCounter statusCounter = new StatusCounter(resultsables, notFailingStatuses);

        // then
        assertThat(statusCounter.size()).isEqualTo(this.passedCounter + this.failedCounter + this.skippedCounter + this.pendingCounter + this.undefinedCounter);
        assertThat(statusCounter.getValueFor(Status.PASSED)).isEqualTo(this.passedCounter + this.failedCounter + this.skippedCounter + this.pendingCounter + this.undefinedCounter);
        assertThat(statusCounter.getValueFor(Status.FAILED)).isZero();
        assertThat(statusCounter.getValueFor(Status.SKIPPED)).isZero();
        assertThat(statusCounter.getValueFor(Status.PENDING)).isZero();
        assertThat(statusCounter.getValueFor(Status.UNDEFINED)).isZero();
        assertThat(statusCounter.getFinalStatus()).isEqualTo(Status.PASSED);
    }

    @Test
    public void StatusCounter_OnIncrementFor() {
        // given
        StatusCounter statusCounter = new StatusCounter();

        // when
        for (Status status : this.statuses) {
            statusCounter.incrementFor(status);
        }

        // then
        assertThat(statusCounter.size()).isEqualTo(this.passedCounter + this.failedCounter + this.skippedCounter + this.pendingCounter + this.undefinedCounter);
        assertThat(statusCounter.getValueFor(Status.PASSED)).isEqualTo(this.passedCounter);
        assertThat(statusCounter.getValueFor(Status.FAILED)).isEqualTo(this.failedCounter);
        assertThat(statusCounter.getValueFor(Status.SKIPPED)).isEqualTo(this.skippedCounter);
        assertThat(statusCounter.getValueFor(Status.PENDING)).isEqualTo(this.pendingCounter);
        assertThat(statusCounter.getValueFor(Status.UNDEFINED)).isEqualTo(this.undefinedCounter);
        assertThat(statusCounter.getFinalStatus()).isEqualTo(this.finalStatus);
   }

}
