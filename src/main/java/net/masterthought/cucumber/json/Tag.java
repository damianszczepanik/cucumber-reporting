package net.masterthought.cucumber.json;

import com.googlecode.totallylazy.Function1;

public class Tag {

    private String name;

    public Tag(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static class functions {

        public static Function1<Tag, String> getName() {
            return new Function1<Tag, String>() {
                @Override
                public String call(Tag tag) throws Exception {
                    return tag.getName();
                }
            };
        }
    }

}
