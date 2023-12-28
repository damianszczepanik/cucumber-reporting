package net.masterthought.cucumber.json;

import static org.assertj.core.api.Assertions.assertThat;

import net.masterthought.cucumber.json.support.Argument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import net.masterthought.cucumber.generators.integrations.PageTest;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
class MatchTest extends PageTest {

    @BeforeEach
    void setUp() {
        setUpWithJson(SAMPLE_JSON);
    }

    @Test
    void getLocation_ReturnsLocation() {

        // given
        Match match = features.get(0).getElements()[1].getSteps()[0].getMatch();

        // when
        String location = match.getLocation();

        // then
        assertThat(location).isEqualTo("ATMScenario.createAccount(int)");
    }

    @Test
    void getArguments_ReturnsArguments() {

        // given
        Match match = features.get(0).getElements()[1].getSteps()[0].getMatch();

        // when
        Argument[] arguments = match.getArguments();

        // then
        assertThat(arguments).hasSize(1);
    }
}
