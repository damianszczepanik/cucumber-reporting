package net.masterthought.cucumber.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class CounterTest {

    @Test
    void next_shouldIncrement() {

        // given
        Counter counter = new Counter();
        int initValue = counter.intValue();

        // when
        int nextValue = counter.next();

        // then
        assertNotEquals(initValue, nextValue);
        assertEquals(initValue + 1, nextValue);
    }
}
