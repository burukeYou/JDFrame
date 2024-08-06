package io.github.burukeyou.dataframe.iframe.support;

/**
 * Custom Connection operation to return a new Element
 *
 * @param <L>        left table element
 * @param <R>        right  table element
 * @param <V>        new element
 *
 * @author  caizhihao
 */
public interface Join<L,R,V> {

    /**
     * Join Operation
     * @param left     left table element,  If it is a right connection, it may be null
     * @param right    right table element,  If it is a left connection, it may be null
     * @return         Build a new element with left and right element
     */
    V join(L left, R right);
}
