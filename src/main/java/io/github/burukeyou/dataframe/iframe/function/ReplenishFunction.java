package io.github.burukeyou.dataframe.iframe.function;

public interface ReplenishFunction<T1,T2, R> {
    R apply(T1 t, T2 t2);
}
