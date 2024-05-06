package io.github.burukeyou.dataframe.iframe.window;

import java.util.Comparator;
import java.util.function.Function;

/**
 *
 * @author caihzihao
 * @param <T>
 */
public interface Sorter<T> extends Comparator<T> {

    Comparator<T> getComparator();

    static <T,U extends Comparable<? super U>> Sorter<T> sortAscBy(Function<T,U> sortField){
        return new SorterBuilder<>(Comparator.comparing(sortField));
    }

    static <T,U extends Comparable<? super U>> Sorter<T> sortDescBy(Function<T,U> sortField){
        return new SorterBuilder<>(Comparator.comparing(sortField).reversed());
    }

    static <T> Sorter<T> toSorter(Comparator<T> comparator){
        return new SorterBuilder<>(comparator);
    }

    <U extends Comparable<? super U>> Sorter<T> sortAsc(Function<T,U> sortField);

    <U extends Comparable<? super U>> Sorter<T> sortDesc(Function<T,U> sortField);

    Sorter<T> sort(Comparator<T> comparator);


}
