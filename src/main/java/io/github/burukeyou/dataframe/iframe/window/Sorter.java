package io.github.burukeyou.dataframe.iframe.window;

import java.util.Comparator;
import java.util.function.Function;

public interface Sorter<T> {

    Comparator<T> getComparator();

    static <T,U extends Comparable<? super U>> Sorter<T> sortAscBy(Function<T,U> sortField){
        return new SorterBuilder<>(Comparator.comparing(sortField));
    }

    static <T,U extends Comparable<? super U>> Sorter<T> sortDescBy(Function<T,U> sortField){
        return new SorterBuilder<>(Comparator.comparing(sortField).reversed());
    }

    <U extends Comparable<? super U>> Sorter<T> sortAsc(Function<T,U> sortField);

    <U extends Comparable<? super U>> Sorter<T> sortDesc(Function<T,U> sortField);


}
