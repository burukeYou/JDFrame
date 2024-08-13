package io.github.burukeyou.dataframe.iframe.group;

import io.github.burukeyou.dataframe.iframe.window.Sorter;

import java.util.Comparator;
import java.util.function.Function;

/**
 * @author          caizhihao
 * @param <T>
 */
public class GroupWindowImpl<T> implements GroupWindow<T> {

    protected Comparator<T> distincter;

    protected Sorter<T> sorter;

    @Override
    public <U extends Comparable<? super U>> GroupWindow<T> sortAsc(Function<T, U> sortField) {
        if (sorter == null){
            this.sorter = Sorter.sortAscBy(sortField);
        }else {
            sorter.sortAsc(sortField);
        }
        return this;
    }

    @Override
    public <U extends Comparable<? super U>> GroupWindow<T> sortDesc(Function<T, U> sortField) {
        if (sorter == null){
            this.sorter = Sorter.sortDescBy(sortField);
        }else {
            sorter.sortDesc(sortField);
        }
        return this;
    }

    @Override
    public GroupWindow<T> sort(Comparator<T> comparator) {
        if (sorter == null){
            this.sorter = Sorter.toSorter(comparator);
        }else {
            sorter.sort(comparator);
        }
        return this;
    }

    @Override
    public <U extends Comparable<? super U>> GroupWindow<T> distinct(Function<T, U> distinctField) {
        if (distincter == null){
            distincter = Comparator.comparing(distinctField);
        }else {
            distincter = distincter.thenComparing(distinctField);
        }
        return this;
    }
}
