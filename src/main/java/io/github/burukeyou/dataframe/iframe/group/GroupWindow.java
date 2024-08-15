package io.github.burukeyou.dataframe.iframe.group;

import io.github.burukeyou.dataframe.iframe.window.Sorter;

import java.util.Comparator;
import java.util.function.Function;

/**
 * @author          caizhihao
 * @param <T>
 */
public interface GroupWindow<T> {

    /**
     * open a GroupWindow by ascending sort
     * @param sortField                sort fields
     */
    static <T,U extends Comparable<? super U>> GroupWindow<T> sortAscBy(Function<T,U> sortField){
        return new GroupWindowImpl<>(Sorter.sortAscBy(sortField));
    }

    /**
     * open a GroupWindow by descending sort
     * @param sortField                sort fields
     */
    static <T,U extends Comparable<? super U>> GroupWindow<T> sortDescBy(Function<T,U> sortField){
        return new GroupWindowImpl<>(Sorter.sortDescBy(sortField));
    }

    /**
     * open a GroupWindow by  sort
     */
    static <T,U extends Comparable<? super U>> GroupWindow<T> sortBy(Comparator<T> comparator){
        return new GroupWindowImpl<>(Sorter.toSorter(comparator));
    }



    /**
     * open a GroupWindow by distinct
     */
    static <T,U extends Comparable<? super U>> GroupWindow<T> distinctBy(Function<T,U> sortField){
        return new GroupWindowImpl<>(Comparator.comparing(sortField));
    }

    /**
     * open a GroupWindow by distinct
     */
    static <T,U extends Comparable<? super U>> GroupWindow<T> distinctBy(Comparator<T> distinctComparator){
        return new GroupWindowImpl<>(distinctComparator);
    }


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
     * distinct by field value
     * @param distinctField             distinct field
     */
    <U extends Comparable<? super U>> GroupWindow<T> distinct(Function<T,U> distinctField);

    /**
     * distinct by Comparator
     * @param distinctComparator             distinct Comparator
     */
    GroupWindow<T> distinct(Comparator<T> distinctComparator);

    Sorter<T> getSorter();

    Comparator<T> getDistincter();

}
