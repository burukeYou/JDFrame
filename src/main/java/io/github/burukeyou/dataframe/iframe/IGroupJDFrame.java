package io.github.burukeyou.dataframe.iframe;

import io.github.burukeyou.dataframe.iframe.function.ListToOneValueFunction;
import io.github.burukeyou.dataframe.iframe.function.NumberFunction;
import io.github.burukeyou.dataframe.iframe.group.GroupConcat;
import io.github.burukeyou.dataframe.iframe.item.FI2;
import io.github.burukeyou.dataframe.iframe.item.FI3;
import io.github.burukeyou.dataframe.iframe.item.FI4;
import io.github.burukeyou.dataframe.iframe.support.MaxMin;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

/**
 * @author          caizhihao
 * @param <T>
 */
public interface IGroupJDFrame<T> extends IGroupFrame<T> {

    /**
     * Group list
     * @param key        group field
     */
    <K> JDFrame<FI2<K,List<T>>> group(Function<? super T, ? extends K> key);


    /**
     * Group list
     * @param key        group field
     * @param key2       secondary level group field
     */
    <K,J> JDFrame<FI3<K,J,List<T>>> group2(Function<T, K> key, Function<T,J> key2);

    /**
     * Group list
     * @param key        group field
     * @param key2       secondary level group field
     * @param key3      third level group field
     */
    <K,J,H> JDFrame<FI4<K,J,H,List<T>>> group3(Function<T, K> key, Function<T,J> key2, Function<T,H> key3);


    /**
     * Group list
     * @param key               group field
     * @param function          group list to one value function
     */
    <K,V> JDFrame<FI2<K,V>> groupByCustom(Function<T,K> key, ListToOneValueFunction<T,V> function);


    /**
     * Group list
     * @param key        group field
     * @param key2       secondary level group field
     * @param function          group list to one value function
     */
    <K,J,V> JDFrame<FI3<K,J,V>> group2ByCustom(Function<T, K> key, Function<T,J> key2, ListToOneValueFunction<T,V> function);


    /**
     * Group list
     * @param key               group field
     * @param key2              secondary level group field
     * @param key3              third level group field
     * @param function          group list to one value function
     */
    <K,J,H,V> JDFrame<FI4<K,J,H,V>> group3ByCustom(Function<T, K> key, Function<T,J> key2, Function<T,H> key3, ListToOneValueFunction<T,V> function);


    /**
     * Group summation
     * @param key       group field
     * @param value     Aggregated field
     */
    <K,R extends Number> JDFrame<FI2<K, BigDecimal>> groupBySum(Function<T, K> key, NumberFunction<T,R> value);

    /**
     * Group summation
     * @param key       group field
     * @param key2      secondary level group field
     * @param value     Aggregated field
     */
    <K, J,R extends Number> JDFrame<FI3<K,J, BigDecimal>> group2BySum(Function<T, K> key, Function<T, J> key2, NumberFunction<T,R> value);

    /**
     * Group summation
     *
     * @param key     group field
     * @param key2    secondary level group field
     * @param key3    third level group field
     * @param value   Aggregated field
     */
    <K, J, H,R extends Number> JDFrame<FI4<K, J, H, BigDecimal>> group3BySum(Function<T, K> key,
                                                                             Function<T, J> key2,
                                                                             Function<T, H> key3,
                                                                             NumberFunction<T,R> value);

    /**
     * Group count
     * @param key       group field
     */
    <K> JDFrame<FI2<K, Long>> groupByCount(Function<T, K> key);

    /**
     * Group count
     * @param key       group field
     * @param key2      secondary level group field
     */
    <K, J> JDFrame<FI3<K, J, Long>> group2ByCount(Function<T, K> key, Function<T, J> key2);

    /**
     * Group count
     *
     * @param key     group field
     * @param key2    secondary level group field
     * @param key3    third level group field
     */
    <K, J, H> JDFrame<FI4<K, J, H, Long>> group3ByCount(Function<T, K> key, Function<T, J> key2, Function<T, H> key3);

    /**
     * Group sum and count together
     *
     * @param key           group field
     * @param value         Aggregated field
     * @return              FItem3(key, Sum, Count)
     */
    <K,R extends Number> JDFrame<FI3<K, BigDecimal,Long>> groupBySumCount(Function<T, K> key, NumberFunction<T,R> value);

    /**
     * Group sum and count together
     *
     * @param key           group field
     * @param key2          secondary level group field
     * @param value         Aggregated field
     * @return              FItem4(key, ke2,Sum, Count)
     */
    <K, J,R extends Number> JDFrame<FI4<K, J, BigDecimal, Long>> group2BySumCount(Function<T, K> key, Function<T, J> key2, NumberFunction<T,R> value);


    /**
     * Group average
     * @param key       group field
     * @param value     Aggregated field
     */
    <K,R extends Number> JDFrame<FI2<K, BigDecimal>> groupByAvg(Function<T, K> key, NumberFunction<T,R> value) ;

    /**
     * Group average
     * @param key       group field
     * @param key2      secondary level group field
     * @param value     Aggregated field
     */
    <K, J,R extends Number> JDFrame<FI3<K, J, BigDecimal>> group2ByAvg(Function<T, K> key, Function<T, J> key2, NumberFunction<T,R> value);

    /**
     * Group average
     * @param key       group field
     * @param key2      secondary level group field
     * @param key3      third level group field
     * @param value     Aggregated field
     */
    <K, J, H,R extends Number> JDFrame<FI4<K, J, H, BigDecimal>> group3ByAvg(Function<T, K> key,
                                                                             Function<T, J> key2,
                                                                             Function<T, H> key3,
                                                                             NumberFunction<T,R> value) ;

    /**
     * Group max
     * @param key       group field
     * @param value     Aggregated field
     */
    <K, V extends Comparable<? super V>> JDFrame<FI2<K, T>> groupByMax(Function<T, K> key, Function<T, V> value) ;

    /**
     * Group max
     * @param key       group field
     * @param key2      secondary level group field
     * @param value     Aggregated field
     */
    <K,J, V extends Comparable<? super V>> JDFrame<FI3<K,J, T>> group2ByMax(Function<T, K> key, Function<T, J> key2, Function<T, V> value);


    /**
     * Group max value
     * @param key       group field
     * @param value     Aggregated field
     */
    <K, V extends Comparable<? super V>> JDFrame<FI2<K, V>> groupByMaxValue(Function<T, K> key, Function<T, V> value) ;


    /**
     * Group max value
     * @param key       group field
     * @param key2      secondary level group field
     * @param value     Aggregated field
     */
    <K,J, V extends Comparable<? super V>> JDFrame<FI3<K,J,V>> group2ByMaxValue(Function<T, K> key, Function<T, J> key2, Function<T, V> value) ;


    /**
     * Group min
     * @param key       group field
     * @param value     Aggregated field
     */
    <K, V extends Comparable<? super V>> JDFrame<FI2<K, T>> groupByMin(Function<T, K> key, Function<T, V> value);


    /**
     * Group min
     * @param key       group field
     * @param key2      secondary level group field
     * @param value     Aggregated field
     */
    <K, J,V extends Comparable<? super V>> JDFrame<FI3<K, J,T>> group2ByMin(Function<T, K> key, Function<T, J> key2, Function<T, V> value);


    /**
     * Group min value
     * @param key       group field
     * @param value     Aggregated field
     */
    <K, V extends Comparable<? super V>> JDFrame<FI2<K, V>> groupByMinValue(Function<T, K> key, Function<T, V> value);


    /**
     * Group min value
     * @param key       group field
     * @param key2      secondary level group field
     * @param value     Aggregated field
     */
    <K,J,V extends Comparable<? super V>> JDFrame<FI3<K,J,V>> group2ByMinValue(Function<T, K> key, Function<T, J> key2, Function<T, V> value);


    /**
     * Group max and min value
     * @param key       group field
     * @param value     Aggregated field
     */
    <K, V extends Comparable<? super V>> JDFrame<FI2<K, MaxMin<V>>> groupByMaxMinValue(Function<T, K> key, Function<T, V> value);

    /**
     * Group max and min value
     * @param key       group field
     * @param key2      secondary level group field
     * @param value     Aggregated field
     */
    <K, J, V extends Comparable<? super V>> JDFrame<FI3<K, J, MaxMin<V>>> group2ByMaxMinValue(Function<T, K> key,
                                                                                              Function<T, J> key2,
                                                                                              Function<T, V> value);

    /**
     * Group max and min element
     * @param key       group field
     * @param value     Aggregated field
     */
    <K, V extends Comparable<? super V>> JDFrame<FI2<K, MaxMin<T>>> groupByMaxMin(Function<T, K> key,
                                                                                  Function<T, V> value) ;

    /**
     * Group max and min element
     * @param key       group field
     * @param key2      secondary level group field
     * @param value     Aggregated field
     */
    <K, J, V extends Comparable<? super V>> JDFrame<FI3<K, J, MaxMin<T>>> group2ByMaxMin(Function<T, K> key,
                                                                                         Function<T, J> key2,
                                                                                         Function<T, V> value);

    /**
     * Group Concat
     * @param key                          group field
     * @param concatField                  concat value
     * @param delimiter                    concat delimiter
     */
    <K,V> JDFrame<FI2<K,String>> groupByConcat(Function<T, K> key, Function<T,V> concatField,CharSequence delimiter);


    /**
     * Group Concat
     * @param key                     group field
     * @param concat                  group concat builder
     */
    <K> JDFrame<FI2<K,String>> groupByConcat(Function<T, K> key, GroupConcat<T> concat);

    /**
     * Group Concat
     * @param key                          first group field
     * @param key2                         second group field
     * @param concatField                  concat value
     * @param delimiter                    concat delimiter
     */
    <K,J,V> JDFrame<FI3<K,J,String>> group2ByConcat(Function<T, K> key, Function<T, J> key2,Function<T,V> concatField,CharSequence delimiter);

    /**
     * Group Concat
     * @param key                          first group field
     * @param key2                         second group field
     * @param concat                       group concat builder
     */
    <K,J> JDFrame<FI3<K,J,String>> group2ByConcat(Function<T, K> key, Function<T, J> key2,GroupConcat<T> concat);

    /**
     * Group Concat
     * @param key                          first group field
     * @param key2                         second group field
     * @param key3                         third group field
     * @param concat                       group concat builder
     */
    <K,J,H> JDFrame<FI4<K,J,H,String>> group3ByConcat(Function<T, K> key, Function<T, J> key2,Function<T, H> key3,GroupConcat<T> concat);

}
