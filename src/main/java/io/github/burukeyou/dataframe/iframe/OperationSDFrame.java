package io.github.burukeyou.dataframe.iframe;

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
     * different other frame
     *      Elements that are not within the other frame
     * @return           other frame
     */
    SDFrame<T> different(IFrame<T> other);
}
