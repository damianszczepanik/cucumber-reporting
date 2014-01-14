//package net.masterthought.cucumber.util;
//
//import com.googlecode.totallylazy.Function1;
//import net.masterthought.cucumber.json.Step;
//import net.masterthought.cucumber.json.Tag;
//
//public class Functions {
//
//    public static Function1<Step, Util.Status> scenarioStatus() {
//        return new Function1<Step, Util.Status>() {
//            @Override
//            public Util.Status call(Step step) throws Exception {
//                return step.getStatus();
//            }
//        };
//    }
//
//    public static Function1<Tag, String> tagName() {
//        return new Function1<Tag, String>() {
//            @Override
//            public String call(Tag tag) throws Exception {
//                return tag.getName();
//            }
//        };
//    }
//}
