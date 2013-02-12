package net.masterthought.cucumber.json;

import net.masterthought.cucumber.util.Util;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Element {

    private String name;
    private int thiscount;
    private String description;
    private String keyword;
    private Step[] steps;
    private Tag[] tags;
    
    public static int counter=0;

    public Element() {
        counter++;
        thiscount=counter;
    }

    public Step[] getSteps() {
        for(Step s:steps){
            s.setNameScen(thiscount+name.trim().toLowerCase().replaceAll(" ", ""));
        }
        return steps;
      }

      public Util.Status getStatus() {
          Closure<String, Step> scenarioStatus = new Closure<String, Step>() {
              public Util.Status call(Step step) {
                  return step.getStatus();
              }
          };
          List<Util.Status> results = Util.collectSteps(steps, scenarioStatus);
          return results.contains(Util.Status.FAILED) ? Util.Status.FAILED : Util.Status.PASSED;
      }

      public String getRawName(){
          return name;
      }

    public String getKeyword() {
        return keyword;
    }

    public String getName() {
          List<String> contentString = new ArrayList<String>();

          if (Util.itemExists(keyword)) {
              contentString.add("<span class=\"scenario-keyword\">" + keyword + ": </span>");
          }

          if (Util.itemExists(name)) {
              contentString.add("<span class=\"scenario-name\">" + name + "</span>");
          }

          return Util.itemExists(contentString) ? Util.resultScen(getStatus(),thiscount+name) + StringUtils.join(contentString.toArray(), " ") + Util.closeDivScen() : "";
      }

      public List<String> getTagList() {
          return processTags();
      }

      public boolean hasTags() {
          return Util.itemExists(tags);
      }

      private List<String> processTags() {
          List<String> results = new ArrayList<String>();
          if (Util.itemExists(tags)) {
              StringClosure<String, Tag> scenarioTags = new StringClosure<String, Tag>() {
                  public String call(Tag tag) {
                      return tag.getName();
                  }
              };
              results = Util.collectTags(tags, scenarioTags);
          }
          return results;
      }

      public String getTags() {
          String result = "<div class=\"feature-tags\">";
          if (Util.itemExists(tags)) {
              String tagList = StringUtils.join(processTags().toArray(), ",");
              result = "<div class=\"feature-tags\">" + tagList ;
          }
          return result;
      }
      
      public String closeTag(){
          return "</div>";
      }


}
