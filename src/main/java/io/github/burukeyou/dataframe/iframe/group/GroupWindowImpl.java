package io.github.burukeyou.dataframe.iframe.group;

import io.github.burukeyou.dataframe.iframe.window.Sorter;
import lombok.Data;

import java.util.Comparator;
import java.util.function.Function;

/**
 * @author          caizhihao
 * @param <T>
 */

@Data
public class GroupWindowImpl<T> implements GroupWindow<T> {

    protected Comparator<T> distincter;

    protected Sorter<T> sorter;


    public GroupWindowImpl(Comparator<T> distincter) {
        this.distincter = distincter;
    }

    public GroupWindowImpl(Sorter<T> sorter) {
        this.sorter = sorter;
    }

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
    public <U extends Comparable<? super U>> GroupWindow<T> distinct(Function<T, U> distinctField) {
        if (distincter == null){
            distincter = Comparator.comparing(distinctField);
        }else {
            distincter = distincter.thenComparing(distinctField);
        }
        return this;
    }

    @Override
    public GroupWindow<T> distinct(Comparator<T> distinctComparator) {
        if (distincter == null){
            distincter = distinctComparator;
        }else {
            distincter = distincter.thenComparing(distinctComparator);
        }
        return this;
    }

    public Comparator<T> getDistincter() {
        return distincter;
    }

    public Sorter<T> getSorter() {
        return sorter;
    }
}
