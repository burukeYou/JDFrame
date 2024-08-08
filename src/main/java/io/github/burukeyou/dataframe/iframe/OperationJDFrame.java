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
     * Retains only the elements in this list that are contained in the specified collection
     * @return           other frame
     */
    JDFrame<T> retainAll(IFrame<T> other);

    /**
     * different other frame
     *      Elements that are not within the other frame
     * @return           other frame
     */
    JDFrame<T> different(IFrame<T> other);
}
