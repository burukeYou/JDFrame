package io.github.burukeyou.dataframe.dataframe.support;

public interface JoinOn<T,K> {

    boolean on(T t, K k);
}
