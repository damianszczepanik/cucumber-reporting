package net.masterthought.cucumber;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.masterthought.cucumber.charts.*;

/** 
* JsChartUtil Tester. 
* 
* @author <Authors name> 
* @since <pre>Jan 21, 2013</pre> 
* @version 1.0 
*/ 
public class JsChartUtilTest {

    private JsChartUtil util;
    public static final String PASSED_COLOUR = "#88dd11";
    public static final String FAILED_COLOUR = "#cc1134";
    public static final String SKIPPED_COLOUR = "#88aaff";
    public static final String PENDING_COLOUR = "#FBB917";

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

    Object[] result = util.orderStepsByValue(totalPassed, totalFailed, totalSkipped, totalPending).toArray();
    Assert.assertTrue("lists not ordered correctly",
          Arrays.equals(Lists.newArrayList(PENDING_COLOUR, SKIPPED_COLOUR, FAILED_COLOUR, PASSED_COLOUR).toArray(), result));
}

@Test
public void testOrderStepsByValue_NoDupes_2() throws Exception {

    int totalPassed = 7;
    int totalFailed = 3;
    int totalSkipped = 5;
    int totalPending = 1;

    Object[] result = util.orderStepsByValue(totalPassed, totalFailed, totalSkipped, totalPending).toArray();
    Assert.assertTrue("lists not ordered correctly",
            Arrays.equals(Lists.newArrayList(PASSED_COLOUR, SKIPPED_COLOUR, FAILED_COLOUR, PENDING_COLOUR).toArray(), result));
}

@Test
public void testOrderStepsByValue_TwoDupes_1() throws Exception {

    int totalSkipped = 1;
    int totalFailed = 2;
    int totalPassed = 2;
    int totalPending = 0;

    Object[] result = util.orderStepsByValue(totalPassed, totalFailed, totalSkipped, totalPending).toArray();
    Assert.assertTrue("lists not ordered correctly",
            Arrays.equals(Lists.newArrayList(PASSED_COLOUR, FAILED_COLOUR, SKIPPED_COLOUR, PENDING_COLOUR).toArray(), result) ||
            Arrays.equals(Lists.newArrayList(FAILED_COLOUR, PASSED_COLOUR, SKIPPED_COLOUR, PENDING_COLOUR).toArray(), result));
}

@Test
public void testOrderStepsByValue_TwoDupes_2() throws Exception {

    int totalSkipped = 1;
    int totalFailed = 3;
    int totalPassed = 0;
    int totalPending = 0;

    Object[] result = util.orderStepsByValue(totalPassed, totalFailed, totalSkipped, totalPending).toArray();
    Assert.assertTrue("lists not ordered correctly",
            Arrays.equals(Lists.newArrayList(FAILED_COLOUR, SKIPPED_COLOUR, PENDING_COLOUR, PASSED_COLOUR).toArray(), result) ||
            Arrays.equals(Lists.newArrayList(FAILED_COLOUR, SKIPPED_COLOUR, PASSED_COLOUR, PENDING_COLOUR).toArray(), result));
}

@Test
public void testOrderStepsByValue_TwoDupes_3() throws Exception {

    int totalSkipped = 0;
    int totalFailed = 0;
    int totalPassed = 1;
    int totalPending = 3;

    Object[] result = util.orderStepsByValue(totalPassed, totalFailed, totalSkipped, totalPending).toArray();
    Assert.assertTrue("lists not ordered correctly",
            Arrays.equals(Lists.newArrayList(PENDING_COLOUR, PASSED_COLOUR, SKIPPED_COLOUR, FAILED_COLOUR).toArray(), result) ||
            Arrays.equals(Lists.newArrayList(PENDING_COLOUR, PASSED_COLOUR, FAILED_COLOUR, SKIPPED_COLOUR).toArray(), result));
}


@Test
public void testOrderStepsByValue_ThreeDupes_1() throws Exception {

    int totalSkipped = 0;
    int totalFailed = 0;
    int totalPassed = 1;
    int totalPending = 3;

    Object[] result = util.orderStepsByValue(totalPassed, totalFailed, totalSkipped, totalPending).toArray();
    Assert.assertTrue("lists not ordered correctly",
            Arrays.equals(Lists.newArrayList(PENDING_COLOUR, PASSED_COLOUR, SKIPPED_COLOUR, FAILED_COLOUR).toArray(), result) ||
                    Arrays.equals(Lists.newArrayList(PENDING_COLOUR, PASSED_COLOUR, FAILED_COLOUR, SKIPPED_COLOUR).toArray(), result));
}

@Test
public void testOrderScenariosByValue_NoDupes_1() throws Exception {
    int totalFailed = 0;
    int totalPassed = 1;

    Object[] result = util.orderScenariosByValue(totalPassed, totalFailed).toArray();
    Assert.assertTrue("lists not ordered correctly",
            Arrays.equals(Lists.newArrayList(PASSED_COLOUR, FAILED_COLOUR).toArray(), result));
}

@Test
public void testOrderScenariosByValue_NoDupes_2() throws Exception {
    int totalFailed = 2;
    int totalPassed = 1;

    Object[] result = util.orderScenariosByValue(totalPassed, totalFailed).toArray();
    Assert.assertTrue("lists not ordered correctly",
            Arrays.equals(Lists.newArrayList(FAILED_COLOUR, PASSED_COLOUR).toArray(), result));
}

@Test
public void testOrderScenariosByValue_Dupes() throws Exception {
    int totalFailed = 0;
    int totalPassed = 0;

    Object[] result = util.orderScenariosByValue(totalPassed, totalFailed).toArray();
    Assert.assertTrue("lists not ordered correctly",
            Arrays.equals(Lists.newArrayList(FAILED_COLOUR, PASSED_COLOUR).toArray(), result) ||
            Arrays.equals(Lists.newArrayList(PASSED_COLOUR, FAILED_COLOUR).toArray(), result));
}

    /**
* 
* Method: generateTagChartData(List<TagObject> tagObjectList) 
* 
*/ 
@Test
public void testGenerateTagChartData() throws Exception { 
//TODO: Test goes here... 
} 

/**
* 
* Method: getTags(List<TagObject> tagObjectList) 
* 
*/ 
@Test
public void testGetTags() throws Exception { 
	List<TagObject> tagObjectList = new ArrayList<TagObject>();
	TagObject tag = new TagObject("TestTagName", new ArrayList());
	tagObjectList.add(tag);
	String result = JsChartUtil.getTags(tagObjectList);
	Assert.assertEquals("['TestTagName']", result);
}


/**
* 
* Method: getTags(List<TagObject> tagObjectList) 
* 
*/ 
@Test
public void testGetTags_withoutTags() throws Exception { 
	List<TagObject> tagObjectList = new ArrayList<TagObject>();
	String result = JsChartUtil.getTags(tagObjectList);
	Assert.assertEquals("[]", result);
}

/**
* 
* Method: generateTagChartDataForHighCharts(List<TagObject> tagObjectList) 
* 
*/ 
@Test
public void testGenerateTagChartDataForHighCharts() throws Exception { 
	List<TagObject> tagObjectList = new ArrayList<TagObject>();
	TagObject tag = new TagObject("TestTagName", new ArrayList());
	tagObjectList.add(tag);
	String result = JsChartUtil.generateTagChartDataForHighCharts(tagObjectList);
	Assert.assertEquals("[[0,0,0,0]]", result);
} 

/**
* 
* Method: generateTagChartDataForHighCharts(List<TagObject> tagObjectList) 
* 
*/ 
@Test
public void testGenerateTagChartDataForHighCharts_withoutTags() throws Exception { 
	List<TagObject> tagObjectList = new ArrayList<TagObject>();
	String result = JsChartUtil.generateTagChartDataForHighCharts(tagObjectList);
	Assert.assertEquals("[]", result);
} 

/** 
* 
* Method: compare(Object a, Object b) 
* 
*/ 
@Test
public void testCompare() throws Exception { 
//TODO: Test goes here... 
} 


} 
