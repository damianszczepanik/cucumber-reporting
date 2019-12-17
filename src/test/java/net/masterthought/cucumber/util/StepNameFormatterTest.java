package net.masterthought.cucumber.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import net.masterthought.cucumber.generators.integrations.PageTest;
import net.masterthought.cucumber.json.Step;

public class StepNameFormatterTest extends PageTest {

    @Before
    public void setUp() {
        setUpWithJson(SAMPLE_JSON);
    }

    @Test
    public void format_OnNoArgument_ReturnsUnchangedValue() {

        // given
        Step step = features.get(0).getElements()[1].getSteps()[1];

        // when
        String formatted = StepNameFormatter.format(step.getName(), step.getMatch().getArguments(), "<arg>", "</arg>");

        // then
        assertThat(formatted).isEqualTo(step.getName());
    }

    @Test
    public void format_OnLastPosition_ReturnsFormattedValue() {

        // given
        Step step = features.get(0).getElements()[1].getSteps()[0];

        // when
        String formatted = StepNameFormatter.format(step.getName(), step.getMatch().getArguments(), "<arg>", "</arg>");

        // then
        assertThat(formatted).isEqualTo("the account balance is <arg>100</arg>");
    }

    @Test
    public void format_OnEmptyArgument_ReturnsFormattedValue() {

        // given
        Step step = features.get(0).getElements()[1].getSteps()[1];

        // when
        String formatted = StepNameFormatter.format(step.getName(), step.getMatch().getArguments(), "<arg>", "</arg>");

        // then
        assertThat(formatted).isEqualTo("the card is valid");
    }

    @Test
    public void format_OnArgumentAtEndOfString_ReturnsFormattedValue() {

        // given
        Step step = features.get(0).getElements()[1].getSteps()[3];

        // when
        String formatted = StepNameFormatter.format(step.getName(), step.getMatch().getArguments(), "*", "*");

        // then
        assertThat(formatted).isEqualTo("the Account Holder requests *10*, entering PIN *1234*");
    }

    @Test
    public void format_ReturnsFormattedValue() {

        // given
        Step step = features.get(0).getElements()[1].getSteps()[4];

        // when
        String formatted = StepNameFormatter.format(step.getName(), step.getMatch().getArguments(), "<arg>", "</arg>");

        // then
        assertThat(formatted).isEqualTo("the ATM should dispense <arg>10</arg> monetary units");
    }

    @Test
    public void format_StartPosition_ReturnsFormattedValue() {

        // given
        Step step = features.get(0).getElements()[1].getSteps()[2];

        // when
        String formatted = StepNameFormatter.format(step.getName(), step.getMatch().getArguments(), "<arg>", "</arg>");

        // then
        assertThat(formatted).isEqualTo("<arg>100</arg> is contained in the machine");
    }

    @Test
    public void format_OnMultipleArguments_ReturnsFormattedValue() {

        // given
        Step step = features.get(0).getElements()[1].getSteps()[3];

        // when
        String formatted = StepNameFormatter.format(step.getName(), step.getMatch().getArguments(), "<arg>", "</arg>");

        // then
        assertThat(formatted).isEqualTo("the Account Holder requests <arg>10</arg>, entering PIN <arg>1234</arg>");
    }

    @Test
    public void format_OnEmptyValue_ReturnsUnchangedValue() {

        // given
        Step step = features.get(0).getElements()[1].getSteps()[3];

        // when
        String formatted = StepNameFormatter.format(step.getName(), step.getMatch().getArguments(), "<arg>", "</arg>");

        // then
        assertThat(formatted).isEqualTo("the Account Holder requests <arg>10</arg>, entering PIN <arg>1234</arg>");
    }

    @Test
    public void format_optionalArgumentNotMatched() {

        // given
        Step step = features.get(1).getElements()[0].getSteps()[0];

        // when
        String formatted = StepNameFormatter.format(step.getName(), step.getMatch().getArguments(), "<arg>", "</arg>");

        // then
        assertThat(formatted).isEqualTo("the account balance is <arg>100</arg>");
    }

    @Test
    public void format_ReturnsEscapedValues() {

        // given
        Step step = features.get(1).getElements()[0].getSteps()[1];

        // when
        String formatted = StepNameFormatter.format(step.getName(), step.getMatch().getArguments(), "<arg>", "</arg>");

        // then
        assertThat(formatted).isEqualTo("the card is valid");
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
