package io.github.burukeyou.dataframe.iframe.function;

import java.math.BigDecimal;

@FunctionalInterface
public interface BigDecimalFunction<T> {
    BigDecimal applyAsBigDecimal(T value);

}