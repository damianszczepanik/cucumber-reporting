package net.masterthought.cucumber;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

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
        assertThat("['TestTagName']").isEqualTo(result);
    }

    @Test
    public void testGetTags_withoutTags() throws Exception {
        List<TagObject> tagObjectList = new ArrayList<>();
        String result = ChartUtil.getTags(tagObjectList);
        assertThat("[]").isEqualTo(result);
    }

    @Test
    public void testGenerateTagChartDataForHighCharts() throws Exception {
        List<TagObject> tagObjectList = new ArrayList<>();
        TagObject tag = new TagObject("TestTagName");
        tagObjectList.add(tag);
        String result = ChartUtil.generateTagChartDataForHighCharts(tagObjectList);
        assertThat("[[0, 0, 0, 0]]").isEqualTo(result);
    }

    @Test
    public void testGenerateTagChartDataForHighCharts_withoutTags() throws Exception {
        List<TagObject> tagObjectList = new ArrayList<>();
        String result = ChartUtil.generateTagChartDataForHighCharts(tagObjectList);
        assertThat("[]").isEqualTo(result);
    }
}
