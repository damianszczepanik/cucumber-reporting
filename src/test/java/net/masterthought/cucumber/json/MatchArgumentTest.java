package net.masterthought.cucumber.json;

import static org.assertj.core.api.Assertions.assertThat;

import net.masterthought.cucumber.generators.integrations.PageTest;
import org.junit.Before;
import org.junit.Test;

public class MatchArgumentTest extends PageTest {

    @Before
    public void setUp() {
        setUpWithJson(SAMPLE_JSON);
    }

    @Test
    public void getVal_ReturnsVal() {

        // given
        MatchArgument matchArgument = features.get(0).getElements()[1].getSteps()[0].getMatch().getArguments()[0];

        // when
        String val = matchArgument.getVal();

        // then
        assertThat(val).isEqualTo("100");
    }

    @Test
    public void getArguments_ReturnsArguments() {

        // given
        MatchArgument matchArgument = features.get(0).getElements()[1].getSteps()[0].getMatch().getArguments()[0];

        // when
        int offset = matchArgument.getOffset();

        // then
        assertThat(offset).isEqualTo(23);
    }
}
