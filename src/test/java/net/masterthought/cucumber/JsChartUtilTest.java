package net.masterthought.cucumber;

import static net.masterthought.cucumber.json.support.Status.FAILED;
import static net.masterthought.cucumber.json.support.Status.MISSING;
import static net.masterthought.cucumber.json.support.Status.PASSED;
import static net.masterthought.cucumber.json.support.Status.PENDING;
import static net.masterthought.cucumber.json.support.Status.SKIPPED;
import static net.masterthought.cucumber.json.support.Status.UNDEFINED;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import net.masterthought.cucumber.charts.JsChartUtil;
import net.masterthought.cucumber.json.support.ScenarioTag;
import net.masterthought.cucumber.json.support.TagObject;

/**
 * JsChartUtil Tester.
 * 
 * @author <Authors name>
 * @since <pre>
 * Jan 21, 2013
 * </pre>
 * @version 1.0
 */
public class JsChartUtilTest {

    private JsChartUtil util;

    @Before
    public void before() throws Exception {
        util = new JsChartUtil();
    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void testOrderStepsByValue_NoDupes_1() throws Exception {

        int totalPassed = 1;
        int totalFailed = 3;
        int totalSkipped = 5;
        int totalPending = 7;
        int totalUndefined = 0;
        int totalMissing = 0;

        Object[] result = util.orderStepsByValue(totalPassed, totalFailed, totalSkipped, totalPending, totalUndefined,
                totalMissing).toArray();
        Assert.assertArrayEquals("lists not ordered correctly",
                new String[] { PENDING.color, SKIPPED.color, FAILED.color, PASSED.color, UNDEFINED.color, MISSING.color },
                result);
    }

    @Test
    public void testOrderStepsByValue_NoDupes_2() throws Exception {

        int totalPassed = 7;
        int totalFailed = 3;
        int totalSkipped = 5;
        int totalPending = 1;
        int totalUndefined = 0;
        int totalMissing = 0;

        Object[] result = util.orderStepsByValue(totalPassed, totalFailed, totalSkipped, totalPending, totalUndefined,
                totalMissing).toArray();
        Assert.assertArrayEquals(
                "lists not ordered correctly",
                new String[] {PASSED.color, SKIPPED.color, FAILED.color, PENDING.color, UNDEFINED.color, MISSING.color},
                result);
    }

    @Test
    public void testOrderStepsByValue_TwoDupes_1() throws Exception {

        int totalPassed = 2;
        int totalFailed = 2;
        int totalSkipped = 1;
        int totalPending = 0;
        int totalUndefined = 0;
        int totalMissing = 0;

        Object[] result = util.orderStepsByValue(totalPassed, totalFailed, totalSkipped, totalPending, totalUndefined,
                totalMissing).toArray();
        Assert.assertArrayEquals(
                "lists not ordered correctly",
                new String[] {PASSED.color, FAILED.color, SKIPPED.color, PENDING.color, UNDEFINED.color, MISSING.color},
                result);
    }

    @Test
    public void testOrderStepsByValue_TwoDupes_2() throws Exception {

        int totalPassed = 0;
        int totalFailed = 3;
        int totalSkipped = 1;
        int totalPending = 0;
        int totalUndefined = 0;
        int totalMissing = 0;

        Object[] result = util.orderStepsByValue(totalPassed, totalFailed, totalSkipped, totalPending, totalUndefined,
                totalMissing).toArray();
        Assert.assertArrayEquals(
                "lists not ordered correctly",
                new String[] {FAILED.color, SKIPPED.color, PASSED.color, PENDING.color, UNDEFINED.color, MISSING.color},
                result);
    }

    @Test
    public void testOrderStepsByValue_TwoDupes_3() throws Exception {

        int totalPassed = 1;
        int totalFailed = 0;
        int totalSkipped = 0;
        int totalPending = 3;
        int totalUndefined = 0;
        int totalMissing = 0;

        Object[] result = util.orderStepsByValue(totalPassed, totalFailed, totalSkipped, totalPending, totalUndefined,
                totalMissing).toArray();
        Assert.assertArrayEquals(
                "lists not ordered correctly",
                new String[] {PENDING.color, PASSED.color, FAILED.color, SKIPPED.color, UNDEFINED.color, MISSING.color},
                result);
    }

    @Test
    public void testOrderStepsByValue_ThreeDupes_1() throws Exception {

        int totalPassed = 1;
        int totalFailed = 0;
        int totalSkipped = 0;
        int totalPending = 3;
        int totalUndefined = 0;
        int totalMissing = 0;

        Object[] result = util.orderStepsByValue(totalPassed, totalFailed, totalSkipped, totalPending, totalUndefined,
                totalMissing).toArray();
        Assert.assertArrayEquals(
                "lists not ordered correctly",
                new String[] {PENDING.color, PASSED.color, FAILED.color, SKIPPED.color, UNDEFINED.color, MISSING.color},
                result);
    }

    @Test
    public void testOrderScenariosByValue_NoDupes_1() throws Exception {

        int totalPassed = 1;
        int totalFailed = 0;
        int totalSkipped = 0;
        int totalPending = 0;
        int totalUndefined = 0;
        int totalMissing = 0;

        Object[] result = util.orderStepsByValue(totalPassed, totalFailed, totalSkipped, totalPending,
                totalUndefined, totalMissing).toArray();
        Assert.assertArrayEquals(
                "lists not ordered correctly",
                new String[] {PASSED.color, FAILED.color, SKIPPED.color, PENDING.color, UNDEFINED.color, MISSING.color},
                result);
    }

    @Test
    public void testOrderScenariosByValue_NoDupes_2() throws Exception {

        int totalPassed = 1;
        int totalFailed = 2;
        int totalSkipped = 0;
        int totalPending = 0;
        int totalUndefined = 0;
        int totalMissing = 0;

        Object[] result = util.orderStepsByValue(totalPassed, totalFailed, totalSkipped, totalPending,
                totalUndefined, totalMissing).toArray();
        Assert.assertArrayEquals("lists not ordered correctly",
                new String[] {FAILED.color, PASSED.color, SKIPPED.color, PENDING.color, UNDEFINED.color, MISSING.color},
                result);
    }

    @Test
    public void testOrderScenariosByValue_Dupes() throws Exception {

        int totalPassed = 0;
        int totalFailed = 0;
        int totalSkipped = 0;
        int totalPending = 0;
        int totalUndefined = 0;
        int totalMissing = 0;

        Object[] result = util.orderStepsByValue(totalPassed, totalFailed, totalSkipped, totalPending,
                totalUndefined, totalMissing).toArray();
        Assert.assertArrayEquals(
                "lists not ordered correctly",
                new String[] {PASSED.color, FAILED.color, SKIPPED.color, PENDING.color, UNDEFINED.color, MISSING.color},
                result);
    }

    /**
     * Method: generateTagChartData(List<TagObject> tagObjectList)
     */
    @Test
    public void testGenerateTagChartData() throws Exception {
        // TODO: Test goes here...
    }

    /**
     * Method: getTags(List<TagObject> tagObjectList)
     */
    @Test
    public void testGetTags() throws Exception {
        List<TagObject> tagObjectList = new ArrayList<TagObject>();
        TagObject tag = new TagObject("TestTagName", new ArrayList<ScenarioTag>());
        tagObjectList.add(tag);
        String result = JsChartUtil.getTags(tagObjectList);
        Assert.assertEquals("['TestTagName']", result);
    }

    /**
     * Method: getTags(List<TagObject> tagObjectList)
     */
    @Test
    public void testGetTags_withoutTags() throws Exception {
        List<TagObject> tagObjectList = new ArrayList<TagObject>();
        String result = JsChartUtil.getTags(tagObjectList);
        Assert.assertEquals("[]", result);
    }

    /**
     * Method: generateTagChartDataForHighCharts(List<TagObject> tagObjectList)
     */
    @Test
    public void testGenerateTagChartDataForHighCharts() throws Exception {
        List<TagObject> tagObjectList = new ArrayList<TagObject>();
        TagObject tag = new TagObject("TestTagName", new ArrayList<ScenarioTag>());
        tagObjectList.add(tag);
        String result = JsChartUtil.generateTagChartDataForHighCharts(tagObjectList);
        Assert.assertEquals("[[0,0,0,0]]", result);
    }

    /**
     * Method: generateTagChartDataForHighCharts(List<TagObject> tagObjectList)
     */
    @Test
    public void testGenerateTagChartDataForHighCharts_withoutTags() throws Exception {
        List<TagObject> tagObjectList = new ArrayList<TagObject>();
        String result = JsChartUtil.generateTagChartDataForHighCharts(tagObjectList);
        Assert.assertEquals("[]", result);
    }

    /**
     * Method: compare(Object a, Object b)
     */
    @Test
    public void testCompare() throws Exception {
        // TODO: Test goes here...
    }
}
