package io.github.burukeyou.dataframe.iframe.window;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

/**
 * @author caizhiao
 */
public interface Window<T>  {

    @SafeVarargs
    static <T> Window<T> groupBy(Function<T,?>...groupField){
        return new WindowBuilder<>(Arrays.asList(groupField));
    }

    static <T,U extends Comparable<? super U>> Window<T> sortAscBy(Function<T,U> sortField){
        return new WindowBuilder<>(Sorter.sortAscBy(sortField));
    }

    static <T,U extends Comparable<? super U>> Window<T> sortDescBy(Function<T,U> sortField){
        return new WindowBuilder<>(Sorter.sortDescBy(sortField));
    }

    static <T> Window<T> sortBy(Comparator<T> comparator){
        return new WindowBuilder<>(Sorter.toSorter(comparator));
    }

    <U extends Comparable<? super U>> Window<T> sortAsc(Function<T,U> sortField);

    <U extends Comparable<? super U>> Window<T> sortDesc(Function<T,U> sortField);

    Window<T> sort(Comparator<T> comparator);

    List<Function<T, ?>> partitions();

    Sorter<T> getComparator();

}
