package io.github.burukeyou.dataframe.iframe;

/**
 *
 * @author      caizhihao
 * @param <T>
 */
public interface OperationJDFrame<T>  extends OperationIFrame<T> {

    /**
     * union other frame
     * @param other         other frame
     */
    JDFrame<T> unionAll(IFrame<T> other);

    /**
     * union frame,union other frame, will delete duplicates
     * @param other         other frame
     */
    JDFrame<T> union(IFrame<T> other);

    /**
     * intersection other frame
     *      retain elements that exist simultaneously in two frame
     * @return           other frame
     */
    JDFrame<T> intersection(IFrame<T> other);

    /**
     * different other frame
     *      Elements that are not within the other frame
     * @return           other frame
     */
    JDFrame<T> different(IFrame<T> other);
}
