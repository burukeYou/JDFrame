package io.github.burukeyou.dataframe.iframe.support;

/**
 * @author  caizhiho
 * @param <T>
 * @param <K>
 */
public interface JoinOn<T,K> {

    /**
     * Determine whether the association is successful
     * @param t        left table element
     * @param k        right  table element
     */
    boolean on(T t, K k);
}
