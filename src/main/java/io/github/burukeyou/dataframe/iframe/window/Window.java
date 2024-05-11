package io.github.burukeyou.dataframe.iframe.window;

import io.github.burukeyou.dataframe.iframe.window.round.Range;
import io.github.burukeyou.dataframe.iframe.window.round.WindowRange;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

/**
 *  Window building tool
 *       can specify window partition information, window range, and window sorting
 *
 * @author caizhiao
 */
public interface Window<T>  {

    /**
     * open a Window by partitioning based on provided fields
     * @param groupField                group fields
     */
    @SafeVarargs
    static <T> Window<T> groupBy(Function<T,?>...groupField){
        return new WindowBuilder<>(Arrays.asList(groupField));
    }

    /**
     * open a Window by ascending sort
     * @param sortField                sort fields
     */
    static <T,U extends Comparable<? super U>> Window<T> sortAscBy(Function<T,U> sortField){
        return new WindowBuilder<>(Sorter.sortAscBy(sortField));
    }

    /**
     *  open a Window by descending order
     * @param sortField                  sort fields
     */
    static <T,U extends Comparable<? super U>> Window<T> sortDescBy(Function<T,U> sortField){
        return new WindowBuilder<>(Sorter.sortDescBy(sortField));
    }

    /**
     * open a Window by Comparator
     * @param comparator                  window sort  comparator
     */
    static <T> Window<T> sortBy(Comparator<T> comparator){
        return new WindowBuilder<>(Sorter.toSorter(comparator));
    }

    /**
     * open a Window by Sorter
     * @param sorter                  window sorter
     */
    static <T> Window<T> sortBy(Sorter<T> sorter){
        return new WindowBuilder<>(sorter);
    }

    /**
     * open a Window by window range
     *      this range is form start to end
     * @param start                 start range
     * @param end                   end range
     */
    static <T> Window<T> roundBetweenBy(WindowRange start, WindowRange end){
        return new WindowBuilder<>(start,end);
    }

    /**
     * open a Window by window range
     *      this range is form the first n lines of the current row to the current row
     * @param n                     The first n lines of the current row
     */
    static <T> Window<T> roundBefore2CurrentRowBy(int n){
        return new WindowBuilder<>(Range.BEFORE(n), Range.CURRENT_ROW);
    }

    /**
     * open a Window by window range
     *      this range is form the current row to   the last n row of the current row
     * @param n                     the last n row of the current row
     */
    static <T> Window<T> roundCurrentRow2AfterBy(int n){
        return new WindowBuilder<>(Range.CURRENT_ROW, Range.AFTER(n));
    }

    /**
     * open a Window by window range
     *      this range is form the current row to the end row
     */
    static <T> Window<T> roundCurrentRow2EndRowBy(){
        return new WindowBuilder<>(Range.CURRENT_ROW, Range.END_ROW);
    }

    /**
     * open a Window by window range
     *      this range is form the start row to the current row
     */
    static <T>Window<T> roundStartRow2CurrentRowBy(){
        return new WindowBuilder<>(Range.START_ROW, Range.CURRENT_ROW);
    }

    /**
     * open a Window by window range
     *      this range is window all row
     */
    static <T> Window<T> roundAllRowBy(){
        return new WindowBuilder<>(Range.START_ROW, Range.END_ROW);
    }

    /**
     * open a Window by window range
     *      this range is from before n row of current row  to  after n row of current row
     * @param before                         before n row of current row
     * @param after                          after n row of current row
     */
    static <T> Window<T> roundBeforeAfterBy(int before, int after){
        return new WindowBuilder<>(Range.BEFORE(before), Range.AFTER(after));
    }

    /**
     * Sort windows in ascending order according to specified fields
     * @param sortField                 sort field
     */
    <U extends Comparable<? super U>> Window<T> sortAsc(Function<T,U> sortField);

    /**
     * Sort windows in descending order according to specified fields
     * @param sortField          sort field
     */
    <U extends Comparable<? super U>> Window<T> sortDesc(Function<T,U> sortField);

    /**
     *  sort window by  Comparator
     * @param comparator        sort comparator
     */
    Window<T> sort(Comparator<T> comparator);

    /**
     * Specify window range
     * @param start              window start range
     * @param end                window end range
     */
    Window<T> roundBetween(WindowRange start, WindowRange end);


    /**
     * Specify window range
     *      this range is form the first n lines of the current row to the current row
     * @param n                     The first n lines of the current row
     */
    Window<T> roundBefore2CurrentRow(int n);

    /**
     * Specify window range
     *      this range is form the current row to   the last n row of the current row
     * @param n                     the last n row of the current row
     */
    Window<T> roundCurrentRow2After(int n);

    /**
     * Specify window range
     *      this range is form the current row to the end row
     */
    Window<T> roundCurrentRow2EndRow();

    /**
     * Specify window range
     *      this range is form the start row to the current row
     */
    Window<T> roundStartRow2CurrentRow();


    /**
     * Specify window range
     *      this range is window all row
     */
    Window<T> roundAllRow();

    /**
     * Specify window range
     *      this range is from before n row of current row  to  after n row of current row
     * @param before                         before n row of current row
     * @param after                          after n row of current row
     */
    Window<T> roundBeforeAfter(int before, int after);

    /**
     * get partitions info
     */
    List<Function<T, ?>> partitions();

    /**
     *   get window comparator
     */
    Comparator<T> getComparator();

    /**
     * get window start range
     */
    WindowRange getStartRange();

    /**
     * get window end range
     */
    WindowRange getEndRange();
}
