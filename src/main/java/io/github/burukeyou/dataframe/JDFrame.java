package io.github.burukeyou.dataframe;

import io.github.burukeyou.dataframe.dataframe.DFList;
import io.github.burukeyou.dataframe.dataframe.JDFrameImpl;
import io.github.burukeyou.dataframe.dataframe.MaxMin;
import io.github.burukeyou.dataframe.dataframe.ToBigDecimalFunction;
import io.github.burukeyou.dataframe.dataframe.item.FItem2;
import io.github.burukeyou.dataframe.dataframe.item.FItem3;
import io.github.burukeyou.dataframe.dataframe.item.FItem4;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 
 * @author caizhihao
 */
public interface JDFrame<T> {

    static <T> JDFrame<T> read(List<T> list) {
        return new JDFrameImpl<>(list);
    }

    /**
     * ===========================   排序相关  =====================================
     **/

    JDFrame<T> sortDesc(Comparator<T> comparator);

     <R extends Comparable<R>> JDFrame<T> sortDesc(Function<T, R> function);


    /** ===========================   截取相关  ===================================== **/

    /**
     * 截取前n个
     */
    JDFrame<T> first(int n);

    /**
     * ===========================   筛选相关  =====================================
     **/

    <R> JDFrame<T> whereNotNull(Function<T, R> function);

    <R extends Comparable<R>> JDFrame<T> whereBetween(Function<T, R> function, R start, R end);

    <R extends Comparable<R>> JDFrame<T> whereBetweenR(Function<T, R> function, R start, R end);

    <R extends Comparable<R>> JDFrame<T> whereNotBetween(Function<T, R> function, R start, R end);

    <R> JDFrame<T> whereIn(Function<T, R> function, List<R> list);

    <R> JDFrame<T> whereNotIn(Function<T, R> function, List<R> list);

    JDFrame<T> whereTrue(Predicate<T> predicate);

    JDFrame<T> whereNotTrue(Predicate<T> predicate);

    <R> JDFrame<T> whereEq(Function<T, R> function, R value);

    <R> JDFrame<T> whereNotEq(Function<T, R> function, R value);


     <R extends Comparable<R>> JDFrame<T> whereGt(Function<T, R> function, R value);

     <R extends Comparable<R>> JDFrame<T> whereGe(Function<T, R> function, R value);

     <R extends Comparable<R>> JDFrame<T> whereLt(Function<T, R> function, R value);

     <R extends Comparable<R>> JDFrame<T> whereLe(Function<T, R> function, R value);


     <R> JDFrame<T> whereLike(Function<T, R> function, R value);

     <R> JDFrame<T> whereNotLike(Function<T, R> function, R value);

     <R> JDFrame<T> whereLikeLeft(Function<T, R> function, R value);

     <R> JDFrame<T> whereLikeRight(Function<T, R> function, R value);

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
    <K> DFList<FItem2<K, BigDecimal>> groupBySum(Function<T, K> K, ToBigDecimalFunction<T> value);

    /**
     * 分组求和
     *
     * @param K     分组K
     * @param K2    二级分组K
     * @param value 聚合字段
     */
     <K, J> DFList<FItem3<K, J, BigDecimal>> groupBySum(Function<T, K> K, Function<T, J> K2, ToBigDecimalFunction<T> value);

    /**
     * 分组求和
     *
     * @param K     分组K
     * @param J    二级分组K
     * @param H    三级分组K
     * @param value 聚合字段
     */
     <K, J, H> DFList<FItem4<K, J, H, BigDecimal>> groupBySum(Function<T, K> K, 
                                                              Function<T, J> J, 
                                                              Function<T, H> H, 
                                                              ToBigDecimalFunction<T> value);

    /**
     * 分组求数量
     *
     * @param K 分组K
     */
     <K> DFList<FItem2<K, Long>> groupByCount(Function<T, K> K);

    /**
     * 分组求数量
     *
     * @param K  分组K
     * @param J 二级分组K
     */
     <K, J> DFList<FItem3<K, J, Long>> groupByCount(Function<T, K> K, Function<T, J> J);

    /**
     * 分组求数量
     *
     * @param K 分组K
     *          二级分组K
     *          三级分组K
     */
     <K, J, H> DFList<FItem4<K, J, H, Long>> groupByCount(Function<T, K> K, Function<T, J> J, Function<T, H> H);


    /**
     * 分组求和及数量
     *
     * @param K     分组的字段
     * @param value 求和的字段
     * @return              FItem3<K, 和, 数量>
     */
     <K> DFList<FItem3<K, BigDecimal,Long>> groupBySumCount(Function<T, K> K, ToBigDecimalFunction<T> value);

    /**
     * 分组求和及数量
     *
     * @param K             分组K
     * @param J             二级分组K
     * @param value         求和字段
     * @return              FItem4<K,K2, 和, 数量>
     */
     <K, J> DFList<FItem4<K, J, BigDecimal, Long>> groupBySumCount(Function<T, K> K, Function<T, J> J, ToBigDecimalFunction<T> value);


    /**
     * 分组求平均值
     *
     * @param K     分组的字段
     * @param value 聚合的字段
     */
     <K> DFList<FItem2<K, BigDecimal>> groupByAvg(Function<T, K> K, ToBigDecimalFunction<T> value) ;

    /**
     * 分组求平均
     *
     * @param K     分组K
     * @param J    二级分组K
     * @param value 聚合字段
     */
     <K, J> DFList<FItem3<K, J, BigDecimal>> groupByAvg(Function<T, K> K, Function<T, J> J, ToBigDecimalFunction<T> value);

    /**
     * 分组求平均
     *
     * @param K     分组K
     * @param J    二级分组K
     * @param H    三级分组K
     * @param value 聚合字段
     */
     <K, J, H> DFList<FItem4<K, J, H, BigDecimal>> groupByAvg(Function<T, K> K,
                                                              Function<T, J> J,
                                                              Function<T, H> H,
                                                              ToBigDecimalFunction<T> value) ;


    /**
     * 分组求最大
     *
     * @param K     分组K
     * @param value 聚合字段
     */
     <K, V extends Comparable<V>> DFList<FItem2<K, T>> groupByMax(Function<T, K> K, Function<T, V> value) ;
    /**
     * 分组求最小
     *
     * @param K     分组K
     * @param value 聚合字段
     */
     <K, V extends Comparable<V>> DFList<FItem2<K, T>> groupByMin(Function<T, K> K, Function<T, V> value);

    /**
     * 分组求最大和最小值
     *
     * @param K     分组K
     * @param value 聚合字段
     */
     <K, V extends Comparable<V>> DFList<FItem2<K, MaxMin<V>>> groupByMaxAndMinValue(Function<T, K> K, Function<T, V> value);
     
    /**
     * 分组求最大和最小值
     *
     * @param K     分组K
     * @param J    二级分组K
     * @param value 聚合字段
     */
     <K, J, V extends Comparable<V>> DFList<FItem3<K, J, MaxMin<V>>> groupByMaxAndMinValue(Function<T, K> K,
                                                                                           Function<T, J> J,
                                                                                           Function<T, V> value);

    /**
     * 分组求最大和最小
     *
     * @param K     分组K
     * @param value 聚合字段
     */
     <K, V extends Comparable<V>> DFList<FItem2<K, MaxMin<T>>> groupByMaxAndMin(Function<T, K> K,
                                                                                Function<T, V> value) ;

    /**
     * 分组求最大和最小
     *
     * @param K     分组K
     * @param J   二级分组K
     * @param value 聚合字段
     */
     <K, J, V extends Comparable<V>> DFList<FItem3<K, J, MaxMin<T>>> groupByMaxAndMin(Function<T, K> K,
                                                                                      Function<T, J> J,
                                                                                      Function<T, V> value);
}
