package io.github.burukeyou.dataframe.iframe.window.param;

import io.github.burukeyou.dataframe.iframe.window.OverEnum;
import lombok.Data;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

@Data
public class BaseWindowParam<T,V> {

    private List<T> windowList;
    private Comparator<T> comparator;
    private Class<V> resultType;
    private List<Function<T,?>> partitionBy;
    private OverEnum overEnum;
//
//    private Function<T, F> field;
//    private int n;
}
