package net.masterthought.cucumber.json;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import net.masterthought.cucumber.generators.Page;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class RowTest extends Page {

    @Before
    public void setUp() {
        setUpWithJson(SAMPLE_JOSN);
    }

    @Test
    public void getCellsReturnsCells() {

        // give
        Row[] rows = features.get(0).getElements()[0].getSteps()[1].getRows();

        // when
        String[] cells = rows[0].getCells();

        // then
        assertThat(rows).hasSize(5);
        assertThat(cells).containsExactly("Müller", "Deutschland");
    }

}