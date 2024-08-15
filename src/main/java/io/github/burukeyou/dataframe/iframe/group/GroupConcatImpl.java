package io.github.burukeyou.dataframe.iframe.group;

import lombok.Data;

import java.util.function.Function;

/**
 * @author          caizhihao
 * @param <T>
 */
@Data
public class GroupConcatImpl<T> implements GroupConcat<T> {

    private Function<T,?> aggField;

    private CharSequence delimiter = ",";

    private CharSequence prefix = "";

    private CharSequence suffix = "";

    public GroupConcatImpl(Function<T, ?> aggField, CharSequence delimiter) {
        this.aggField = aggField;
        this.delimiter = delimiter;
    }

    public GroupConcatImpl(Function<T, ?> aggField, CharSequence delimiter, CharSequence prefix, CharSequence suffix) {
        this.aggField = aggField;
        this.delimiter = delimiter;
        this.prefix = prefix;
        this.suffix = suffix;
    }
}
