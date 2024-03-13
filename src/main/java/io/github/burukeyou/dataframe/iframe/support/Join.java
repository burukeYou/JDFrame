package io.github.burukeyou.dataframe.iframe.support;

/**
 * Connection operation
 * @author  caizhihao
 * @param <T>
 * @param <K>
 * @param <R>
 */
public interface Join<T,K,R> {

    /**
     *
     * @param t     If it is a right connection, it may be null
     * @param k     If it is a left connection, it may be null
     * @return
     */
    R join(T t, K k);
}
