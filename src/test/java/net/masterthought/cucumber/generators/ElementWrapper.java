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

    public ElementWrapper byClass(String cssClass) {
        Elements inner = element.getElementsByClass(cssClass);
        if (inner == null) {
            throw new IllegalArgumentException("Could not get element by class:" + cssClass);
        }
        if (inner.isEmpty()) {
            throw new IllegalArgumentException("Could not find element with class:" + cssClass);
        }
        if (!inner.first().equals(inner.last())) {
            throw new IllegalArgumentException("Found more than one element with class:" + cssClass);
        }

        return new ElementWrapper(inner.first());
    }

    public ElementWrapper bySelector(String selector) {
        Elements inner = element.getElementsByTag(selector);
        if (inner == null) {
            throw new IllegalArgumentException("Could not get element with selector:" + selector);
        }
        if (inner.isEmpty()) {
            throw new IllegalArgumentException("Could not find element with selector:" + selector);
        }
        if (!inner.first().equals(inner.last())) {
            throw new IllegalArgumentException("Found more than one element with selector:" + selector);
        }

        return new ElementWrapper(inner.first());
    }

    public Elements bySelectors(String selector) {
        Elements inner = element.select(selector);
        if (inner == null) {
            throw new IllegalArgumentException("Could not get element with selector:" + selector);
        }
        if (inner.isEmpty()) {
            throw new IllegalArgumentException("Could not find element with selector:" + selector);
        }

        return inner;
    }

    public Element getElement() {
        return element;
    }

    public String text() {
        return element.text();
    }

}
