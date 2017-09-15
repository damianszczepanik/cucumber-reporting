package net.masterthought.cucumber.generators.integrations.helpers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.*;

import org.apache.commons.lang.text.StrBuilder;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class WebAssertion {

    protected Element element;

    public WebAssertion() {
    }

    public WebAssertion(Element element) {
        this.element = element;
    }

    public <T extends WebAssertion> T byId(String id, Class<T> clazz) {
        Element inner = element.getElementById(id);
        if (inner == null) {
            throw new IllegalArgumentException("Could not get element by id:" + id);
        }
        return toAssertion(inner, clazz);
    }

    public <T extends WebAssertion> T oneByClass(String cssClass, Class<T> clazz) {
        Elements inner = element.getElementsByClass(cssClass);

        validateListSizeEqualToOne(inner, cssClass);

        return toAssertion(inner.first(), clazz);
    }

    public <T extends WebAssertion> T childByClass(String cssClass, Class<T> clazz) {
        Elements children = element.children();

        List<Element> matched = new ArrayList<>();
        for(Element child : children)
        {
            if (child.hasClass(cssClass)) {
                matched.add(child);
            }
        }

        validateListSizeEqualToOne(matched, cssClass);

        return toAssertion(matched.get(0), clazz);
    }

    private void validateListSizeEqualToOne(List<Element> list, String cssClass) {
        assertNotEmpty(list, cssClass);
        if (list.size() > 1) {
            StrBuilder sb = new StrBuilder();
            for(Element element : list) {
                sb.append(element).append("\n");
            }
            throw new IllegalArgumentException(String.format("Expected one but found %d elements with class '%s': %s",
                    list.size(), cssClass, sb.toString()));
        }
    }

    public <T extends WebAssertion> T[] allByClass(String cssClass, Class<T> clazz) {
        Elements inner = element.getElementsByClass(cssClass);

        assertNotEmpty(inner, cssClass);

        return toArray(inner, clazz);
    }

    private <T extends WebAssertion> T toAssertion(Element inner, Class<T> clazz) {
        T assertion;
        try {
            assertion = clazz.newInstance();
            assertion.element = inner;
            return assertion;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public <T extends WebAssertion> T[] allBySelector(String selector, Class<T> clazz) {
        Elements inner = element.select(selector);
        // when current element is <abc> selector and querying is by the same selector
        // then at the first position is current element what is not desirable
        if (this.element.equals(inner.get(0))) {
            inner.remove(0);
        }
        assertNotEmpty(inner, selector);

        return toArray(inner, clazz);
    }

    private <T extends WebAssertion> T[] toArray(Elements inner, Class<T> clazz) {
        List<T> elements = new ArrayList<>();
        for(Element element : inner) {
            T assertion;
            try {
                assertion = clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new IllegalArgumentException(e);
            }
            assertion.element = element;
            elements.add(assertion);
        }

        @SuppressWarnings("unchecked")
        T[] array = (T[]) java.lang.reflect.Array.newInstance(clazz, elements.size());
        return elements.toArray(array);
    }

    public <T extends WebAssertion> T firstBySelector(String selector, Class<T> clazz) {
        Elements inner = element.select(selector);

        assertNotEmpty(inner, selector);
        return toAssertion(inner.first(), clazz);
    }

    private void assertNotEmpty(List<?> elements, String criteria) {
        assertThat(elements).describedAs("Element by '%s' does not exist in '%s' ", criteria, element.html()).isNotEmpty();
    }

    public String text() {
        return element.text();
    }

    public String html() {
        return element.html();
    }

    public Set<String> classNames() {
        return element.classNames();
    }

    public String attr(String attributeKey) {
        return element.attr(attributeKey);
    }

    @Override
    public String toString() {
        return html();
    }
}
