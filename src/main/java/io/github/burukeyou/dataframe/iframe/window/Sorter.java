package io.github.burukeyou.dataframe.iframe.window;

import java.util.Comparator;
import java.util.function.Function;

/**
 * multi level Sort Builder
 *      enhance Comparator to make it easier for us to build multi level sorting logic
 * @author caihzihao
 * @param <T>
 */
public interface Sorter<T> extends Comparator<T> {

    Comparator<T> getComparator();

    /**
     * Sort in ascending order based on specified fields
     * @param sortField             sort field
     */
    static <T,U extends Comparable<? super U>> Sorter<T> sortAscBy(Function<T,U> sortField){
        return new SorterBuilder<>(Comparator.comparing(sortField));
    }


    /**
     * Sort in descending order based on specified fields
     * @param sortField             sort field
     */
    static <T,U extends Comparable<? super U>> Sorter<T> sortDescBy(Function<T,U> sortField){
        return new SorterBuilder<>(Comparator.comparing(sortField).reversed());
    }

    /**
     * convert comparator to a Sorter
     */
    static <T> Sorter<T> toSorter(Comparator<T> comparator){
        return new SorterBuilder<>(comparator);
    }

    /**
     * Sort in ascending order based on specified fields
     * @param sortField             sort field
     */
    <U extends Comparable<? super U>> Sorter<T> sortAsc(Function<T,U> sortField);

    /**
     * Sort in descending order based on specified fields
     * @param sortField             sort field
     */
    <U extends Comparable<? super U>> Sorter<T> sortDesc(Function<T,U> sortField);


    /**
     * Sort by Comparator
     */
    Sorter<T> sort(Comparator<T> comparator);


}
