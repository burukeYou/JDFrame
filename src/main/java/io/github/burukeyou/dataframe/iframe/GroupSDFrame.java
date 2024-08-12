package io.github.burukeyou.dataframe.iframe;

import io.github.burukeyou.dataframe.iframe.function.NumberFunction;
import io.github.burukeyou.dataframe.iframe.item.FI2;
import io.github.burukeyou.dataframe.iframe.item.FI3;
import io.github.burukeyou.dataframe.iframe.item.FI4;
import io.github.burukeyou.dataframe.iframe.support.MaxMin;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

/**
 * @author      caizhihao
 * @param <T>
 */
public interface GroupSDFrame<T>  extends GroupIFrame<T> {

    /**
     * Group list
     * @param key        group field
     */
    <K> SDFrame<FI2<K, List<T>>> group(Function<? super T, ? extends K> key);

    /**
     * Group list
     * @param key        group field
     * @param key2       secondary level group field
     */
    <K,J> SDFrame<FI3<K, J,List<T>>> group(Function<T, K> key,Function<T,J> key2);


    /**
     * Group list
     * @param key        group field
     * @param key2       secondary level group field
     * @param key3      third level group field
     */
    <K,J,H> SDFrame<FI4<K, J,H,List<T>>> group(Function<T, K> key,Function<T,J> key2,Function<T,H> key3);


    /**
     * Group summation
     * @param key       group field
     * @param value     Aggregated field
     */
    <K,R extends Number> SDFrame<FI2<K, BigDecimal>> groupBySum(Function<T, K> key, NumberFunction<T,R> value);

    /**
     * Group summation
     * @param key       group field
     * @param key2      secondary level group field
     * @param value     Aggregated field
     */
    <K, J,R extends Number> SDFrame<FI3<K, J, BigDecimal>> groupBySum(Function<T, K> key, Function<T, J> key2, NumberFunction<T,R> value);

    /**
     * Group summation
     *
     * @param key     group field
     * @param key2    secondary level group field
     * @param key3    third level group field
     * @param value   Aggregated field
     */
    <K, J, H,R extends Number> SDFrame<FI4<K, J, H, BigDecimal>> groupBySum(Function<T, K> key,
                                                                            Function<T, J> key2,
                                                                            Function<T, H> key3,
                                                                            NumberFunction<T,R> value);

    /**
     * Group count
     * @param key       group field
     */
    <K> SDFrame<FI2<K, Long>> groupByCount(Function<T, K> key);

    /**
     * Group count
     * @param key       group field
     * @param key2      secondary level group field
     */
    <K, J> SDFrame<FI3<K, J, Long>> groupByCount(Function<T, K> key, Function<T, J> key2);

    /**
     * Group count
     *
     * @param key     group field
     * @param key2    secondary level group field
     * @param key3    third level group field
     */
    <K, J, H> SDFrame<FI4<K, J, H, Long>> groupByCount(Function<T, K> key, Function<T, J> key2, Function<T, H> key3);

    /**
     * Group sum and count together
     *
     * @param key           group field
     * @param value         Aggregated field
     * @return              FItem3(key, Sum, Count)
     */
    <K,R extends Number> SDFrame<FI3<K, BigDecimal,Long>> groupBySumCount(Function<T, K> key, NumberFunction<T,R> value);

    /**
     * Group sum and count together
     *
     * @param key           group field
     * @param key2          secondary level group field
     * @param value         Aggregated field
     * @return              FItem4(key, ke2,Sum, Count)
     */
    <K, J,R extends Number> SDFrame<FI4<K, J, BigDecimal, Long>> groupBySumCount(Function<T, K> key, Function<T, J> key2, NumberFunction<T,R> value);


    /**
     * Group average
     * @param key       group field
     * @param value     Aggregated field
     */
    <K,R extends Number> SDFrame<FI2<K, BigDecimal>> groupByAvg(Function<T, K> key, NumberFunction<T,R> value) ;

    /**
     * Group average
     * @param key       group field
     * @param key2      secondary level group field
     * @param value     Aggregated field
     */
    <K, J,R extends Number> SDFrame<FI3<K, J, BigDecimal>> groupByAvg(Function<T, K> key, Function<T, J> key2, NumberFunction<T,R> value);

    /**
     * Group average
     * @param key       group field
     * @param key2      secondary level group field
     * @param key3      third level group field
     * @param value     Aggregated field
     */
    <K, J, H,R extends Number> SDFrame<FI4<K, J, H, BigDecimal>> groupByAvg(Function<T, K> key,
                                                                            Function<T, J> key2,
                                                                            Function<T, H> key3,
                                                                            NumberFunction<T,R> value) ;

    /**
     * Group max
     * @param key       group field
     * @param value     Aggregated field
     */
    <K, V extends Comparable<? super V>> SDFrame<FI2<K, T>> groupByMax(Function<T, K> key, Function<T, V> value) ;

    /**
     * Group max
     * @param key       group field
     * @param key2      secondary level group field
     * @param value     Aggregated field
     */
    <K,J, V extends Comparable<? super V>> SDFrame<FI3<K,J,T>> groupByMax(Function<T, K> key, Function<T, J> key2,Function<T, V> value);


    /**
     * Group max value
     * @param key       group field
     * @param value     Aggregated field
     */
    <K, V extends Comparable<? super V>> SDFrame<FI2<K, V>> groupByMaxValue(Function<T, K> key, Function<T, V> value) ;

    /**
     * Group max value
     * @param key       group field
     * @param key2      secondary level group field
     * @param value     Aggregated field
     */
    <K,J, V extends Comparable<? super V>> SDFrame<FI3<K,J,V>> groupByMaxValue(Function<T, K> key, Function<T, J> key2,Function<T, V> value) ;


    /**
     * Group min
     * @param key       group field
     * @param value     Aggregated field
     */
    <K, V extends Comparable<? super V>> SDFrame<FI2<K, T>> groupByMin(Function<T, K> key, Function<T, V> value);


    /**
     * Group min
     * @param key       group field
     * @param key2      secondary level group field
     * @param value     Aggregated field
     */
    <K, J,V extends Comparable<? super V>> SDFrame<FI3<K, J,T>> groupByMin(Function<T, K> key, Function<T, J> key2,Function<T, V> value);


    /**
     * Group min value
     * @param key       group field
     * @param value     Aggregated field
     */
    <K, V extends Comparable<? super V>> SDFrame<FI2<K, V>> groupByMinValue(Function<T, K> key, Function<T, V> value);

    /**
     * Group min value
     * @param key       group field
     * @param key2      secondary level group field
     * @param value     Aggregated field
     */
    <K,J,V extends Comparable<? super V>> SDFrame<FI3<K,J,V>> groupByMinValue(Function<T, K> key, Function<T, J> key2,Function<T, V> value);


    /**
     * Group max and min value
     * @param key       group field
     * @param value     Aggregated field
     */
    <K, V extends Comparable<? super V>> SDFrame<FI2<K, MaxMin<V>>> groupByMaxMinValue(Function<T, K> key, Function<T, V> value);

    /**
     * Group max and min value
     * @param key       group field
     * @param key2      secondary level group field
     * @param value     Aggregated field
     */
    <K, J, V extends Comparable<? super V>> SDFrame<FI3<K, J, MaxMin<V>>> groupByMaxMinValue(Function<T, K> key,
                                                                                             Function<T, J> key2,
                                                                                             Function<T, V> value);

    /**
     * Group max and min element
     * @param key       group field
     * @param value     Aggregated field
     */
    <K, V extends Comparable<? super V>> SDFrame<FI2<K, MaxMin<T>>> groupByMaxMin(Function<T, K> key,
                                                                                  Function<T, V> value) ;

    /**
     * Group max and min element
     * @param key       group field
     * @param key2      secondary level group field
     * @param value     Aggregated field
     */
    <K, J, V extends Comparable<? super V>> SDFrame<FI3<K, J, MaxMin<T>>> groupByMaxMin(Function<T, K> key,
                                                                                        Function<T, J> key2,
                                                                                        Function<T, V> value);

}
