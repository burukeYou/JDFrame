package io.github.burukeyou.dataframe.iframe;

/**
 *
 * @author      caizhihao
 * @param <T>
 */
public interface OperationJDFrame<T>  extends OperationIFrame<T> {

    /**
     * union frame
     * @param other         other frame
     */
    JDFrame<T> union(IFrame<T> other);

    /**
     * intersection other frame
     *      retain elements that exist simultaneously in two frame
     * @return           other frame
     */
    JDFrame<T> intersection(IFrame<T> other);
}
