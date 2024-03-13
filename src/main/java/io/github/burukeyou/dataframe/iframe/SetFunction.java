package io.github.burukeyou.dataframe.iframe;

public interface SetFunction<T,V> {
    void accept(T t, V v);
}
