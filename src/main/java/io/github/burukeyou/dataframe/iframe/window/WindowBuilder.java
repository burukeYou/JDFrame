package io.github.burukeyou.dataframe.iframe.window;

import io.github.burukeyou.dataframe.iframe.window.round.Round;
import io.github.burukeyou.dataframe.iframe.window.round.WindowRound;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

/**
 * @author caizhiao
 */

public class WindowBuilder<T>  implements Window<T> {

    private List<Function<T,?>> groupBy;
    protected Sorter<T> sorter;

    private WindowRound startRound;

    private WindowRound endRound;

    public WindowBuilder() {
    }

    public WindowBuilder(Sorter<T> comparator) {
        this.sorter = comparator;
    }


    public WindowBuilder(List<Function<T, ?>> groupBy) {
        this.groupBy = groupBy;
    }

    public WindowBuilder(WindowRound startRound, WindowRound endRound) {
       roundBetween(startRound, endRound);
    }

    public void initDefault() {
        if (startRound == null){
            this.startRound =  Round.START_ROW;
        }

        if (endRound == null){
            if (sorter == null || sorter.getComparator() == null){
                this.endRound = Round.END_ROW;
            }else {
                this.endRound = Round.CURRENT_ROW;
            }
        }
    }

    @Override
    public Comparator<T> getComparator() {
        return sorter == null ? null : sorter.getComparator();
    }

    public WindowRound getStartRound() {
        return startRound;
    }

    public WindowRound getEndRound() {
        return endRound;
    }

    public List<Function<T, ?>> partitions() {
        return groupBy;
    }

    public <U extends Comparable<? super U>> Window<T> sortAsc(Function<T,U> sortField) {
        if (sorter == null){
            this.sorter = Sorter.sortAscBy(sortField);
        }else {
            sorter.sortAsc(sortField);
        }
        return this;
    }

    public <U extends Comparable<? super U>> Window<T> sortDesc(Function<T,U> sortField) {
        if (sorter == null){
            this.sorter = Sorter.sortDescBy(sortField);
        }else {
            sorter.sortDesc(sortField);
        }
        return this;
    }

    @Override
    public Window<T> sort(Comparator<T> comparator) {
        if (sorter == null){
            this.sorter = Sorter.toSorter(comparator);
        }else {
            sorter.sort(comparator);
        }
        return this;
    }

    @Override
    public Window<T> roundBetween(WindowRound start, WindowRound end) {
        if (Round.END_ROW.eq(start)){
            throw new IllegalArgumentException("The starting boundary param cannot be set to END_ROW");
        }
        if (Round.AFTER_ROW.eq(start)){
            throw new IllegalArgumentException("The starting boundary param cannot be set to AFTER_ROW");
        }

        if (Round.START_ROW.eq(end)){
            throw new IllegalArgumentException("The ending boundary param cannot be set to START_ROW");
        }

        if (Round.BEFORE_ROW.eq(end)){
            throw new IllegalArgumentException("The ending boundary param cannot be set to BEFORE_ROW");
        }

        start.check();
        end.check();

        this.startRound = start;
        this.endRound = end;
        return this;
    }

    @Override
    public Window<T> roundBefore2CurrentRow(int n) {
        roundBetween(Round.BEFORE(n),Round.CURRENT_ROW);
        return this;
    }

    @Override
    public Window<T> roundCurrentRow2After(int n) {
        roundBetween(Round.CURRENT_ROW,Round.AFTER(n));
        return this;
    }

    @Override
    public Window<T> roundCurrentRow2EndRow() {
        roundBetween(Round.CURRENT_ROW,Round.END_ROW);
        return this;
    }

    @Override
    public Window<T> roundStartRow2CurrentRow() {
        roundBetween(Round.START_ROW,Round.CURRENT_ROW);
        return this;
    }

    @Override
    public Window<T> roundAllRow() {
        roundBetween(Round.START_ROW,Round.END_ROW);
        return this;
    }
}
