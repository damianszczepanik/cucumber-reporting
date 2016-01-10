package net.masterthought.cucumber;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import net.masterthought.cucumber.json.support.TagObject;
import net.masterthought.cucumber.util.ChartUtil;

public class ChartUtilTest {

    @Test
    public void testGetTags() throws Exception {
        List<TagObject> tagObjectList = new ArrayList<>();
        TagObject tag = new TagObject("TestTagName");
        tagObjectList.add(tag);
        String result = ChartUtil.getTags(tagObjectList);
        Assert.assertEquals("['TestTagName']", result);
    }

    @Test
    public void testGetTags_withoutTags() throws Exception {
        List<TagObject> tagObjectList = new ArrayList<>();
        String result = ChartUtil.getTags(tagObjectList);
        Assert.assertEquals("[]", result);
    }

    @Test
    public void testGenerateTagChartDataForHighCharts() throws Exception {
        List<TagObject> tagObjectList = new ArrayList<>();
        TagObject tag = new TagObject("TestTagName");
        tagObjectList.add(tag);
        String result = ChartUtil.generateTagChartDataForHighCharts(tagObjectList);
        Assert.assertEquals("[[0,0,0,0]]", result);
    }

    @Test
    public void testGenerateTagChartDataForHighCharts_withoutTags() throws Exception {
        List<TagObject> tagObjectList = new ArrayList<>();
        String result = ChartUtil.generateTagChartDataForHighCharts(tagObjectList);
        Assert.assertEquals("[]", result);
    }
}
