package io.github.burukeyou.dataframe.iframe.function;

/**
 *
 * @author               caizhizhao
 * @param <T>
 * @param <R>
 */
public interface NumberFunction<T,R extends Number> {

     /**
      * get number value
      */
     R apply(T value);

}
