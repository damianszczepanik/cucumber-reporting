package net.masterthought.cucumber;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by suci on 15/12/2017.
 */
public class FeatureScenarioTest {
    FeatureScenario featureScenario;

    @Before
    public void setUp() throws Exception{
        //Given
        featureScenario = new FeatureScenario("device 1", "feature 1", "scenario 1", "PASSED", "3");
    }

    @Test
    public void getFeatureName() throws Exception {
        assertEquals("feature 1", featureScenario.getFeatureName());
    }

    @Test
    public void getScenarioName() throws Exception {
        assertEquals("scenario 1", featureScenario.getScenarioName());
    }

    @Test
    public void getStatus() throws Exception {
        assertEquals("PASSED", featureScenario.getStatus());
    }

    @Test
    public void getDeviceName() throws Exception {
        assertEquals("device 1", featureScenario.getDeviceName());
    }

    @Test
    public void getId() throws Exception {
        String Id = "device 1;feature 1;scenario 1;3";

        assertEquals(Id, featureScenario.getId());
    }

}