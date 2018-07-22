package net.masterthought.cucumber.json.support.comparators;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.json.support.StatusCounter;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class StatusCounterTest {

    @Test
    public void getValueFor_ReturnsStatusCounter() {

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
    public void size_ReturnsAllStatusCounter() {

        // given
        StatusCounter statusCounter = new StatusCounter();

        // when
        statusCounter.incrementFor(Status.PASSED);
        statusCounter.incrementFor(Status.FAILED);

        // then
        assertThat(statusCounter.size()).isEqualTo(2);
    }

    @Test
    public void getFinalStatus_WithNoStatuses_ReturnsPass() {

        // given
        StatusCounter statusCounter = new StatusCounter();

        // when
        Status status = statusCounter.getFinalStatus();

        // then
        assertThat(status).isEqualTo(Status.PASSED);
    }

    @Test
    public void getFinalStatus_OnSameStatuses_ReturnsThatStatus() {

        // given
        StatusCounter statusCounter = new StatusCounter();

        // when
        statusCounter.incrementFor(Status.PASSED);
        statusCounter.incrementFor(Status.PASSED);

        // then
        assertThat(statusCounter.getFinalStatus()).isEqualTo(Status.PASSED);
    }

    @Test
    public void getFinalStatus_OnDifferentStatuses_ReturnsFailedStatus() {

        // given
        StatusCounter statusCounter = new StatusCounter();

        // when
        statusCounter.incrementFor(Status.PASSED);
        statusCounter.incrementFor(Status.UNDEFINED);

        // then
        assertThat(statusCounter.getFinalStatus()).isEqualTo(Status.FAILED);
    }

    @Test
    public void getFinalStatus_OnFailedStatus_ReturnsFailedStatus() {

        // given
        StatusCounter statusCounter = new StatusCounter();

        // when
        statusCounter.incrementFor(Status.PASSED);
        statusCounter.incrementFor(Status.FAILED);

        // then
        assertThat(statusCounter.getFinalStatus()).isEqualTo(Status.FAILED);
    }
}
