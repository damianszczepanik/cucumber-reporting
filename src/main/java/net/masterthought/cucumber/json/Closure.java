package net.masterthought.cucumber.json;

import net.masterthought.cucumber.util.Util;

public interface Closure<R, T> {
  public Util.Status call(T t);
}
