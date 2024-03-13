package io.github.burukeyou.dataframe.iframe.support;

public interface JoinOn<T,K> {

    boolean on(T t, K k);
}
