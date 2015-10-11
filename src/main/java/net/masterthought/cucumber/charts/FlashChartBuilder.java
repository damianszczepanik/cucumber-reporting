package net.masterthought.cucumber.charts;

import java.util.List;

import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.json.support.TagObject;

public final class FlashChartBuilder {

    public static String getStepsChart(int totalPassed, int totalFailed, int totalSkipped, int totalPending,
            int totalUndefined, int totalMissing) {
        StringBuilder sb = new StringBuilder();
        sb.append("<chart>");
        sb.append("<chart_data><row><null/>");
        for (Status status : Status.getOrderedStatuses()) {
            sb.append("<string>").append(status.getLabel()).append("</string>");
        }
        sb.append("</row><row><string></string>");
        sb.append(wrapNumber(totalPassed));
        sb.append(wrapNumber(totalFailed));
        sb.append(wrapNumber(totalSkipped));
        sb.append(wrapNumber(totalPending));
        sb.append(wrapNumber(totalUndefined));
        sb.append(wrapNumber(totalMissing));
        sb.append("</row></chart_data>");

        sb.append("<chart_label shadow='low' color='ffffff' alpha='95' size='20' position='inside' as_percentage='true'/>");
        sb.append("<chart_pref select='true'/>");
        sb.append("<chart_rect x='90' y='85' width='300' height='200'/>");
        sb.append("<chart_transition type='scale' delay='1' duration='.5' order='category'/>");
        sb.append("<chart_type>donut</chart_type><draw><text layer='foreground' color='2B3856' alpha='60' x='5' y='260' width='250' height='100' size='30'>Steps</text>");
        sb.append("<rect transition='dissolve' layer='background' x='60' y='100' width='360' height='150' fill_alpha='0' line_color='ffffff' line_alpha='25' line_thickness='40' corner_tl='40' corner_tr='40' corner_br='40' corner_bl='40'/>");
        sb.append("<circle transition='dissolve' layer='background' x='240' y='150' radius='150' fill_color='ccddff' fill_alpha='100' line_thickness='0' bevel='bg' blur='blur1'/>");
        sb.append("<rect transition='dissolve' layer='background' shadow='soft' x='65' y='15' width='350' height='35' fill_color='ddeeff' fill_alpha='90' corner_tl='10' corner_tr='10' corner_br='10' corner_bl='10'/>");
        sb.append("</draw>");

        sb.append("<filter>");
        sb.append("<shadow id='low' distance='2' angle='45' color='0' alpha='40' blurX='5' blurY='5'/>");
        sb.append("<shadow id='high' distance='5' angle='45' color='0' alpha='40' blurX='10' blurY='10'/>");
        sb.append("<shadow id='soft' distance='2' angle='45' color='0' alpha='20' blurX='5' blurY='5'/>");
        sb.append("<bevel id='data' angle='45' blurX='5' blurY='5' distance='3' highlightAlpha='15' shadowAlpha='25' type='inner'/>");
        sb.append("<bevel id='bg' angle='45' blurX='50' blurY='50' distance='10' highlightAlpha='35' shadowColor='0000ff' shadowAlpha='25' type='full'/>");
        sb.append("<blur id='blur1' blurX='75' blurY='75' quality='1'/>");
        sb.append("</filter>");

        sb.append("<context_menu full_screen='false'/>");
        sb.append("<legend transition='dissolve' x='90' width='330' bevel='low' fill_alpha='0' line_alpha='0' bullet='circle' size='12' color='000000' alpha='100'/>");
        generateSeriesColor(sb);
        sb.append("<series_explode><number>5</number><number>30</number><number>5</number><number>30</number><number>5</number><number>30</number></series_explode><series transfer='true'/>");
        sb.append("</chart>");

        return sb.toString();
    }

    public static String pieScenariosChart(int totalPassed, int totalFailed) {
        StringBuilder sb = new StringBuilder();

        sb.append("<chart><chart_data><row><null/><string>Passed</string><string>Failed</string></row><row><string></string>");
        sb.append(wrapNumber(totalPassed));
        sb.append(wrapNumber(totalFailed));
        sb.append("</row></chart_data>");

        sb.append("<chart_grid_h alpha='10' color='ffffff' thickness='2' type='dotted'/>");
        sb.append("<chart_label shadow='low' color='ffffff' alpha='95' size='30' position='inside' as_percentage='true'/>");
        sb.append("<chart_rect x='100' y='50' width='300' height='210' positive_color='000000' positive_alpha='0' negative_color='ff0000' negative_alpha='0'/>");
        sb.append("<chart_transition type='drop' delay='0' duration='3' order='all'/>");
        sb.append("<chart_type>pie</chart_type>");
        sb.append("<draw><rect shadow='bg' layer='background' x='0' y='0' width='480' height='300' fill_color='4c5577' fill_alpha='100' line_alpha='0' line_thickness='0'/>");
        sb.append("<text shadow='high' transition='zoom' delay='1.5' duration='1' color='ffffff' alpha='90' size='30' x='5' y='260' width='250' height='100'>Scenarios</text></draw>");

        sb.append("<filter>");
        sb.append("<shadow id='high' distance='5' angle='45' color='0' alpha='50' blurX='10' blurY='10'/>");
        sb.append("<shadow id='bg' inner='true' quality='2' distance='25' angle='-45' color='000000' alpha='35' blurX='100' blurY='100'/>");
        sb.append("<bevel id='data' angle='45' blurX='30' blurY='30' distance='5' highlightAlpha='25' highlightColor='ffffff' shadowAlpha='40' type='inner'/>");
        sb.append("</filter>");

        sb.append("<context_menu full_screen='false'/>");
        sb.append("<legend transition='dissolve' x='120' y='-80' width='230' bevel='low' fill_alpha='0' line_alpha='0' bullet='circle' size='12' color='ffffff' alpha='100' layout='horizontal'/>");
        generateSeriesColor(sb);
        sb.append("<series_explode><number>0</number><number>20</number></series_explode>");
        sb.append("</chart>");

        return sb.toString();
    }

    private static String wrapNumber(int number) {
        StringBuilder sb = new StringBuilder();
        sb.append("<number shadow='high' bevel='data' line_color='FFFFFF' line_thickness='3' line_alpha='75'>");
        sb.append(number);
        sb.append("</number>");

        return sb.toString();
    }

    public static String generateStackedColumnChart(List<TagObject> tagObjectList) {
        StringBuilder sb = new StringBuilder();
        sb.append("<chart>");
        sb.append("<axis_category shadow='low' size='10' color='FFFFFF' alpha='75' orientation='diagonal_down'/>");
        sb.append("<axis_ticks value_ticks='true' category_ticks='true' minor_count='1'/>");
        sb.append("<axis_value shadow='low' size='10' color='FFFFFF' alpha='75'/>");

        sb.append("<chart_border top_thickness='0' bottom_thickness='2' left_thickness='2' right_thickness='0' />");
        sb.append("<chart_data><row><null/>").append(generateRowsForColumnChart(tagObjectList)).append("</row>").append(generateColumnsForColumnChart(tagObjectList)).append("</chart_data>");
        sb.append("<chart_grid_h thickness='1' type='solid'/>");
        sb.append("<chart_grid_v thickness='1' type='solid'/>");
        sb.append("<chart_rect x='80' y='30' width='470' height='150' positive_color='888888' positive_alpha='50'/>");
        sb.append("<chart_pref rotation_x='15' rotation_y='0' min_x='0' max_x='80' min_y='0' max_y='60'/>");
        sb.append("<chart_type>stacked 3d column</chart_type>");
        
        sb.append("<filter><shadow id='high' distance='5' angle='45' alpha='35' blurX='15' blurY='15'/>");
        sb.append("<shadow id='low' distance='2' angle='45' alpha='50' blurX='5' blurY='5'/>");
        sb.append("</filter>");
         
        sb.append("<legend shadow='high' x='75' y='270' width='470' height='50' margin='20' fill_color='000000' fill_alpha='7' line_color='000000' line_alpha='0' line_thickness='0' layout='horizontal' size='12' color='000000' alpha='50'/>");
        sb.append("<tooltip color='ffffcc' alpha='80' size='12' background_color_1='444488' background_alpha='75' shadow='low'/>");
        generateSeriesColor(sb);
        sb.append("<series bar_gap='0' set_gap='20'/>");
        sb.append("</chart>");

        return sb.toString();
    }

    private static String generateRowsForColumnChart(List<TagObject> tagObjectList) {
        StringBuilder sb = new StringBuilder();
        for (TagObject tag : tagObjectList) {
            sb.append("<string>" + tag.getTagName() + "</string>");
        }
        return sb.toString();
    }

    private static String generateColumnsForColumnChart(List<TagObject> tagObjectList) {
        StringBuilder sb = new StringBuilder();

        for (Status status : Status.getOrderedStatuses()) {
            sb.append("<row>");
            sb.append("<string>").append(status.getLabel()).append("</string>");
            for (TagObject tag : tagObjectList) {
                int statusCounter = tag.getNumberOfStatus(status);
                sb.append("<number tooltip='");
                sb.append(statusCounter).append("'>").append(statusCounter);
                sb.append("</number>");
            }
            sb.append("</row>");
        }

        return sb.toString();
    }

    private static void generateSeriesColor(StringBuilder sb) {
        sb.append("<series_color>");

        for (Status status : Status.getOrderedStatuses()) {
            // substring because it expects color without trailing hash character
            sb.append("<color>").append(status.color.substring(1)).append("</color>");
        }
        sb.append("</series_color>");

    }
}
