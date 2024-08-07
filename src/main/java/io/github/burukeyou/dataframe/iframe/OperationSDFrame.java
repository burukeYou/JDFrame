package io.github.burukeyou.dataframe.iframe;

/**
 *
 * @author      caizhihao
 * @param <T>
 */
public interface OperationSDFrame<T> {

    /**
     * union frame
     * @param other         other frame
     */
    SDFrame<T> union(IFrame<T> other);

}
