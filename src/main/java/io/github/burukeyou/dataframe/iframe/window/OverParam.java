package io.github.burukeyou.dataframe.iframe.window;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OverParam<T> {

    private List<Function<T,?>> partitionBy;
    private Comparator<T> comparator;
}
