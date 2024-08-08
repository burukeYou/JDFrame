package io.github.burukeyou.dataframe.iframe;

import java.util.Collection;

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
     * union other Collection
     * @param other         other frame
     */
    JDFrame<T> unionAll(Collection<T> other);

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
     * Retains only the elements in this list that are contained in the specified collection
     * @return           other collection
     */
    JDFrame<T> retainAll(Collection<T> other);

    /**
     * intersection other frame
     *          get identical elements from two sets
     * @param other     other frame
     */
    JDFrame<T> intersection(IFrame<T> other);

    /**
     * intersection other collection
     *          get identical elements from two sets
     * @param other     other collection
     */
    JDFrame<T> intersection(Collection<T> other);

    /**
     * different other frame
     *      Elements that are not within the other frame
     * @return           other frame
     */
    JDFrame<T> different(IFrame<T> other);

    /**
     * different other collection
     *      Elements that are not within the other frame
     * @return           other collection
     */
    JDFrame<T> different(Collection<T> other);
}
