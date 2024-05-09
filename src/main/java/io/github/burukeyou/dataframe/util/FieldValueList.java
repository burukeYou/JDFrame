package io.github.burukeyou.dataframe.util;

import java.util.List;
import java.util.function.Function;

/**
 * @author caizhihao
 *
 */
public class FieldValueList<T,F> {
    private final List<T> data;
    private final Function<T, F> field;

    public FieldValueList(List<T> data, Function<T, F> field) {
        this.data = data;
        this.field = field;
    }

    public F get(Integer index){
        if (index == null){
            return null;
        }
        return field.apply(data.get(index));
    }
}
