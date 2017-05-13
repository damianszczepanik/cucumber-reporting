package net.masterthought.cucumber;

import net.masterthought.cucumber.json.Feature;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static net.masterthought.cucumber.FileReaderUtil.getAbsolutePathFromResource;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

public class LocationTest {

    private final Configuration configuration = new Configuration(new File(""), "testProject");


    @Test
    public void shouldHandleBrokenLocation() throws IOException {

        List<String> jsonReports = new ArrayList<>();
        jsonReports.add(getAbsolutePathFromResource("net/masterthought/cucumber/location_formats.json"));

        ReportParser reportParser = new ReportParser(configuration);
        List<Feature> features = reportParser.parseJsonResults(jsonReports);

        String location = features.get(0).getElements()[0].getSteps()[0].getMatch().getLocation();
        assertThat(location,is("features/step_definitions/selenium/selenium.rb:5"));

        String locationWithRange = features.get(0).getElements()[0].getAfter()[0].getMatch().getLocation();
        assertThat(locationWithRange,is("json_spec-1.1.4/lib/json_spec/cucumber.rb:5..7"));

        String locationNonConsecutive = features.get(0).getElements()[0].getSteps()[1].getMatch().getLocation();
        assertThat(locationNonConsecutive,is("features/step_definitions/selenium/selenium.rb:5,7,8"));

        String missingLocation = features.get(0).getElements()[0].getSteps()[2].getMatch().getLocation();
        assertThat(missingLocation,is(nullValue()));

        String correctLocation = features.get(0).getElements()[0].getSteps()[3].getMatch().getLocation();
        assertThat(correctLocation, is("cucumber.rb:6"));

        String anotherLocationType = features.get(0).getElements()[0].getAfter()[1].getMatch().getLocation();
        assertThat(anotherLocationType, is("ruby/core/support/hooks.rb:19"));

        String invalidFormat1 = features.get(0).getElements()[0].getAfter()[2].getMatch().getLocation();
        assertThat(invalidFormat1,is(nullValue()));

        String invalidFormat2 = features.get(0).getElements()[0].getAfter()[3].getMatch().getLocation();
        assertThat(invalidFormat2,is(nullValue()));

        String invalidFormat3 = features.get(0).getElements()[0].getAfter()[4].getMatch().getLocation();
        assertThat(invalidFormat3,is(nullValue()));

        String invalidFormat4 = features.get(0).getElements()[0].getAfter()[5].getMatch().getLocation();
        assertThat(invalidFormat4,is(nullValue()));

        String invalidFormat5 = features.get(0).getElements()[0].getAfter()[6].getMatch().getLocation();
        assertThat(invalidFormat5,is(nullValue()));
    }
}