package net.masterthought.cucumber.charts;

import net.masterthought.cucumber.TagObject;

import java.util.List;

public class FlashChartBuilder {

    public static String donutChart(int total_passed, int total_failed, int total_skipped, int total_pending) {
        // I was going to use XMLBuilder to generate the chart - but it's so long and boring and I already have the xml so .....
        return "<chart><chart_data><row><null/><string>Passed</string><string>Failed</string><string>Skipped</string><string>Pending</string></row><row><string></string><number shadow='high' bevel='data' line_color='FFFFFF' line_thickness='3' line_alpha='75'>" + total_passed + "</number><number shadow='high' bevel='data' line_color='FFFFFF' line_thickness='3' line_alpha='75'>" + total_failed + "</number><number shadow='high' bevel='data' line_color='FFFFFF' line_thickness='3' line_alpha='75'>" + total_skipped + "</number><number shadow='high' bevel='data' line_color='FFFFFF' line_thickness='3' line_alpha='75'>" + total_pending + "</number></row></chart_data><chart_label shadow='low' color='ffffff' alpha='95' size='15' position='inside' as_percentage='true' /><chart_pref select='true' /><chart_rect x='90' y='85' width='300' height='175' /><chart_transition type='scale' delay='1' duration='.5' order='category' /><chart_type>donut</chart_type><draw><text layer='foreground' color='2B3856' alpha='60' x='5' y='260' width='250' height='100' size='30'>Steps</text><rect transition='dissolve' layer='background' x='60' y='100' width='360' height='150' fill_alpha='0' line_color='ffffff' line_alpha='25' line_thickness='40' corner_tl='40' corner_tr='40' corner_br='40' corner_bl='40' /><circle transition='dissolve' layer='background' x='240' y='150' radius='150' fill_color='ccddff' fill_alpha='100' line_thickness='0' bevel='bg' blur='blur1' /><rect transition='dissolve' layer='background' shadow='soft' x='65' y='10' width='350' height='35' fill_color='ddeeff' fill_alpha='90' corner_tl='10' corner_tr='10' corner_br='10' corner_bl='10' /></draw><filter><shadow id='low' distance='2' angle='45' color='0' alpha='40' blurX='5' blurY='5' /><shadow id='high' distance='5' angle='45' color='0' alpha='40' blurX='10' blurY='10' /><shadow id='soft' distance='2' angle='45' color='0' alpha='20' blurX='5' blurY='5' /><bevel id='data' angle='45' blurX='5' blurY='5' distance='3' highlightAlpha='15' shadowAlpha='25' type='inner' /><bevel id='bg' angle='45' blurX='50' blurY='50' distance='10' highlightAlpha='35' shadowColor='0000ff' shadowAlpha='25' type='full' /><blur id='blur1' blurX='75' blurY='75' quality='1' /></filter><context_menu full_screen='false' /><legend transition='dissolve' x='90' width='330' bevel='low' fill_alpha='0' line_alpha='0' bullet='circle' size='12' color='000000' alpha='100' /><series_color><color>88dd11</color><color>cc1134</color><color>88aaff</color><color>FBB917</color></series_color><series_explode><number>25</number><number>0</number><number>0</number><number>0</number></series_explode><series transfer='true' /></chart>";
    }

    public static String pieChart(int total_passed, int total_failed){
        return "<chart><chart_data><row><null/><string>Passed</string><string>Failed</string></row><row><string></string><number shadow='high' bevel='data' line_color='FFFFFF' line_thickness='3' line_alpha='65'>" + total_passed + "</number><number shadow='high'>" + total_failed + "</number></row></chart_data><chart_grid_h alpha='10' color='ffffff' thickness='2' type='dotted' /><chart_label shadow='low' color='ffffff' alpha='95' size='30' position='inside' as_percentage='true'/><chart_rect x='100' y='50' width='300' height='210' positive_color='000000' positive_alpha='0' negative_color='ff0000' negative_alpha='0' /><chart_transition type='drop' delay='0' duration='3' order='all' /><chart_type>pie</chart_type><draw><rect shadow='bg' layer='background' x='0' y='0' width='480' height='300' fill_color='4c5577' fill_alpha='100' line_alpha='0' line_thickness='0' /><text shadow='high' transition='zoom' delay='1.5' duration='1' color='ffffff' alpha='90' size='30' x='5' y='260' width='250' height='100'>Scenarios</text></draw><filter><shadow id='high' distance='5' angle='45' color='0' alpha='50' blurX='10' blurY='10' /><shadow id='bg' inner='true' quality='2' distance='25' angle='-45' color='000000' alpha='35' blurX='100' blurY='100' /><bevel id='data' angle='45' blurX='30' blurY='30' distance='5' highlightAlpha='25' highlightColor='ffffff' shadowAlpha='40' type='inner' /></filter><context_menu full_screen='false' /><legend transition='dissolve' x='90' y='-90' width='330' bevel='low' fill_alpha='0' line_alpha='0' bullet='circle' size='12' color='ffffff' alpha='100' layout='horizontal'/><series_color><color>88dd11</color><color>cc1134</color></series_color><series_explode><number>20</number><number>30</number></series_explode></chart>";
    }

    public static String StackedColumnChart(List<TagObject> tagObjectList) {
        return "<chart><axis_category shadow='low' size='10' color='FFFFFF' alpha='75' orientation='diagonal_down' /><axis_ticks value_ticks='true' category_ticks='true' minor_count='1' /><axis_value shadow='low' size='10' color='FFFFFF' alpha='75' /><chart_border top_thickness='0' bottom_thickness='2' left_thickness='2' right_thickness='0' /><chart_data><row><null/>" + generateRowsForColumnChart(tagObjectList) + "</row>" + generateColumnsForColumnChart(tagObjectList) + "</chart_data><chart_grid_h thickness='1' type='solid' /><chart_grid_v thickness='1' type='solid' /><chart_rect x='80' y='30' width='470' height='150' positive_color='888888' positive_alpha='50' /><chart_pref rotation_x='15' rotation_y='0' min_x='0' max_x='80' min_y='0' max_y='60' /><chart_type>stacked 3d column</chart_type><filter><shadow id='high' distance='5' angle='45' alpha='35' blurX='15' blurY='15' /><shadow id='low' distance='2' angle='45' alpha='50' blurX='5' blurY='5' /></filter><legend shadow='high' x='75' y='270' width='470' height='50' margin='20' fill_color='000000' fill_alpha='7' line_color='000000' line_alpha='0' line_thickness='0' layout='horizontal' size='12' color='000000' alpha='50' /><tooltip color='ffffcc' alpha='80' size='12' background_color_1='444488' background_alpha='75' shadow='low' /><series_color><color>C5D88A</color><color>D88A8A</color><color>2DEAEC</color><color>ebcc81</color></series_color><series bar_gap='0' set_gap='20' /></chart>";

    }

    private static String generateRowsForColumnChart(List<TagObject> tagObjectList) {
        StringBuffer buffer = new StringBuffer();
        for (TagObject tag : tagObjectList) {
            buffer.append("<string>" + tag.getTagName() + "</string>");
        }
        return buffer.toString();
    }

    private static String generateColumnsForColumnChart(List<TagObject> tagObjectList) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<row>");
        buffer.append("<string>Passed</string>");
        for (TagObject tag : tagObjectList) {
            buffer.append("<number tooltip='" + tag.getNumberOfPasses() + "'>" + tag.getNumberOfPasses() + "</number>");

        }
        buffer.append("</row>");
        buffer.append("<row>");
        buffer.append("<string>Failed</string>");
        for (TagObject tag : tagObjectList) {
            buffer.append("<number tooltip='" + tag.getNumberOfFailures() + "'>" + tag.getNumberOfFailures() + "</number>");

        }
        buffer.append("</row>");
        buffer.append("<row>");
        buffer.append("<string>Skipped</string>");
        for (TagObject tag : tagObjectList) {
            buffer.append("<number tooltip='" + tag.getNumberOfSkipped() + "'>" + tag.getNumberOfSkipped() + "</number>");

        }
        buffer.append("</row>");
        buffer.append("<row>");
        buffer.append("<string>Pending</string>");
        for (TagObject tag : tagObjectList) {
            buffer.append("<number tooltip='" + tag.getNumberOfPending() + "'>" + tag.getNumberOfPending() + "</number>");

        }
        buffer.append("</row>");
        return buffer.toString();
    }
}
