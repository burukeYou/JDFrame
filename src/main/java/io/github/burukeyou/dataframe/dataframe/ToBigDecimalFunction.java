package io.github.burukeyou.dataframe.dataframe;

import java.math.BigDecimal;

@FunctionalInterface
public interface ToBigDecimalFunction<T> {
    BigDecimal applyAsBigDecimal(T value);

}