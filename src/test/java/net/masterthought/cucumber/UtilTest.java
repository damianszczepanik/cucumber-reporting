package net.masterthought.cucumber;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import net.masterthought.cucumber.util.Util;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class UtilTest {

    @Test
    public void formatAsPercentage_ReturnsFormatedValue() {

        // given
        final int[][] values = {{1, 3}, {2, 2}, {1, 5}, {0, 5}};
        String[] formatted = {"33.33%", "100.00%", "20.00%", "0.00%"};

        // then
        for (int i = 0; i < values.length; i++) {
            assertThat(Util.formatAsPercentage(values[i][0], values[i][1])).isEqualTo(formatted[i]);
        }
    }

    @Test
    public void formatAsPercentage_OnZeroTotal_ReturnsFormattedValue() {

        // given
        final int[] values = {1, 2, 0};

        // then
        for (int i = 0; i < values.length; i++) {
            assertThat(Util.formatAsPercentage(values[i], 0)).isEqualTo("0.00%");
        }
    }

    @Test
    public void formatAsDecimal_ReturnsFormattedValue() {

        // given
        final int[][] values = {{1, 3}, {2, 2}, {1, 5}, {0, 5}, {0, 0}};
        String[] formatted = {"33.33", "100.00", "20.00", "0.00", "0.00"};

        // then
        for (int i = 0; i < values.length; i++) {
            assertThat(Util.formatAsDecimal(values[i][0], values[i][1])).isEqualTo(formatted[i]);
        }
    }
}
