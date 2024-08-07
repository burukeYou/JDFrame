package io.github.burukeyou.dataframe.iframe;

/**
 *
 * @author      caizhihao
 * @param <T>
 */
public interface OperationIFrame<T> {

    /**
     * union frame
     * @param other         other frame
     */
    IFrame<T> union(IFrame<T> other);

}
