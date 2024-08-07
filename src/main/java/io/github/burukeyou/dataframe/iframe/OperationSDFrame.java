package io.github.burukeyou.dataframe.iframe;

/**
 *
 * @author      caizhihao
 * @param <T>
 */
public interface OperationSDFrame<T> extends OperationIFrame<T> {

    /**
     * union frame
     * @param other         other frame
     */
    SDFrame<T> union(IFrame<T> other);

    /**
     * intersection other frame
     *      retain elements that exist simultaneously in two frame
     * @return           other frame
     */
    SDFrame<T> intersection(IFrame<T> other);
}
