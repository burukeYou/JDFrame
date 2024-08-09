package io.github.burukeyou.dataframe.iframe;


import io.github.burukeyou.dataframe.iframe.function.CompareTwo;

import java.util.Collection;
import java.util.Comparator;

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
     * union other frame, will delete duplicates
     * @param other              other frame
     * @param comparator         repetitive judgment comparator
     */
    SDFrame<T> union(IFrame<T> other, Comparator<T> comparator);

    /**
     * union other frame, will delete duplicates
     * @param other         other Collection
     */
    SDFrame<T> union(Collection<T> other);

    /**
     * union other frame, will delete duplicates
     * @param other         other Collection
     * @param comparator         repetitive judgment comparator
     */
    SDFrame<T> union(Collection<T> other, Comparator<T> comparator);

    /**
     * Retains only the elements in this list that are contained in the specified collection
     * @return           other frame
     */
    SDFrame<T> retainAll(IFrame<T> other);

    /**
     *  Retains only the elements in this list that are contained in the specified collection
     * @return                   other frame
     * @param comparator         repetitive judgment comparator
     */
    SDFrame<T> retainAll(IFrame<T> other,Comparator<T> comparator);

    /**
     * Retains only the elements in this list that are contained in the specified collection
     * @return           other collection
     */
    SDFrame<T> retainAll(Collection<T> other);

    /**
     * Retains only the elements in this list that are contained in the specified collection
     * @return                   other collection
     * @param comparator         repetitive judgment comparator
     */
    SDFrame<T> retainAll(Collection<T> other,Comparator<T> comparator);

    /**
     * Retains only the elements in this list that are contained in the specified collection
     * @return                   other collection
     * @param comparator         repetitive judgment comparator
     */
    <K> SDFrame<T> retainAllOther(Collection<K> other, CompareTwo<T,K> comparator);

    /**
     * intersection other frame
     *          get identical elements from two sets
     * @param other     other frame
     */
    SDFrame<T> intersection(IFrame<T> other);

    /**
     * intersection other frame
     *          get identical elements from two sets
     * @param other     other frame
     * @param comparator         repetitive judgment comparator
     */
    SDFrame<T> intersection(IFrame<T> other,Comparator<T> comparator);

    /**
     * intersection other collection
     *          get identical elements from two sets
     * @param other     other collection
     */
    SDFrame<T> intersection(Collection<T> other);

    /**
     * intersection other collection
     *          get identical elements from two sets
     * @param other     other collection
     * @param comparator         repetitive judgment comparator
     */
    SDFrame<T> intersection(Collection<T> other,Comparator<T> comparator);

    /**
     * different other frame
     *      Elements that are not within the other frame
     * @return           other frame
     */
    SDFrame<T> different(IFrame<T> other);


    /**
     * different other frame
     *      Elements that are not within the other frame
     * @return           other frame
     * @param comparator         repetitive judgment comparator
     */
    SDFrame<T> different(IFrame<T> other,Comparator<T> comparator);

    /**
     * different other collection
     *      Elements that are not within the other frame
     * @return           other collection
     */
    SDFrame<T> different(Collection<T> other);

    /**
     * different other collection
     *      Elements that are not within the other frame
     * @return           other collection
     * @param comparator         repetitive judgment comparator
     */
    SDFrame<T> different(Collection<T> other,Comparator<T> comparator);

    /**
     * different other collection
     *      Elements that are not within the other frame
     * @return                   other collection
     * @param comparator         repetitive judgment comparator
     */
    <K> SDFrame<T> differentOther(Collection<K> other, CompareTwo<T,K> comparator);

    /**
     * subtract other
     * @return           other
     */
    SDFrame<T> subtract(IFrame<T> other);

    /**
     * subtract other
     * @return           other
     */
    SDFrame<T> subtract(Collection<T> other);

    /**
     * subtract other
     * @return                   other
     * @param comparator         repetitive judgment comparator
     */
    SDFrame<T> subtract(IFrame<T> other,Comparator<T> comparator);

    /**
     * subtract other
     * @return                   other
     * @param comparator         repetitive judgment comparator
     */
    SDFrame<T> subtract(Collection<T> other,Comparator<T> comparator);
}
