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

    public WindowBuilder(Sorter<T> comparator) {
        this.sorter = comparator;
        initDefault();
    }


    public WindowBuilder(List<Function<T, ?>> groupBy) {
        this.groupBy = groupBy;
        initDefault();
    }

    public WindowBuilder(WindowRound startRound, WindowRound endRound) {
        this.startRound = startRound;
        this.endRound = endRound;
    }

    private void initDefault() {
        roundStartRow2CurrentRow();
    }

    @Override
    public Comparator<T> getComparator() {
        return sorter.getComparator();
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
        this.startRound = start;
        this.endRound = end;
        return this;
    }

    @Override
    public Window<T> roundBefore2CurrentRow(int n) {
        this.startRound = Round.BEFORE(n);
        this.endRound = Round.CURRENT_ROW;
        return this;
    }

    @Override
    public Window<T> roundCurrentRow2After(int n) {
        this.startRound = Round.CURRENT_ROW;
        this.endRound = Round.AFTER(n);
        return this;
    }

    @Override
    public Window<T> roundCurrentRow2EndRow() {
        this.startRound = Round.CURRENT_ROW;
        this.endRound = Round.END_ROW;
        return this;
    }

    @Override
    public Window<T> roundStartRow2CurrentRow() {
        this.startRound = Round.START_ROW;
        this.endRound = Round.CURRENT_ROW;
        return this;
    }

    @Override
    public Window<T> roundAllRow() {
        this.startRound = Round.START_ROW;
        this.endRound = Round.END_ROW;
        return null;
    }
}
