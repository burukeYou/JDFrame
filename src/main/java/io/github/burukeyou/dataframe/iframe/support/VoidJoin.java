package io.github.burukeyou.dataframe.iframe.support;

/**
 * Connection operation
 * @author  caizhihao
 * @param <T>
 * @param <K>
 */
@FunctionalInterface
public interface VoidJoin<T,K> {

    /**
     * Join Operation
     * @param t     If it is a right connection, it may be null
     * @param k     If it is a left connection, it may be null
     */
    void join(T t, K k);

}
