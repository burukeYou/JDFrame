package io.github.burukeyou.dataframe.iframe;


import io.github.burukeyou.dataframe.iframe.function.CompareTwo;

import java.util.Collection;
import java.util.Comparator;

/**
 *
 * @author      caizhihao
 * @param <T>
 */
public interface IOperationJDFrame<T>  extends IOperationFrame<T> {

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
     * union other frame, will delete duplicates
     * @param other              other frame
     * @param comparator         repetitive judgment comparator
     */
    JDFrame<T> union(IFrame<T> other,  Comparator<T> comparator);

    /**
     * union other frame, will delete duplicates
     * @param other         other Collection
     */
    JDFrame<T> union(Collection<T> other);

    /**
     * union other frame, will delete duplicates
     * @param other         other Collection
     * @param comparator         repetitive judgment comparator
     */
    JDFrame<T> union(Collection<T> other, Comparator<T> comparator);

    /**
     * Retains only the elements in this list that are contained in the specified collection
     * @return           other frame
     */
    JDFrame<T> retainAll(IFrame<T> other);

    /**
     *  Retains only the elements in this list that are contained in the specified collection
     * @return                   other frame
     * @param comparator         repetitive judgment comparator
     */
    JDFrame<T> retainAll(IFrame<T> other,Comparator<T> comparator);

    /**
     * Retains only the elements in this list that are contained in the specified collection
     * @return           other collection
     */
    JDFrame<T> retainAll(Collection<T> other);

    /**
     * Retains only the elements in this list that are contained in the specified collection
     * @return                   other collection
     * @param comparator         repetitive judgment comparator
     */
    JDFrame<T> retainAll(Collection<T> other,Comparator<T> comparator);

    /**
     * Retains only the elements in this list that are contained in the specified collection
     * @return                   other collection
     * @param comparator         repetitive judgment comparator
     */
    <K> JDFrame<T> retainAllOther(Collection<K> other, CompareTwo<T,K> comparator);

    /**
     * intersection other frame
     *          get identical elements from two sets
     * @param other     other frame
     */
    JDFrame<T> intersection(IFrame<T> other);

    /**
     * intersection other frame
     *          get identical elements from two sets
     * @param other     other frame
     * @param comparator         repetitive judgment comparator
     */
    JDFrame<T> intersection(IFrame<T> other,Comparator<T> comparator);

    /**
     * intersection other collection
     *          get identical elements from two sets
     * @param other     other collection
     */
    JDFrame<T> intersection(Collection<T> other);

    /**
     * intersection other collection
     *          get identical elements from two sets
     * @param other     other collection
     * @param comparator         repetitive judgment comparator
     */
    JDFrame<T> intersection(Collection<T> other,Comparator<T> comparator);

    /**
     * different other frame
     *      Elements that are not within the other frame
     * @return           other frame
     */
    JDFrame<T> different(IFrame<T> other);

    /**
     * different other frame
     *      Elements that are not within the other frame
     * @return           other frame
     * @param comparator         repetitive judgment comparator
     */
    JDFrame<T> different(IFrame<T> other,Comparator<T> comparator);

    /**
     * different other collection
     *      Elements that are not within the other frame
     * @return           other collection
     */
    JDFrame<T> different(Collection<T> other);


    /**
     * different other collection
     *      Elements that are not within the other frame
     * @return           other collection
     * @param comparator         repetitive judgment comparator
     */
    JDFrame<T> different(Collection<T> other,Comparator<T> comparator);

    /**
     * different other collection
     *      Elements that are not within the other frame
     * @return                   other collection
     * @param comparator         repetitive judgment comparator
     */
    <K> JDFrame<T> differentOther(Collection<K> other, CompareTwo<T,K> comparator);

    /**
     * subtract other
     * @return           other
     */
    JDFrame<T> subtract(IFrame<T> other);

    /**
     * subtract other
     * @return           other
     */
    JDFrame<T> subtract(Collection<T> other);

    /**
     * subtract other
     * @return                   other
     * @param comparator         repetitive judgment comparator
     */
    JDFrame<T> subtract(IFrame<T> other,Comparator<T> comparator);

    /**
     * subtract other
     * @return                   other
     * @param comparator         repetitive judgment comparator
     */
    JDFrame<T> subtract(Collection<T> other,Comparator<T> comparator);
}
