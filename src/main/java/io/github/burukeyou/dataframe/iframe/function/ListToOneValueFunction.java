package io.github.burukeyou.dataframe.iframe.function;

import java.util.List;

/**
 *
 * @param <T>
 */
@FunctionalInterface
public interface ListToOneValueFunction<T,V> {

    /**
     * list to one value
     */
    V apply(List<T> elements);
}
