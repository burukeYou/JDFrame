package io.github.burukeyou.dataframe.iframe;


import io.github.burukeyou.dataframe.iframe.function.CompareTwo;

import java.util.Collection;
import java.util.Comparator;

/**
 *
 * @author      caizhihao
 * @param <T>
 */
public interface OperationIFrame<T> {

    /**
     * union other frame
     * @param other         other frame
     */
    IFrame<T> unionAll(IFrame<T> other);


    /**
     * union other Collection
     * @param other         other frame
     */
    IFrame<T> unionAll(Collection<T> other);

    /**
     * union other frame, will delete duplicates
     * @param other         other frame
     */
    IFrame<T> union(IFrame<T> other);

    /**
     * union other frame, will delete duplicates
     * @param other              other frame
     * @param comparator         repetitive judgment comparator
     */
    IFrame<T> union(IFrame<T> other, Comparator<T> comparator);

    /**
     * union other frame, will delete duplicates
     * @param other         other Collection
     */
    IFrame<T> union(Collection<T> other);

    /**
     * union other frame, will delete duplicates
     * @param other         other Collection
     * @param comparator         repetitive judgment comparator
     */
    IFrame<T> union(Collection<T> other, Comparator<T> comparator);

    /**
     *  Retains only the elements in this list that are contained in the specified collection
     * @return           other frame
     */
    IFrame<T> retainAll(IFrame<T> other);


    /**
     *  Retains only the elements in this list that are contained in the specified collection
     * @return                   other frame
     * @param comparator         repetitive judgment comparator
     */
    IFrame<T> retainAll(IFrame<T> other,Comparator<T> comparator);

    /**
     * Retains only the elements in this list that are contained in the specified collection
     * @return           other collection
     */
    IFrame<T> retainAll(Collection<T> other);


    /**
     * Retains only the elements in this list that are contained in the specified collection
     * @return                   other collection
     * @param comparator         repetitive judgment comparator
     */
    IFrame<T> retainAll(Collection<T> other,Comparator<T> comparator);

    /**
     * Retains only the elements in this list that are contained in the specified collection
     * @return                   other collection
     * @param comparator         repetitive judgment comparator
     */
    <K> IFrame<T> retainAllOther(Collection<K> other, CompareTwo<T,K> comparator);

    /**
     * intersection other frame
     *          get identical elements from two sets
     * @param other     other frame
     */
    IFrame<T> intersection(IFrame<T> other);

    /**
     * intersection other frame
     *          get identical elements from two sets
     * @param other     other frame
     * @param comparator         repetitive judgment comparator
     */
    IFrame<T> intersection(IFrame<T> other,Comparator<T> comparator);

    /**
     * intersection other collection
     *          get identical elements from two sets
     * @param other     other collection
     */
    IFrame<T> intersection(Collection<T> other);

    /**
     * intersection other collection
     *          get identical elements from two sets
     * @param other     other collection
     * @param comparator         repetitive judgment comparator
     */
    IFrame<T> intersection(Collection<T> other,Comparator<T> comparator);

    /**
     * different other frame
     *      Elements that are not within the other frame
     * @return           other frame
     */
    IFrame<T> different(IFrame<T> other);

    /**
     * different other frame
     *      Elements that are not within the other frame
     * @return           other frame
     * @param comparator         repetitive judgment comparator
     */
    IFrame<T> different(IFrame<T> other,Comparator<T> comparator);

    /**
     * different other collection
     *      Elements that are not within the other frame
     * @return           other collection
     */
    IFrame<T> different(Collection<T> other);

    /**
     * different other collection
     *      Elements that are not within the other frame
     * @return           other collection
     * @param comparator         repetitive judgment comparator
     */
    IFrame<T> different(Collection<T> other,Comparator<T> comparator);

    /**
     * different other collection
     *      Elements that are not within the other frame
     * @return                   other collection
     * @param comparator         repetitive judgment comparator
     */
    <K> IFrame<T> differentOther(Collection<K> other, CompareTwo<T,K> comparator);

    /**
     * subtract other
     * @return           other
     */
    IFrame<T> subtract(IFrame<T> other);

    /**
     * subtract other
     * @return           other
     */
    IFrame<T> subtract(Collection<T> other);

    /**
     * subtract other
     * @return                   other
     * @param comparator         repetitive judgment comparator
     */
    IFrame<T> subtract(IFrame<T> other,Comparator<T> comparator);

    /**
     * subtract other
     * @return                   other
     * @param comparator         repetitive judgment comparator
     */
    IFrame<T> subtract(Collection<T> other,Comparator<T> comparator);
}
