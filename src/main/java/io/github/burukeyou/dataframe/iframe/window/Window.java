package io.github.burukeyou.dataframe.iframe.window;

import io.github.burukeyou.dataframe.iframe.window.round.Round;
import io.github.burukeyou.dataframe.iframe.window.round.WindowRound;

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

    static <T> Window<T> roundBetweenBy(WindowRound start, WindowRound end){
        return new WindowBuilder<>(start,end);
    }

    static <T> Window<T> roundBefore2CurrentRowBy(int n){
        return new WindowBuilder<>(Round.BEFORE(n),Round.CURRENT_ROW);
    }

    static <T> Window<T> roundCurrentRow2AfterBy(int n){
        return new WindowBuilder<>(Round.CURRENT_ROW,Round.AFTER(n));
    }

    static <T> Window<T> roundCurrentRow2EndRowBy(){
        return new WindowBuilder<>(Round.CURRENT_ROW,Round.END_ROW);
    }

    static <T>Window<T> roundStartRow2CurrentRowBy(){
        return new WindowBuilder<>(Round.START_ROW,Round.CURRENT_ROW);
    }

    static <T> Window<T> roundAllRowBy(){
        return new WindowBuilder<>(Round.START_ROW,Round.END_ROW);
    }

    <U extends Comparable<? super U>> Window<T> sortAsc(Function<T,U> sortField);

    <U extends Comparable<? super U>> Window<T> sortDesc(Function<T,U> sortField);

    Window<T> sort(Comparator<T> comparator);

    Window<T> roundBetween(WindowRound start, WindowRound end);

    Window<T> roundBefore2CurrentRow(int n);

    Window<T> roundCurrentRow2After(int n);

    Window<T> roundCurrentRow2EndRow();

    Window<T> roundStartRow2CurrentRow();

    Window<T> roundAllRow();

    List<Function<T, ?>> partitions();

    Comparator<T> getComparator();

    WindowRound getStartRound();

    WindowRound getEndRound();
}
