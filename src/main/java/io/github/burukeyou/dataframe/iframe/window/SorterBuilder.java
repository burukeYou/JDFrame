package io.github.burukeyou.dataframe.iframe.window;

import lombok.Getter;

import java.util.Comparator;
import java.util.function.Function;

/**
 * @author caizhiao
 */

@Getter
public class SorterBuilder<T> implements Sorter<T> {

    protected Comparator<T> comparator;

    public SorterBuilder(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    public <U extends Comparable<? super U>> Sorter<T> sortAsc(Function<T,U> sortField) {
        if (this.comparator == null){
            this.comparator = Comparator.comparing(sortField);
        }else {
            this.comparator = this.comparator.thenComparing(sortField);
        }
        return this;
    }


    public <U extends Comparable<? super U>> Sorter<T> sortDesc(Function<T,U> sortField) {
        if (this.comparator == null){
            this.comparator = Comparator.comparing(sortField).reversed();
        }else {
            this.comparator = this.comparator.thenComparing(Comparator.comparing(sortField).reversed());
        }
        return this;
    }

    @Override
    public Sorter<T> sort(Comparator<T> comparator) {
        if (this.comparator == null){
            this.comparator = comparator;
        }else {
            this.comparator = this.comparator.thenComparing(comparator);
        }
        return this;
    }

    @Override
    public int compare(T o1, T o2) {
        return comparator.compare(o1,o2);
    }
}
