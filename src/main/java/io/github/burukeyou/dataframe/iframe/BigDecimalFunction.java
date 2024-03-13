package io.github.burukeyou.dataframe.iframe;

import java.math.BigDecimal;

@FunctionalInterface
public interface BigDecimalFunction<T> {
    BigDecimal applyAsBigDecimal(T value);

}