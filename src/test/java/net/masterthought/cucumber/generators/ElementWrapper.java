package net.masterthought.cucumber.generators;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class ElementWrapper {

    private Element element;

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
        if (!inner.first().equals(inner.last())) {
            throw new IllegalArgumentException("Found more than one element with class: " + cssClass);
        }

        return new ElementWrapper(inner.first());
    }

    public Elements allByClass(String cssClass) {
        Elements inner = element.getElementsByClass(cssClass);

        assertNotEmpty(inner, cssClass);

        return inner;
    }

    public ElementWrapper oneBySelector(String selector) {
        Elements inner = element.getElementsByTag(selector);

        assertNotEmpty(inner, selector);
        if (!inner.first().equals(inner.last())) {
            throw new IllegalArgumentException("Found more than one element with selector: " + selector);
        }

        return new ElementWrapper(inner.first());
    }

    public Elements allBySelector(String selector) {
        Elements inner = element.select(selector);
        assertNotEmpty(inner, selector);

        return inner;
    }

    private void assertNotEmpty(Elements elements, String criteria) {
        if (elements == null || elements.isEmpty()) {
            throw new IllegalArgumentException("Could not find element by: " + criteria);
        }
    }

    public Element getElement() {
        return element;
    }

    public String text() {
        return element.text();
    }

}
