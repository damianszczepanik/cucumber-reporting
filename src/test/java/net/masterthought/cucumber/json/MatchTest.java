package net.masterthought.cucumber.json;

import net.masterthought.cucumber.generators.integrations.PageTest;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Sean Bucholtz (sean.bucholtz@gmail.com)
 */
public class MatchTest extends PageTest{
    @Before
    public void setUp() {
        setUpWithJson(SAMPLE_JSON);
    }

    @Test
    public void getLocation_ReturnLocation() throws Exception {
        Match match = this.features.get(1).getElements()[0].getBefore()[0].getMatch();
        assertEquals("MachineFactory.findCachMachine()", match.getLocation());
    }

    @Test
    public void hashCode_ReturnHashCode() throws Exception {
        Match match = this.features.get(1).getElements()[0].getBefore()[0].getMatch();
        assertEquals(1151916150, match.hashCode());
    }

    @Test
    public void equals_ReturnTrueSameInstance() throws Exception {
        Match match = this.features.get(1).getElements()[0].getBefore()[0].getMatch();
        assertTrue(match.equals(match));
    }

    @Test
    public void equals_ReturnTrueSameValue() throws Exception {
        Match match1 = this.features.get(1).getElements()[0].getBefore()[0].getMatch();
        Match match2 = new Match();
        TestUtils.setFieldViaReflection("location", "MachineFactory.findCachMachine()", match2);
        assertTrue(match1.equals(match2));
    }

    @Test
    public void equals_ReturnFalseNotSameValue() throws Exception {
        Match match1 = this.features.get(1).getElements()[0].getBefore()[0].getMatch();
        Match match2 = new Match();
        TestUtils.setFieldViaReflection("location", "27°59'16.8\"N 86°55'30.0\"E", match2);
        assertFalse(match1.equals(match2));
    }

    @Test
    public void equals_ReturnFalseNotAnInstanceOf() throws Exception {
        Match match = this.features.get(1).getElements()[0].getBefore()[0].getMatch();
        assertFalse(match.equals(new Step()));
    }

}