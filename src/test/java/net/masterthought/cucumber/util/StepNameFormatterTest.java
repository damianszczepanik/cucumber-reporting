package net.masterthought.cucumber.util;

import static org.assertj.core.api.Assertions.assertThat;

import net.masterthought.cucumber.generators.integrations.PageTest;
import net.masterthought.cucumber.json.Step;
import org.junit.Before;
import org.junit.Test;

public class StepNameFormatterTest extends PageTest {
    @Before
    public void setUp() {
        setUpWithJson(SAMPLE_JSON);
    }

    @Test
    public void format_noArguments() {

        // given
        Step step = features.get(0).getElements()[1].getSteps()[1];

        // when
        String formatted = StepNameFormatter.format(step.getName(), step.getMatch().getArguments(), "<arg>", "</arg>");

        // then
        assertThat(formatted).isEqualTo("the card is valid");
    }

    @Test
    public void format_endsWithArguments() {

        // given
        Step step = features.get(0).getElements()[1].getSteps()[0];

        // when
        String formatted = StepNameFormatter.format(step.getName(), step.getMatch().getArguments(), "<arg>", "</arg>");

        // then
        assertThat(formatted).isEqualTo("the account balance is <arg>100</arg>");
    }

    @Test
    public void format_argumentInTheMiddle() {

        // given
        Step step = features.get(0).getElements()[1].getSteps()[4];

        // when
        String formatted = StepNameFormatter.format(step.getName(), step.getMatch().getArguments(), "<arg>", "</arg>");

        // then
        assertThat(formatted).isEqualTo("the ATM should dispense <arg>10</arg> monetary units");
    }

    @Test
    public void format_startsWithArgument() {

        // given
        Step step = features.get(0).getElements()[1].getSteps()[2];

        // when
        String formatted = StepNameFormatter.format(step.getName(), step.getMatch().getArguments(), "<arg>", "</arg>");

        // then
        assertThat(formatted).isEqualTo("<arg>100</arg> is contained in the machine");
    }

    @Test
    public void format_twoArguments() {

        // given
        Step step = features.get(0).getElements()[1].getSteps()[3];

        // when
        String formatted = StepNameFormatter.format(step.getName(), step.getMatch().getArguments(), "<arg>", "</arg>");

        // then
        assertThat(formatted).isEqualTo("the Account Holder requests <arg>10</arg>, entering PIN <arg>1234</arg>");
    }

    @Test
    public void format_shouldEscape() {

        // given
        String text = "I press <ON> & <C> simulatenously";

        // when
        String formatted = StepNameFormatter.format(text, null, "<arg>", "</arg>");

        // then
        assertThat(formatted).isEqualTo("I press &lt;ON&gt; &amp; &lt;C&gt; simulatenously");
    }
}
