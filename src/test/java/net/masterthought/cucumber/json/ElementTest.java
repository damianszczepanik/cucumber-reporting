package net.masterthought.cucumber.json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.FileReaderUtil;
import net.masterthought.cucumber.json.support.Status;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;

@RunWith(Parameterized.class)
public class ElementTest {
    // a simple element json that we'll tweak for our tests
    private static final String TEMPLATE_JSON = "json/element/template.json";

    // the status we're testing for this run
    private final Status step_status;

    @Parameterized.Parameters
    public static Collection<Status[]> data() {
        Collection<Status[]> params = new ArrayList<>();
        params.add(new Status[] {Status.PASSED});
        params.add(new Status[] {Status.FAILED});
        params.add(new Status[] {Status.SKIPPED});
        params.add(new Status[] {Status.MISSING});
        params.add(new Status[] {Status.PENDING});
        params.add(new Status[] {Status.UNDEFINED});
        return params;
    }

    public ElementTest(Status step_status) {
        this.step_status = step_status;
    }

    // reads in the template json file to an element
    // sets the status of the middle step in the json to the given status
    private static Element read_json_set_step(Status status) throws FileNotFoundException {
        String path = FileReaderUtil.getAbsolutePathFromResource(TEMPLATE_JSON);
        FileReader f = new FileReader(path);
        Gson gson = new Gson();

        // read in as json and tweak the last step's status
        JsonObject json = gson.fromJson(f, JsonElement.class).getAsJsonObject();
        JsonArray steps = json.getAsJsonArray("steps");

        // change the middle step
        JsonObject mid_step = steps.get(1).getAsJsonObject().getAsJsonObject("result");
        mid_step.remove("status");
        mid_step.addProperty("status", status.getRawName());

        // setup the last step based on the middle step
        // if passed, then the rest should be rest for the purpose of our test
        // else, the last step should be skipped
        JsonObject last_step = steps.get(2).getAsJsonObject().getAsJsonObject("result");
        last_step.remove("status");
        last_step.addProperty("status", (status == Status.PASSED ? Status.PASSED : Status.SKIPPED).getRawName());

        // now read it back into gson to parse as an element
        return gson.fromJson(json, Element.class);
    }

    // given a test result,
    // the steps group status should rollup the status of the first non-passing step
    @Test
    public void steps_status_no_fail_flags() throws Throwable {
        Element el = read_json_set_step(step_status);

        // setup default config, no fail flags
        el.setMedaData(null, new Configuration(new File(""), ""));

        Assert.assertEquals(step_status, el.getStepsStatus());
    }

    // given a config with fail flags set for the status we're testing,
    // an element's status should be passed if all steps passed
    // or failed if any step's status matches the fail flag
    @Test
    public void element_status_fail_flags() throws Throwable {
        Element el = read_json_set_step(step_status);

        // setup config to have fail flags for the status we're testing
        Configuration cfg = new Configuration(new File(""), "");
        cfg.setStatusFlags(
            step_status == Status.SKIPPED,
            step_status == Status.PENDING,
            step_status == Status.UNDEFINED,
            step_status == Status.MISSING
        );
        el.setMedaData(null, cfg);

        // if fail flags are set, any non-passing status will be fails
        Status expect = step_status == Status.PASSED ? Status.PASSED : Status.FAILED;

        Assert.assertEquals(expect, el.getElementStatus());
    }

    // given a default config (no fail flags),
    // an element's status should rollup the status of the first non-passing step
    @Test
    public void element_status_no_fail_flags() throws Throwable {
        Element el = read_json_set_step(step_status);

        // setup default config, no fail flags
        Configuration cfg = new Configuration(new File(""), "");
        el.setMedaData(null, cfg);

        Assert.assertEquals(step_status, el.getElementStatus());
    }

}
