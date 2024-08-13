package io.github.burukeyou.dataframe.iframe.group;

import java.util.Comparator;
import java.util.function.Function;

/**
 * @author          caizhihao
 * @param <T>
 */
public class GroupConcatImpl<T> extends GroupWindowImpl<T> implements GroupConcat<T> {


    private Function<T,?> aggField;

    private CharSequence delimiter = ",";

    public GroupConcatImpl(Function<T, ?> aggField, CharSequence delimiter) {
        this.aggField = aggField;
        this.delimiter = delimiter;
    }

    @Override
    public <U extends Comparable<? super U>> GroupConcat<T> sortAsc(Function<T, U> sortField) {
        super.sortAsc(sortField);
        return this;
    }

    @Override
    public <U extends Comparable<? super U>> GroupConcat<T> sortDesc(Function<T, U> sortField) {
        super.sortDesc(sortField);
        return this;
    }

    @Override
    public GroupConcat<T> sort(Comparator<T> comparator) {
        super.sort(comparator);
        return this;
    }

    @Override
    public <U extends Comparable<? super U>> GroupWindow<T> distinct(Function<T, U> distinctField) {
        super.distinct(distinctField);
        return this;
    }
}
