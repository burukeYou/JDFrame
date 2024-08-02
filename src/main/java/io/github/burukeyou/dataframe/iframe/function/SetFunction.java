package io.github.burukeyou.dataframe.iframe.function;


/**
 * target object T to accept value v
 * @param <T>           target object
 * @param <V>           value
 */

@FunctionalInterface
public interface SetFunction<T,V> {

    /**
     * Applies this function to the given argument.
     */
    void accept(T t, V v);
}
