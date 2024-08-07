package io.github.burukeyou.dataframe.iframe;

/**
 *
 * @author      caizhihao
 * @param <T>
 */
public interface OperationIFrame<T> {

    /**
     * union other frame
     * @param other         other frame
     */
    IFrame<T> union(IFrame<T> other);

    /**
     * intersection other frame
     *      retain elements that exist simultaneously in two frame
     * @return           other frame
     */
    IFrame<T> intersection(IFrame<T> other);
}
