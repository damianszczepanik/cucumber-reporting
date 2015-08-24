package net.masterthought.cucumber.json;

import com.googlecode.totallylazy.Function1;

public class Tag {

    public final String name = null;

    public static class functions {

        public static Function1<Tag, String> getName() {
            return new Function1<Tag, String>() {
                @Override
                public String call(Tag tag) throws Exception {
                    return tag.name;
                }
            };
        }
    }

}
