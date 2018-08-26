package net.masterthought.cucumber.json;

import static org.assertj.core.api.Assertions.assertThat;

import net.masterthought.cucumber.generators.integrations.PageTest;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class FeaturesTest extends PageTest {
    @Before
    public void setUp() throws Exception {
        setUpWithJson(SAMPLE_JSON);
    }

    @Test
    public void getListFeature_ReturnListFeature(){
        Features.setFeatures(features);
        List<Feature> features_actual = Features.getFeatures();

        assertEquals(2, features_actual.size());
        assertThat(features_actual).asList();
    }

}