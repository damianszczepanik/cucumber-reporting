package net.masterthought.cucumber.json;

public interface StringClosure<R, T> {
  public R call(T t);
}