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
 *  JDFrame
 *      前后的操作是非连续的，所有操作实时生效，即使执行终止操作后还可再用，不用重新read生成流
 *
 * @author  caizhihao
 */
public interface JDFrame<T> extends IFrame<T> {

     static <T> JDFrame<T> read(List<T> list) {
        return new JDFrameImpl<>(list);
    }


    /**
     * ===========================   矩阵变换  =====================================
     **/

    <R> JDFrame<R> map(Function<T,R> map);

    <R extends Number> JDFrame<T> mapPercent(Function<T,R> get, SetFunction<T,BigDecimal> set);


    <R extends Number> JDFrame<T> mapPercent(Function<T,R> get, SetFunction<T,BigDecimal> set, int scale);

    JDFrame<List<T>> partition(int n);

    JDFrame<T> append(T t);

    JDFrame<T> union(IFrame<T> other);

    <R,K> JDFrame<R> join(IFrame<K> other, JoinOn<T,K> on, Join<T,K,R> join);

    <R,K> JDFrame<R> join(IFrame<K> other, JoinOn<T,K> on);

    <R,K> JDFrame<R> leftJoin(IFrame<K> other, JoinOn<T,K> on, Join<T,K,R> join);

    <R,K> JDFrame<R> leftJoin(IFrame<K> other, JoinOn<T,K> on);

    <R,K> JDFrame<R> rightJoin(IFrame<K> other, JoinOn<T,K> on, Join<T,K,R> join);

    <R,K> JDFrame<R> rightJoin(IFrame<K> other, JoinOn<T,K> on);


    JDFrame<FI2<T,Integer>> addSortNoCol();

    JDFrame<FI2<T,Integer>> addSortNoCol(Comparator<T> comparator);

    <R extends Comparable<R>>  JDFrame<FI2<T,Integer>> addSortNoCol(Function<T, R> function);

    JDFrame<T> addSortNoCol(SetFunction<T,Integer> set);

    JDFrame<FI2<T,Integer>> addRankingSameCol(Comparator<T> comparator);

    <R extends Comparable<R>> JDFrame<FI2<T,Integer>> addRankingSameCol(Function<T, R> function);


    JDFrame<T> addRankingSameCol(Comparator<T> comparator,SetFunction<T,Integer> set);

    <R extends Comparable<R>>  JDFrame<T> addRankingSameCol(Function<T, R> function,SetFunction<T,Integer> set);


    /**
     * ===========================   排序相关  =====================================
     **/

    JDFrame<T> sortDesc(Comparator<T> comparator);

    <R extends Comparable<R>> JDFrame<T> sortDesc(Function<T, R> function);

    JDFrame<T> sortAsc(Comparator<T> comparator);

    <R extends Comparable<R>> JDFrame<T> sortAsc(Function<T, R> function);


    JDFrame<T> subRankingSameAsc(Comparator<T> comparator, int n);

    <R extends Comparable<R>> JDFrame<T> subRankingSameAsc(Function<T, R> function, int n);

    JDFrame<T> subRankingSameDesc(Comparator<T> comparator, int n);

    <R extends Comparable<R>> JDFrame<T> subRankingSameDesc(Function<T, R> function, int n);

    /** ===========================   截取相关  ===================================== **/

    /**
     * 截取前n个
     */
    JDFrame<T> subFirst(int n);


    JDFrame<T> subLast(int n);

    /** ===========================   去重相关  ===================================== **/

    JDFrame<T> distinct();

    <R extends Comparable<R>> JDFrame<T> distinct(Function<T, R> function);

    <R extends Comparable<R>> JDFrame<T> distinct(Comparator<T> comparator);


    /**
     * ===========================   筛选相关  =====================================
     **/

    <R> JDFrame<T> whereNull(Function<T, R> function);

    <R> JDFrame<T> whereNotNull(Function<T, R> function);

    /**
     * 区间内筛选 （前闭后闭）
     */
    <R extends Comparable<R>> JDFrame<T> whereBetween(Function<T, R> function, R start, R end);

    /**
     * 区间内筛选 （前开后开）
     */
    <R extends Comparable<R>> JDFrame<T> whereBetweenN(Function<T, R> function, R start, R end);

    /**
     * 区间内筛选 （前开后闭）
     */
    <R extends Comparable<R>> JDFrame<T> whereBetweenR(Function<T, R> function, R start, R end);

    /**
     * 区间内筛选 （前闭后开）
     */
    <R extends Comparable<R>> JDFrame<T> whereBetweenL(Function<T, R> function, R start, R end);

    /**
     * 区间外筛选 （前闭后闭）
     */
    <R extends Comparable<R>> JDFrame<T> whereNotBetween(Function<T, R> function, R start, R end);

    /**
     * 区间外筛选 （前开后开）
     */
    <R extends Comparable<R>> JDFrame<T> whereNotBetweenN(Function<T, R> function, R start, R end);

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

    /** ===========================   分组相关  ===================================== **/
    /**
     * 分组求和
     *
     * @param key     分组的字段
     * @param value 聚合的字段
     */
    <K> JDFrame<FI2<K, BigDecimal>> groupBySum(Function<T, K> key, ToBigDecimalFunction<T> value);

    /**
     * 分组求和
     *
     * @param key     分组K
     * @param key2    二级分组K
     * @param value 聚合字段
     */
    <K, J> JDFrame<FI3<K, J, BigDecimal>> groupBySum(Function<T, K> key, Function<T, J> key2, ToBigDecimalFunction<T> value);

    /**
     * 分组求和
     *
     * @param key     分组K
     * @param key2    二级分组K
     * @param key3    三级分组K
     * @param value 聚合字段
     */
    <K, J, H> JDFrame<FI4<K, J, H, BigDecimal>> groupBySum(Function<T, K> key,
                                                           Function<T, J> key2,
                                                           Function<T, H> key3,
                                                           ToBigDecimalFunction<T> value);

    /**
     * 分组求数量
     *
     * @param key 分组K
     */
    <K> JDFrame<FI2<K, Long>> groupByCount(Function<T, K> key);

    /**
     * 分组求数量
     *
     * @param key  分组K
     * @param key2 二级分组K
     */
    <K, J> JDFrame<FI3<K, J, Long>> groupByCount(Function<T, K> key, Function<T, J> key2);

    /**
     * 分组求数量
     *
     * @param key 分组K
     *          二级分组K
     *          三级分组K
     */
    <K, J, H> JDFrame<FI4<K, J, H, Long>> groupByCount(Function<T, K> key, Function<T, J> key2, Function<T, H> key3);


    /**
     * 分组求和及数量
     *
     * @param key     分组的字段
     * @param value 求和的字段
     * @return              FItem3<K, 和, 数量>
     */
    <K> JDFrame<FI3<K, BigDecimal,Long>> groupBySumCount(Function<T, K> key, ToBigDecimalFunction<T> value);

    /**
     * 分组求和及数量
     *
     * @param key               分组K
     * @param key2              二级分组K
     * @param value             求和字段
     * @return                  FItem4<K,K2, 和, 数量>
     */
    <K, J> JDFrame<FI4<K, J, BigDecimal, Long>> groupBySumCount(Function<T, K> key, Function<T, J> key2, ToBigDecimalFunction<T> value);


    /**
     * 分组求平均值
     *
     * @param key     分组的字段
     * @param value 聚合的字段
     */
    <K> JDFrame<FI2<K, BigDecimal>> groupByAvg(Function<T, K> key, ToBigDecimalFunction<T> value) ;

    /**
     * 分组求平均
     *
     * @param key     分组K
     * @param key2    二级分组K
     * @param value 聚合字段
     */
    <K, J> JDFrame<FI3<K, J, BigDecimal>> groupByAvg(Function<T, K> key, Function<T, J> key2, ToBigDecimalFunction<T> value);

    /**
     * 分组求平均
     *
     * @param key     分组K
     * @param key2    二级分组K
     * @param key3    三级分组K
     * @param value 聚合字段
     */
    <K, J, H> JDFrame<FI4<K, J, H, BigDecimal>> groupByAvg(Function<T, K> key,
                                                           Function<T, J> key2,
                                                           Function<T, H> key3,
                                                           ToBigDecimalFunction<T> value) ;


    /**
     * 分组求最大
     *
     * @param key     分组K
     * @param value 聚合字段
     */
    <K, V extends Comparable<V>> JDFrame<FI2<K, T>> groupByMax(Function<T, K> key, Function<T, V> value) ;
    /**
     * 分组求最小
     *
     * @param key     分组K
     * @param value 聚合字段
     */
    <K, V extends Comparable<V>> JDFrame<FI2<K, T>> groupByMin(Function<T, K> key, Function<T, V> value);

    /**
     * 分组求最大和最小值
     *
     * @param key     分组K
     * @param value 聚合字段
     */
    <K, V extends Comparable<V>> JDFrame<FI2<K, MaxMin<V>>> groupByMaxMinValue(Function<T, K> key, Function<T, V> value);

    /**
     * 分组求最大和最小值
     *
     * @param key     分组K
     * @param key2    二级分组K
     * @param value 聚合字段
     */
    <K, J, V extends Comparable<V>> JDFrame<FI3<K, J, MaxMin<V>>> groupByMaxMinValue(Function<T, K> key,
                                                                                     Function<T, J> key2,
                                                                                     Function<T, V> value);

    /**
     * 分组求最大和最小
     *
     * @param key     分组K
     * @param value 聚合字段
     */
    <K, V extends Comparable<V>> JDFrame<FI2<K, MaxMin<T>>> groupByMaxMin(Function<T, K> key,
                                                                          Function<T, V> value) ;

    /**
     * 分组求最大和最小
     *
     * @param key     分组K
     * @param key2   二级分组K
     * @param value 聚合字段
     */
    <K, J, V extends Comparable<V>> JDFrame<FI3<K, J, MaxMin<T>>> groupByMaxMin(Function<T, K> key,
                                                                                Function<T, J> key2,
                                                                                Function<T, V> value);
}
