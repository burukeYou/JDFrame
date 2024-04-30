package io.github.burukeyou.dataframe.iframe.window;

import lombok.Getter;

import java.util.Comparator;
import java.util.function.Function;

/**
 * @author caizhiao
 */

@Getter
public class SorterBuilder<T> implements  Sorter<T> {

    protected Comparator<T> comparator;

    public SorterBuilder(Comparator<T> comparator) {
        this.comparator = comparator;
    }


    public <U extends Comparable<? super U>> Sorter<T> sortAsc(Function<T,U> sortField) {
        if (this.comparator == null){
            this.comparator = Comparator.comparing(sortField);
        }else {
            this.comparator.thenComparing(Comparator.comparing(sortField));
        }
        return this;
    }


    public <U extends Comparable<? super U>> Sorter<T> sortDesc(Function<T,U> sortField) {
        if (this.comparator == null){
            this.comparator = Comparator.comparing(sortField).reversed();
        }else {
            this.comparator.thenComparing(Comparator.comparing(sortField).reversed());
        }
        return this;
    }



}
