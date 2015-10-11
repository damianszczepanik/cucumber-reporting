package net.masterthought.cucumber;

import org.apache.velocity.VelocityContext;

import java.util.*;

/**
 * Created by Arno Noordover on 30-1-14.
 */
public final class VelocityContextMap implements Map<String,Object> {

    private final VelocityContext velocityContext;

    private VelocityContextMap(VelocityContext velocityContext) {
        this.velocityContext = velocityContext;
    }

    public static VelocityContextMap of(VelocityContext velocityContext) {
        return new VelocityContextMap(velocityContext);
    }
    @Override
    public int size() {
        return velocityContext.getKeys().length;
    }

    @Override
    public boolean isEmpty() {
        return velocityContext.getKeys().length == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return velocityContext.containsKey(key);
    }

    @Override
    public boolean containsValue(Object searchFor) {
        for (String key : keySet()) {
            Object found = velocityContext.get(key);
            if (searchFor.equals(found)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object get(Object key) {
        return velocityContext.get((String) key);
    }

    @Override
    public Object put(String key, Object value) {
        return velocityContext.put(key,value);
    }

    @Override
    public Object remove(Object key) {
        return velocityContext.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ?> map) {
        for (Entry<? extends String, ?> entry : map.entrySet()) {
            velocityContext.put(entry.getKey(),entry.getValue());
        }
    }

    @Override
    public void clear() {
        for (Object key : velocityContext.getKeys()) {
            velocityContext.remove(key);
        }
    }

    @Override
    public Set<String> keySet() {
        return new HashSet(Arrays.asList(velocityContext.getKeys()));
    }

    @Override
    public Collection<Object> values() {
        Collection<Object> result = new HashSet<Object>();
        for (String key : keySet()) {
            result.add(velocityContext.get(key));
        }
        return result;
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        Set<Entry<String,Object>> result = new HashSet<Entry<String, Object>>();
        for (String key : keySet()) {
            result.add(new AbstractMap.SimpleEntry<String,Object>(key,velocityContext.get(key)));
        }
        return result;
    }

    public VelocityContext getVelocityContext() {
        return velocityContext;
    }
}
