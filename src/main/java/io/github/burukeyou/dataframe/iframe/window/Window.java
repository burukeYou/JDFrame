package io.github.burukeyou.dataframe.iframe.window;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

/**
 * @author caizhiao
 */
public interface Window<T>  {

    List<Function<T, ?>> getGroupBy();

    Comparator<T> getComparator();

    static <T> Window<T> groupBy(Function<T,?>...groupField){
        return new WindowBuilder<>(Arrays.asList(groupField),null);
    }

    static <T,U extends Comparable<? super U>> Window<T> sortAscBy(Function<T,U> sortField){
        return new WindowBuilder<>(Comparator.comparing(sortField));
    }

    static <T,U extends Comparable<? super U>> Window<T> sortDescBy(Function<T,U> sortField){
        return new WindowBuilder<>(Comparator.comparing(sortField).reversed());
    }

    <U extends Comparable<? super U>> Window<T> sortAsc(Function<T,U> sortField);

    <U extends Comparable<? super U>> Window<T> sortDesc(Function<T,U> sortField);


}
