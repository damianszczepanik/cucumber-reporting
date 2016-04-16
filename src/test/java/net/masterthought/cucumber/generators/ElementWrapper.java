package net.masterthought.cucumber.generators;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.text.StrBuilder;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class ElementWrapper {

    private final Element element;

    public ElementWrapper(Element element) {
        this.element = element;
    }

    public ElementWrapper byId(String id) {
        Element inner = element.getElementById(id);
        if (inner == null) {
            throw new IllegalArgumentException("Could not get element by id:" + id);
        }
        return new ElementWrapper(inner);
    }

    public ElementWrapper oneByClass(String cssClass) {
        Elements inner = element.getElementsByClass(cssClass);

        assertNotEmpty(inner, cssClass);
        if (inner.size() > 1) {
            StrBuilder sb = new StrBuilder();
            for (int i = 0; i < inner.size(); i++) {
                sb.append(inner.get(i)).append("\n");
            }
            throw new IllegalArgumentException(String.format("Expected one but found %d elements with class '%s': %s",
                    inner.size(), cssClass, sb.toString()));
        }

        return new ElementWrapper(inner.first());
    }

    public ElementWrapper childByClass(String cssClass) {
        Elements children = element.children();

        List<Element> matched = new ArrayList<>();
        for (int i = 0; i < children.size(); i++) {
            Element child = children.get(i);
            if (child.hasClass(cssClass)) {
                matched.add(child);
            }
        }

        assertNotEmpty(matched, cssClass);
        if (matched.size() > 1) {
            StrBuilder sb = new StrBuilder();
            for (int i = 0; i < matched.size(); i++) {
                sb.append(matched.get(i)).append("\n");
            }
            throw new IllegalArgumentException(String.format("Expected one but found %d elements with class '%s': %s",
                    matched.size(), cssClass, sb.toString()));
        }

        return new ElementWrapper(matched.get(0));
    }

    public ElementWrapper[] allByClass(String cssClass) {
        Elements inner = element.getElementsByClass(cssClass);

        assertNotEmpty(inner, cssClass);

        return toArray(inner);
    }

    private ElementWrapper[] toArray(Elements inner) {
        ElementWrapper[] elements = new ElementWrapper[inner.size()];
        for (int i = 0; i < elements.length; i++) {
            elements[i] = new ElementWrapper(inner.get(i));
        }
        return elements;
    }

    public ElementWrapper oneBySelector(String selector) {
        Elements inner = element.getElementsByTag(selector);

        assertNotEmpty(inner, selector);
        if (!inner.first().equals(inner.last())) {
            throw new IllegalArgumentException("Found more than one element with selector: " + selector);
        }

        return new ElementWrapper(inner.first());
    }

    public ElementWrapper[] allBySelector(String selector) {
        Elements inner = element.select(selector);
        assertNotEmpty(inner, selector);

        return toArray(inner);
    }

    private void assertNotEmpty(Elements elements, String criteria) {
        if (elements == null || elements.isEmpty()) {
            throw new IllegalArgumentException(
                    String.format("Could not find element by '%s' in '%s' ", criteria, element.html()));
        }
    }

    private void assertNotEmpty(List<?> elements, String criteria) {
        if (elements == null || elements.isEmpty()) {
            throw new IllegalArgumentException(
                    String.format("Could not find element by '%s' in '%s' ", criteria, element.html()));
        }
    }

    public String text() {
        return element.text();
    }

    public Set<String> classNames() {
        return element.classNames();
    }

    public String attr(String attributeKey) {
        return element.attr(attributeKey);
    }
}
