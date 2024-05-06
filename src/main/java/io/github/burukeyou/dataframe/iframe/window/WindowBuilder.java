package io.github.burukeyou.dataframe.iframe.window;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

/**
 * @author caizhiao
 */

public class WindowBuilder<T>  implements Window<T> {

    private List<Function<T,?>> groupBy;
    protected Sorter<T> sorter;

    public WindowBuilder(Sorter<T> comparator) {
        this.sorter = comparator;
    }

    public WindowBuilder(List<Function<T, ?>> groupBy) {
        this.groupBy = groupBy;
    }


    @Override
    public Sorter<T> getComparator() {
        return sorter;
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
}
