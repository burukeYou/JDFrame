package io.github.burukeyou.dataframe.iframe.function;

import java.util.List;

/**
 *
 * @param <T>
 */
@FunctionalInterface
public interface ListSelectOneFunction<T> {

    /**
     * Select one from the list
     */
    T apply(List<T> elements);
}
