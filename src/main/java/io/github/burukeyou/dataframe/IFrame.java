package io.github.burukeyou.dataframe;

import io.github.burukeyou.dataframe.dataframe.MaxMin;
import io.github.burukeyou.dataframe.dataframe.ToBigDecimalFunction;
import io.github.burukeyou.dataframe.dataframe.item.FT2;
import io.github.burukeyou.dataframe.dataframe.item.FT3;
import io.github.burukeyou.dataframe.dataframe.item.FT4;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * 
 * @author caizhihao
 */
public interface IFrame<T> {


    List<T> toLists();

    Stream<T> stream();

    /**
     * ===========================   排序相关  =====================================
     **/

    IFrame<T> sortDesc(Comparator<T> comparator);

    <R extends Comparable<R>> IFrame<T> sortDesc(Function<T, R> function);

    IFrame<T> sortAsc(Comparator<T> comparator);

    <R extends Comparable<R>> IFrame<T> sortAsc(Function<T, R> function);

    /** ===========================   截取相关  ===================================== **/

    /**
     * 截取前n个
     */
    IFrame<T> first(int n);

    /**
     * ===========================   筛选相关  =====================================
     **/

    <R> IFrame<T> whereNotNull(Function<T, R> function);

    <R extends Comparable<R>> IFrame<T> whereBetween(Function<T, R> function, R start, R end);

    /**
     * 区间筛选 （前开后闭）
     */
    <R extends Comparable<R>> IFrame<T> whereBetweenR(Function<T, R> function, R start, R end);

    <R extends Comparable<R>> IFrame<T> whereNotBetween(Function<T, R> function, R start, R end);

    <R> IFrame<T> whereIn(Function<T, R> function, List<R> list);

    <R> IFrame<T> whereNotIn(Function<T, R> function, List<R> list);

    IFrame<T> whereTrue(Predicate<T> predicate);

    IFrame<T> whereNotTrue(Predicate<T> predicate);

    <R> IFrame<T> whereEq(Function<T, R> function, R value);

    <R> IFrame<T> whereNotEq(Function<T, R> function, R value);


    <R extends Comparable<R>> IFrame<T> whereGt(Function<T, R> function, R value);

    <R extends Comparable<R>> IFrame<T> whereGe(Function<T, R> function, R value);

    <R extends Comparable<R>> IFrame<T> whereLt(Function<T, R> function, R value);

    <R extends Comparable<R>> IFrame<T> whereLe(Function<T, R> function, R value);


    <R> IFrame<T> whereLike(Function<T, R> function, R value);

    <R> IFrame<T> whereNotLike(Function<T, R> function, R value);

    <R> IFrame<T> whereLikeLeft(Function<T, R> function, R value);

    <R> IFrame<T> whereLikeRight(Function<T, R> function, R value);

    /**
     * ===========================   汇总相关  =====================================
     **/

    <R> BigDecimal sum(Function<T, R> function);

    <R> Integer sumInt(Function<T, R> function);

    <R> BigDecimal avg(Function<T, R> function);

    <R extends Comparable<R>> MaxMin<R> maxAndMinValue(Function<T, R> function);

    <R extends Comparable<R>> MaxMin<T> maxAndMin(Function<T, R> function);

    <R extends Comparable<R>> R maxValue(Function<T, R> function);
    <R extends Comparable<R>> T max(Function<T, R> function) ;

    <R extends Comparable<R>> R minValue(Function<T, R> function);

    <R extends Comparable<R>> T min(Function<T, R> function);

    int count();

    /** ===========================   分组相关  ===================================== **/
    /**
     * 分组求和
     *
     * @param K     分组的字段
     * @param value 聚合的字段
     */
    <K> IFrame<FT2<K, BigDecimal>> groupBySum(Function<T, K> K, ToBigDecimalFunction<T> value);

    /**
     * 分组求和
     *
     * @param K     分组K
     * @param K2    二级分组K
     * @param value 聚合字段
     */
    <K, J> IFrame<FT3<K, J, BigDecimal>> groupBySum(Function<T, K> K, Function<T, J> K2, ToBigDecimalFunction<T> value);

    /**
     * 分组求和
     *
     * @param K     分组K
     * @param J    二级分组K
     * @param H    三级分组K
     * @param value 聚合字段
     */
    <K, J, H> IFrame<FT4<K, J, H, BigDecimal>> groupBySum(Function<T, K> K,
                                                          Function<T, J> J,
                                                          Function<T, H> H,
                                                          ToBigDecimalFunction<T> value);

    /**
     * 分组求数量
     *
     * @param K 分组K
     */
    <K> IFrame<FT2<K, Long>> groupByCount(Function<T, K> K);

    /**
     * 分组求数量
     *
     * @param K  分组K
     * @param J 二级分组K
     */
    <K, J> IFrame<FT3<K, J, Long>> groupByCount(Function<T, K> K, Function<T, J> J);

    /**
     * 分组求数量
     *
     * @param K 分组K
     *          二级分组K
     *          三级分组K
     */
    <K, J, H> IFrame<FT4<K, J, H, Long>> groupByCount(Function<T, K> K, Function<T, J> J, Function<T, H> H);


    /**
     * 分组求和及数量
     *
     * @param K     分组的字段
     * @param value 求和的字段
     * @return              FItem3<K, 和, 数量>
     */
    <K> IFrame<FT3<K, BigDecimal,Long>> groupBySumCount(Function<T, K> K, ToBigDecimalFunction<T> value);

    /**
     * 分组求和及数量
     *
     * @param K             分组K
     * @param J             二级分组K
     * @param value         求和字段
     * @return              FItem4<K,K2, 和, 数量>
     */
    <K, J> IFrame<FT4<K, J, BigDecimal, Long>> groupBySumCount(Function<T, K> K, Function<T, J> J, ToBigDecimalFunction<T> value);


    /**
     * 分组求平均值
     *
     * @param K     分组的字段
     * @param value 聚合的字段
     */
    <K> IFrame<FT2<K, BigDecimal>> groupByAvg(Function<T, K> K, ToBigDecimalFunction<T> value) ;

    /**
     * 分组求平均
     *
     * @param K     分组K
     * @param J    二级分组K
     * @param value 聚合字段
     */
    <K, J> IFrame<FT3<K, J, BigDecimal>> groupByAvg(Function<T, K> K, Function<T, J> J, ToBigDecimalFunction<T> value);

    /**
     * 分组求平均
     *
     * @param K     分组K
     * @param J    二级分组K
     * @param H    三级分组K
     * @param value 聚合字段
     */
    <K, J, H> IFrame<FT4<K, J, H, BigDecimal>> groupByAvg(Function<T, K> K,
                                                          Function<T, J> J,
                                                          Function<T, H> H,
                                                          ToBigDecimalFunction<T> value) ;


    /**
     * 分组求最大
     *
     * @param K     分组K
     * @param value 聚合字段
     */
    <K, V extends Comparable<V>> IFrame<FT2<K, T>> groupByMax(Function<T, K> K, Function<T, V> value) ;
    /**
     * 分组求最小
     *
     * @param K     分组K
     * @param value 聚合字段
     */
    <K, V extends Comparable<V>> IFrame<FT2<K, T>> groupByMin(Function<T, K> K, Function<T, V> value);

    /**
     * 分组求最大和最小值
     *
     * @param K     分组K
     * @param value 聚合字段
     */
    <K, V extends Comparable<V>> IFrame<FT2<K, MaxMin<V>>> groupByMaxAndMinValue(Function<T, K> K, Function<T, V> value);

    /**
     * 分组求最大和最小值
     *
     * @param K     分组K
     * @param J    二级分组K
     * @param value 聚合字段
     */
    <K, J, V extends Comparable<V>> IFrame<FT3<K, J, MaxMin<V>>> groupByMaxAndMinValue(Function<T, K> K,
                                                                                       Function<T, J> J,
                                                                                       Function<T, V> value);

    /**
     * 分组求最大和最小
     *
     * @param K     分组K
     * @param value 聚合字段
     */
    <K, V extends Comparable<V>> IFrame<FT2<K, MaxMin<T>>> groupByMaxAndMin(Function<T, K> K,
                                                                            Function<T, V> value) ;

    /**
     * 分组求最大和最小
     *
     * @param K     分组K
     * @param J   二级分组K
     * @param value 聚合字段
     */
    <K, J, V extends Comparable<V>> IFrame<FT3<K, J, MaxMin<T>>> groupByMaxAndMin(Function<T, K> K,
                                                                                  Function<T, J> J,
                                                                                  Function<T, V> value);

}
