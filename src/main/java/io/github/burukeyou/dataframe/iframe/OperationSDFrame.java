package io.github.burukeyou.dataframe.iframe;

import java.util.Collection;

/**
 *
 * @author      caizhihao
 * @param <T>
 */
public interface OperationSDFrame<T> extends OperationIFrame<T> {


    /**
     * union other frame
     * @param other         other frame
     */
    SDFrame<T> unionAll(IFrame<T> other);


    /**
     * union other Collection
     * @param other         other frame
     */
    SDFrame<T> unionAll(Collection<T> other);

    /**
     * union frame,union other frame, will delete duplicates
     * @param other         other frame
     */
    SDFrame<T> union(IFrame<T> other);

    /**
     * Retains only the elements in this list that are contained in the specified collection
     * @return           other frame
     */
    SDFrame<T> retainAll(IFrame<T> other);

    /**
     * Retains only the elements in this list that are contained in the specified collection
     * @return           other collection
     */
    SDFrame<T> retainAll(Collection<T> other);

    /**
     * intersection other frame
     *          get identical elements from two sets
     * @param other     other frame
     */
    SDFrame<T> intersection(IFrame<T> other);

    /**
     * intersection other collection
     *          get identical elements from two sets
     * @param other     other collection
     */
    SDFrame<T> intersection(Collection<T> other);

    /**
     * different other frame
     *      Elements that are not within the other frame
     * @return           other frame
     */
    SDFrame<T> different(IFrame<T> other);

    /**
     * different other collection
     *      Elements that are not within the other frame
     * @return           other collection
     */
    SDFrame<T> different(Collection<T> other);
}
