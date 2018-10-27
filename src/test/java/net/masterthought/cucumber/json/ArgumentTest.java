package net.masterthought.cucumber.json;

import static org.assertj.core.api.Assertions.assertThat;

import mockit.Deencapsulation;
import org.junit.Before;
import org.junit.Test;

import net.masterthought.cucumber.generators.integrations.PageTest;
import net.masterthought.cucumber.json.support.Argument;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class ArgumentTest extends PageTest {

    @Before
    public void setUp() {
        setUpWithJson(SAMPLE_JSON);
    }

    @Test
    public void getRows_ReturnsRows() {

        // given
        Step step = features.get(0).getElements()[1].getSteps()[5];
        Argument[] arguments = Deencapsulation.getField(step, "arguments");

        // when
        Row[] rows = arguments[0].getRows();

        // then
        assertThat(rows).hasSize(2);
        assertThat(rows[0].getCells()).containsOnlyOnce("max", "min");
    }
    @Test
    public void getVal_ReturnsVal() {

        // given
        Argument matchArgument = features.get(0).getElements()[1].getSteps()[0].getMatch().getArguments()[0];

        // when
        String val = matchArgument.getVal();

        // then
        assertThat(val).isEqualTo("100");
    }

    @Test
    public void getArguments_ReturnsArguments() {

        // given
        Argument matchArgument = features.get(0).getElements()[1].getSteps()[0].getMatch().getArguments()[0];

        // when
        int offset = matchArgument.getOffset();

        // then
        assertThat(offset).isEqualTo(23);
    }
}