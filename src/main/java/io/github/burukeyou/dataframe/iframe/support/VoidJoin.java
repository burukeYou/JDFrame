package io.github.burukeyou.dataframe.iframe.support;

/**
 * Custom Connection operation
 *
 * @param <L>           left table element
 * @param <R>           right  table element
 *
 * @author  caizhihao
 */
@FunctionalInterface
public interface VoidJoin<L,R> {

    /**
     * Join Operation
     * @param left      If it is a right connection, it may be null
     * @param right     If it is a left connection, it may be null
     */
    void join(L left, R right);

}
