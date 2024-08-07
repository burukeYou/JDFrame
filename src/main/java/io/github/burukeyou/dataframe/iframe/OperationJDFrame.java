package io.github.burukeyou.dataframe.iframe;

/**
 *
 * @author      caizhihao
 * @param <T>
 */
public interface OperationJDFrame<T> {

    /**
     * union frame
     * @param other         other frame
     */
    JDFrame<T> union(IFrame<T> other);

}
