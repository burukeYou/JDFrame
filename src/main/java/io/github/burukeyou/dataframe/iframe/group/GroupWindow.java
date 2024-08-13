package io.github.burukeyou.dataframe.iframe.group;

import java.util.Comparator;
import java.util.function.Function;

/**
 * @author          caizhihao
 * @param <T>
 */
public interface GroupWindow<T> {

    /**
     * Sort windows in ascending order according to specified fields
     * @param sortField                 sort field
     */
    <U extends Comparable<? super U>> GroupWindow<T> sortAsc(Function<T,U> sortField);

    /**
     * Sort windows in descending order according to specified fields
     * @param sortField          sort field
     */
    <U extends Comparable<? super U>> GroupWindow<T> sortDesc(Function<T,U> sortField);

    /**
     *  sort window by  Comparator
     * @param comparator        sort comparator
     */
    GroupWindow<T> sort(Comparator<T> comparator);


    <U extends Comparable<? super U>> GroupWindow<T> distinct(Function<T,U> distinctField);
}
