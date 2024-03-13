package io.github.burukeyou.dataframe.iframe;

import io.github.burukeyou.dataframe.iframe.item.FI2;
import io.github.burukeyou.dataframe.iframe.item.FI3;
import io.github.burukeyou.dataframe.iframe.item.FI4;
import io.github.burukeyou.dataframe.iframe.support.Join;
import io.github.burukeyou.dataframe.iframe.support.JoinOn;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Stream DataFrame
 *      前后的操作是连续的, 与stream流一致，并且某些操作是终止操作执行后流不可再用，得重新read生成流, 适合串行使用
 *
 * @author caizhihao
 */
public interface SDFrame<T> extends IFrame<T>  {

    static <T> SDFrame<T> read(List<T> list) {
        return new SDFrameImpl<>(list);
    }

    <R> SDFrame<R> map(Function<T,R> map);

    <R extends Number> SDFrame<T> mapPercent(Function<T,R> get, SetFunction<T,BigDecimal> set);

    <R extends Number> SDFrame<T> mapPercent(Function<T,R> get, SetFunction<T,BigDecimal> set, int scale);

    SDFrame<List<T>> partition(int n);

    SDFrame<T> append(T t);

    SDFrame<T> union(IFrame<T> other);


    <R,K> SDFrame<R> join(IFrame<K> other, JoinOn<T,K> on, Join<T,K,R> join);

    <R,K> SDFrame<R> join(IFrame<K> other, JoinOn<T,K> on);


    <R,K> SDFrame<R> leftJoin(IFrame<K> other, JoinOn<T,K> on, Join<T,K,R> join);

    <R,K> SDFrame<R> leftJoin(IFrame<K> other, JoinOn<T,K> on);

    <R,K> SDFrame<R> rightJoin(IFrame<K> other, JoinOn<T,K> on, Join<T,K,R> join);

    <R,K> SDFrame<R> rightJoin(IFrame<K> other, JoinOn<T,K> on);

    SDFrame<FI2<T,Integer>> addSortNoCol();

    SDFrame<FI2<T,Integer>> addSortNoCol(Comparator<T> comparator);

    <R extends Comparable<R>>  SDFrame<FI2<T,Integer>> addSortNoCol(Function<T, R> function);


    SDFrame<T> addSortNoCol(SetFunction<T,Integer> set);

    SDFrame<FI2<T,Integer>> addRankingSameCol(Comparator<T> comparator);


    <R extends Comparable<R>> SDFrame<FI2<T,Integer>> addRankingSameCol(Function<T, R> function);

    SDFrame<T> addRankingSameCol(Comparator<T> comparator,SetFunction<T,Integer> set);

    <R extends Comparable<R>>  SDFrame<T> addRankingSameCol(Function<T, R> function,SetFunction<T,Integer> set);

    /**
     * ===========================   排序相关  =====================================
     **/

    SDFrame<T> sortDesc(Comparator<T> comparator);

    <R extends Comparable<R>> SDFrame<T> sortDesc(Function<T, R> function);

    SDFrame<T> sortAsc(Comparator<T> comparator);

    <R extends Comparable<R>> SDFrame<T> sortAsc(Function<T, R> function);

    SDFrame<T> subRankingSameAsc(Comparator<T> comparator, int n);

    <R extends Comparable<R>> SDFrame<T> subRankingSameAsc(Function<T, R> function, int n);

    SDFrame<T> subRankingSameDesc(Comparator<T> comparator, int n);

    <R extends Comparable<R>> SDFrame<T> subRankingSameDesc(Function<T, R> function, int n);


    /** ===========================   截取相关  ===================================== **/

    SDFrame<T> subFirst(int n);

    SDFrame<T> subLast(int n);


    /** ===========================   去重相关  ===================================== **/

    SDFrame<T> distinct();

    <R extends Comparable<R>> SDFrame<T> distinct(Function<T, R> function);

    <R extends Comparable<R>> SDFrame<T> distinct(Comparator<T> comparator);


    /**
     * ===========================   筛选相关  =====================================
     **/

    <R> SDFrame<T> whereNull(Function<T, R> function);

    <R> SDFrame<T> whereNotNull(Function<T, R> function);

    /**
     * 区间内筛选 （前闭后闭）
     */
    <R extends Comparable<R>> SDFrame<T> whereBetween(Function<T, R> function, R start, R end);

    /**
     * 区间内筛选 （前开后开）
     */
    <R extends Comparable<R>> SDFrame<T> whereBetweenN(Function<T, R> function, R start, R end);

    /**
     * 区间内筛选 （前开后闭）
     */
    <R extends Comparable<R>> SDFrame<T> whereBetweenR(Function<T, R> function, R start, R end);

    /**
     * 区间内筛选 （前闭后开）
     */
    <R extends Comparable<R>> SDFrame<T> whereBetweenL(Function<T, R> function, R start, R end);

    /**
     * 区间外筛选 （前闭后闭）
     */
    <R extends Comparable<R>> SDFrame<T> whereNotBetween(Function<T, R> function, R start, R end);

    /**
     * 区间外筛选 （前开后开）
     */
    <R extends Comparable<R>> SDFrame<T> whereNotBetweenN(Function<T, R> function, R start, R end);

    <R> SDFrame<T> whereIn(Function<T, R> function, List<R> list);

    <R> SDFrame<T> whereNotIn(Function<T, R> function, List<R> list);

    SDFrame<T> whereTrue(Predicate<T> predicate);

    SDFrame<T> whereNotTrue(Predicate<T> predicate);

    <R> SDFrame<T> whereEq(Function<T, R> function, R value);

    <R> SDFrame<T> whereNotEq(Function<T, R> function, R value);


    <R extends Comparable<R>> SDFrame<T> whereGt(Function<T, R> function, R value);

    <R extends Comparable<R>> SDFrame<T> whereGe(Function<T, R> function, R value);

    <R extends Comparable<R>> SDFrame<T> whereLt(Function<T, R> function, R value);

    <R extends Comparable<R>> SDFrame<T> whereLe(Function<T, R> function, R value);


    <R> SDFrame<T> whereLike(Function<T, R> function, R value);

    <R> SDFrame<T> whereNotLike(Function<T, R> function, R value);

    <R> SDFrame<T> whereLikeLeft(Function<T, R> function, R value);

    <R> SDFrame<T> whereLikeRight(Function<T, R> function, R value);

    /** ===========================   分组相关  ===================================== **/
    /**
     * 分组求和
     *
     * @param key     分组的字段
     * @param value 聚合的字段
     */
    <K> SDFrame<FI2<K, BigDecimal>> groupBySum(Function<T, K> key, ToBigDecimalFunction<T> value);

    /**
     * 分组求和
     *
     * @param key     分组K
     * @param key2    二级分组K
     * @param value 聚合字段
     */
    <K, J> SDFrame<FI3<K, J, BigDecimal>> groupBySum(Function<T, K> key, Function<T, J> key2, ToBigDecimalFunction<T> value);

    /**
     * 分组求和
     *
     * @param key     分组K
     * @param key2    二级分组K
     * @param key3    三级分组K
     * @param value 聚合字段
     */
    <K, J, H> SDFrame<FI4<K, J, H, BigDecimal>> groupBySum(Function<T, K> key,
                                                           Function<T, J> key2,
                                                           Function<T, H> key3,
                                                           ToBigDecimalFunction<T> value);

    /**
     * 分组求数量
     *
     * @param key 分组K
     */
    <K> SDFrame<FI2<K, Long>> groupByCount(Function<T, K> key);

    /**
     * 分组求数量
     *
     * @param key  分组K
     * @param key2 二级分组K
     */
    <K, J> SDFrame<FI3<K, J, Long>> groupByCount(Function<T, K> key, Function<T, J> key2);

    /**
     * 分组求数量
     *
     * @param key 分组K
     *          二级分组K
     *          三级分组K
     */
    <K, J, H> SDFrame<FI4<K, J, H, Long>> groupByCount(Function<T, K> key, Function<T, J> key2, Function<T, H> key3);


    /**
     * 分组求和及数量
     *
     * @param key     分组的字段
     * @param value 求和的字段
     * @return              FItem3<K, 和, 数量>
     */
    <K> SDFrame<FI3<K, BigDecimal,Long>> groupBySumCount(Function<T, K> key, ToBigDecimalFunction<T> value);

    /**
     * 分组求和及数量
     *
     * @param key             分组K
     * @param key2             二级分组K
     * @param value         求和字段
     * @return              FItem4<K,K2, 和, 数量>
     */
    <K, J> SDFrame<FI4<K, J, BigDecimal, Long>> groupBySumCount(Function<T, K> key, Function<T, J> key2, ToBigDecimalFunction<T> value);


    /**
     * 分组求平均值
     *
     * @param key     分组的字段
     * @param value 聚合的字段
     */
    <K> SDFrame<FI2<K, BigDecimal>> groupByAvg(Function<T, K> key, ToBigDecimalFunction<T> value) ;

    /**
     * 分组求平均
     *
     * @param key     分组K
     * @param key2    二级分组K
     * @param value 聚合字段
     */
    <K, J> SDFrame<FI3<K, J, BigDecimal>> groupByAvg(Function<T, K> key, Function<T, J> key2, ToBigDecimalFunction<T> value);

    /**
     * 分组求平均
     *
     * @param key     分组K
     * @param key2    二级分组K
     * @param key3    三级分组K
     * @param value 聚合字段
     */
    <K, J, H> SDFrame<FI4<K, J, H, BigDecimal>> groupByAvg(Function<T, K> key,
                                                           Function<T, J> key2,
                                                           Function<T, H> key3,
                                                           ToBigDecimalFunction<T> value) ;


    /**
     * 分组求最大
     *
     * @param key     分组K
     * @param value 聚合字段
     */
    <K, V extends Comparable<V>> SDFrame<FI2<K, T>> groupByMax(Function<T, K> key, Function<T, V> value) ;
    /**
     * 分组求最小
     *
     * @param key     分组K
     * @param value 聚合字段
     */
    <K, V extends Comparable<V>> SDFrame<FI2<K, T>> groupByMin(Function<T, K> key, Function<T, V> value);

    /**
     * 分组求最大和最小值
     *
     * @param key     分组K
     * @param value 聚合字段
     */
    <K, V extends Comparable<V>> SDFrame<FI2<K, MaxMin<V>>> groupByMaxMinValue(Function<T, K> key, Function<T, V> value);

    /**
     * 分组求最大和最小值
     *
     * @param key     分组K
     * @param key2    二级分组K
     * @param value 聚合字段
     */
    <K, J, V extends Comparable<V>> SDFrame<FI3<K, J, MaxMin<V>>> groupByMaxMinValue(Function<T, K> key,
                                                                                     Function<T, J> key2,
                                                                                     Function<T, V> value);

    /**
     * 分组求最大和最小
     *
     * @param key     分组K
     * @param value 聚合字段
     */
    <K, V extends Comparable<V>> SDFrame<FI2<K, MaxMin<T>>> groupByMaxMin(Function<T, K> key,
                                                                          Function<T, V> value) ;

    /**
     * 分组求最大和最小
     *
     * @param key     分组K
     * @param key2   二级分组K
     * @param value 聚合字段
     */
    <K, J, V extends Comparable<V>> SDFrame<FI3<K, J, MaxMin<T>>> groupByMaxMin(Function<T, K> key,
                                                                                Function<T, J> key2,
                                                                                Function<T, V> value);
}
