package net.masterthought.cucumber.json.support.comparators;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;

import net.masterthought.cucumber.json.support.Resultsable;
import net.masterthought.cucumber.json.support.ResultsableBuilder;
import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.json.support.StatusCounter;
import org.junit.jupiter.api.Test;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
class StatusCounterTest {

    @Test
    void StatusCounter_OnFailingStatuses_IncrementsPassing() {

        // given
        Resultsable[] resultsables = ResultsableBuilder.Resultsable(Status.PASSED, Status.FAILED, Status.SKIPPED);

        // when
        StatusCounter statusCounter = new StatusCounter(resultsables, Collections.singleton(Status.SKIPPED));

        // then
        assertThat(statusCounter.getValueFor(Status.PASSED)).isEqualTo(2);
        assertThat(statusCounter.getValueFor(Status.FAILED)).isOne();
        assertThat(statusCounter.getValueFor(Status.SKIPPED)).isZero();
    }

    @Test
    void StatusCounter_OnNullFailingStatuses_IncrementsPassing() {

        // given
        Resultsable[] resultsables = ResultsableBuilder.Resultsable(Status.PASSED, Status.FAILED, Status.SKIPPED);

        // when
        StatusCounter statusCounter = new StatusCounter(resultsables, null);

        // then
        assertThat(statusCounter.getValueFor(Status.PASSED)).isOne();
        assertThat(statusCounter.getValueFor(Status.FAILED)).isOne();
        assertThat(statusCounter.getValueFor(Status.SKIPPED)).isOne();
    }

    @Test
    void StatusCounter_OnEmptyFailingStatuses_IncrementsPassing() {

        // given
        Resultsable[] resultsables = ResultsableBuilder.Resultsable(Status.PASSED, Status.FAILED, Status.SKIPPED);

        // when
        StatusCounter statusCounter = new StatusCounter(resultsables);

        // then
        assertThat(statusCounter.getValueFor(Status.PASSED)).isOne();
        assertThat(statusCounter.getValueFor(Status.FAILED)).isOne();
        assertThat(statusCounter.getValueFor(Status.SKIPPED)).isOne();
    }

    @Test
    void getValueFor_ReturnsStatusCounter() {

        // given
        StatusCounter statusCounter = new StatusCounter();

        // when
        statusCounter.incrementFor(Status.PASSED);
        statusCounter.incrementFor(Status.PASSED);
        statusCounter.incrementFor(Status.PASSED);

        // then
        assertThat(statusCounter.getValueFor(Status.PASSED)).isEqualTo(3);
        assertThat(statusCounter.getValueFor(Status.FAILED)).isZero();
        assertThat(statusCounter.getValueFor(Status.UNDEFINED)).isZero();
        assertThat(statusCounter.getValueFor(Status.PENDING)).isZero();
        assertThat(statusCounter.getValueFor(Status.SKIPPED)).isZero();
    }

    @Test
    void size_ReturnsAllStatusCounter() {

        // given
        StatusCounter statusCounter = new StatusCounter();

        // when
        statusCounter.incrementFor(Status.PASSED);
        statusCounter.incrementFor(Status.FAILED);

        // then
        assertThat(statusCounter.size()).isEqualTo(2);
    }

    @Test
    void getFinalStatus_WithNoStatuses_ReturnsPass() {

        // given
        StatusCounter statusCounter = new StatusCounter();

        // when
        Status status = statusCounter.getFinalStatus();

        // then
        assertThat(status).isEqualTo(Status.PASSED);
    }

    @Test
    void getFinalStatus_OnSameStatuses_ReturnsThatStatus() {

        // given
        StatusCounter statusCounter = new StatusCounter();

        // when
        statusCounter.incrementFor(Status.PASSED);
        statusCounter.incrementFor(Status.PASSED);

        // then
        assertThat(statusCounter.getFinalStatus()).isEqualTo(Status.PASSED);
    }

    @Test
    void getFinalStatus_OnDifferentStatuses_ReturnsFailedStatus() {

        // given
        StatusCounter statusCounter = new StatusCounter();

        // when
        statusCounter.incrementFor(Status.PASSED);
        statusCounter.incrementFor(Status.UNDEFINED);

        // then
        assertThat(statusCounter.getFinalStatus()).isEqualTo(Status.FAILED);
    }

    @Test
    void getFinalStatus_OnFailedStatus_ReturnsFailedStatus() {

        // given
        StatusCounter statusCounter = new StatusCounter();

        // when
        statusCounter.incrementFor(Status.PASSED);
        statusCounter.incrementFor(Status.FAILED);

        // then
        assertThat(statusCounter.getFinalStatus()).isEqualTo(Status.FAILED);
    }
}
