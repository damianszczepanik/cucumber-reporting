package net.masterthought.cucumber.json;

import static org.assertj.core.api.Assertions.assertThat;

import net.masterthought.cucumber.json.support.Argument;
import org.junit.Before;
import org.junit.Test;

import net.masterthought.cucumber.generators.integrations.PageTest;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class MatchTest extends PageTest {

    @Before
    public void setUp() {
        setUpWithJson(SAMPLE_JSON);
    }

    @Test
    public void getLocation_ReturnsLocation() {

        // given
        Match match = features.get(0).getElements()[1].getSteps()[0].getMatch();

        // when
        String location = match.getLocation();

        // then
        assertThat(location).isEqualTo("ATMScenario.createAccount(int)");
    }

    @Test
    public void getArguments_ReturnsArguments() {

        // given
        Match match = features.get(0).getElements()[1].getSteps()[0].getMatch();

        // when
        Argument[] arguments = match.getArguments();

        // then
        assertThat(arguments).hasSize(1);
    }
}
