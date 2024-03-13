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
import java.util.stream.Stream;

/**
 * 
 * @author caizhihao
 */
public interface IFrame<T> extends Iterable<T>{


    List<T> toLists();

    Stream<T> stream();


    /**
     * ===========================   矩阵信息 =====================================
     **/
    /**
     * 打印
     */
    void show();

    /**
     * 打印前N行
     * @param n
     */
    void show(int n);

    /**
     *  获取列头
     */
    List<String> columns();

    /**
     *  获取某一列信息
     */
    <R> List<R> col(Function<T, R> function);


    /**
     * ===========================   连接矩阵  =====================================
     **/
    IFrame<T> append(T t);

    IFrame<T> union(IFrame<T> other);

    /**
     * 内连接
     */
    <R,K> IFrame<R> join(IFrame<K> other, JoinOn<T,K> on, Join<T,K,R> join);

    <R,K> IFrame<R> join(IFrame<K> other, JoinOn<T,K> on);

    /**
     * 左连接
     * @param other         连接的矩阵
     * @param on            连接条件
     * @param join          连接逻辑
     */
    <R,K> IFrame<R> leftJoin(IFrame<K> other, JoinOn<T,K> on, Join<T,K,R> join);

    <R,K> IFrame<R> leftJoin(IFrame<K> other, JoinOn<T,K> on);

    <R,K> IFrame<R> rightJoin(IFrame<K> other, JoinOn<T,K> on, Join<T,K,R> join);

    <R,K> IFrame<R> rightJoin(IFrame<K> other, JoinOn<T,K> on);

    /**
     * ===========================   矩阵变换  =====================================
     **/

    <R> IFrame<R> map(Function<T,R> map);

    /**
     * 百分比转换
     */
    <R extends Number> IFrame<T> mapPercent(Function<T,R> get, SetFunction<T,BigDecimal> set, int scale);

    <R extends Number> IFrame<T> mapPercent(Function<T,R> get, SetFunction<T,BigDecimal> set);

    /**
     * 分区
     */
    IFrame<List<T>> partition(int n);

    /**
     * 添加序号列
     */
    IFrame<FI2<T,Integer>> addSortNoCol();

    /**
     * 添加序号列，按照比较器
     */
    IFrame<FI2<T,Integer>> addSortNoCol(Comparator<T> comparator);

    /**
     * 添加序号列，按照字段
     */
    <R extends Comparable<R>>  IFrame<FI2<T,Integer>> addSortNoCol(Function<T, R> function);

    /**
     * 添加序号列，到某一列
     */
    IFrame<T> addSortNoCol(SetFunction<T,Integer> set);

    /**
     * 添加排名列，按照比较器
     *      排名逻辑，相同值认为名字一样
     */
    IFrame<FI2<T,Integer>> addRankingSameCol(Comparator<T> comparator);

    /**
     * 添加排名列，按照字段
     */
    <R extends Comparable<R>> IFrame<FI2<T,Integer>> addRankingSameCol(Function<T, R> function);

    /**
     * 添加排名列，到某一列
     */
    IFrame<T> addRankingSameCol(Comparator<T> comparator,SetFunction<T,Integer> set);

    <R extends Comparable<R>>  IFrame<T> addRankingSameCol(Function<T, R> function,SetFunction<T,Integer> set);


    /**
     * ===========================   排序相关  =====================================
     **/

    /**
     * 降序
     */
    IFrame<T> sortDesc(Comparator<T> comparator);

    <R extends Comparable<R>> IFrame<T> sortDesc(Function<T, R> function);

    /**
     * 升序
     */
    IFrame<T> sortAsc(Comparator<T> comparator);

    <R extends Comparable<R>> IFrame<T> sortAsc(Function<T, R> function);


    /** ===========================   截取相关  ===================================== **/


    /**
     *  截取前n个
     */
    IFrame<T> subFirst(int n);

    /**
     * 截取后n个
     */
    IFrame<T> subLast(int n);

    /**
     * 按照排名截取前n个
     *          相同值认为排名一样
     * @param comparator
     * @param n
     * @return
     */
    IFrame<T> subRankingSameAsc(Comparator<T> comparator, int n);

    <R extends Comparable<R>> IFrame<T> subRankingSameAsc(Function<T, R> function, int n);

    IFrame<T> subRankingSameDesc(Comparator<T> comparator, int n);

    <R extends Comparable<R>> IFrame<T> subRankingSameDesc(Function<T, R> function, int n);


    /** ===========================   查看相关  ===================================== **/

    /**
     * 获取第一个元素
     */
    T head();

    /**
     * 获取前n个元素
     */
    List<T> head(int n);

    /**
     * 获取最后一个元素
     */
    T tail();

    /**
     * 获取后n个元素
     */
    List<T> tail(int n);

    /** ===========================   去重相关  ===================================== **/

    /**
     * 去重
     */
    IFrame<T> distinct();


    /**
     * 去重，按照字段
     */
    <R extends Comparable<R>> IFrame<T> distinct(Function<T, R> function);

    /**
     * 去重，按照比较器
     */
    <R extends Comparable<R>> IFrame<T> distinct(Comparator<T> comparator);

    /**
     * 去重后数量，按照字段
     */
    <R extends Comparable<R>> long countDistinct(Function<T, R> function);

    long countDistinct(Comparator<T> comparator);


    /**
     * ===========================   筛选相关  =====================================
     **/

    IFrame<T> where(Predicate<? super T> predicate);

    /**
     * 筛选值为空的
     *      如果是字符串兼容了，null 和 ''情况
     */
    <R> IFrame<T> whereNull(Function<T, R> function);

    /**
     * 筛选值不为空的
     */
    <R> IFrame<T> whereNotNull(Function<T, R> function);

    /**
     * 区间内筛选 （前闭后闭）
     */
    <R extends Comparable<R>> IFrame<T> whereBetween(Function<T, R> function, R start, R end);

    /**
     * 区间内筛选 （前开后开）
     */
    <R extends Comparable<R>> IFrame<T> whereBetweenN(Function<T, R> function, R start, R end);

    /**
     * 区间内筛选 （前开后闭）
     */
    <R extends Comparable<R>> IFrame<T> whereBetweenR(Function<T, R> function, R start, R end);

    /**
     * 区间内筛选 （前闭后开）
     */
    <R extends Comparable<R>> IFrame<T> whereBetweenL(Function<T, R> function, R start, R end);

    /**
     * 区间外筛选 （前闭后闭）
     */
    <R extends Comparable<R>> IFrame<T> whereNotBetween(Function<T, R> function, R start, R end);

    /**
     * 区间外筛选 （前开后开）
     */
    <R extends Comparable<R>> IFrame<T> whereNotBetweenN(Function<T, R> function, R start, R end);

    /**
     *
     */
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

    <R> BigDecimal avg(Function<T, R> function);

    <R extends Comparable<R>> MaxMin<T> maxMin(Function<T, R> function);

    <R extends Comparable<R>> MaxMin<R> maxMinValue(Function<T, R> function);

    <R extends Comparable<R>> T max(Function<T, R> function) ;

    <R extends Comparable<R>> R maxValue(Function<T, R> function);

    <R extends Comparable<R>> R minValue(Function<T, R> function);

    <R extends Comparable<R>> T min(Function<T, R> function);

    long count();


    /** ===========================   分组相关  ===================================== **/
    /**
     * 分组求和
     *
     * @param key       分组的字段
     * @param value     聚合的字段
     */
    <K> IFrame<FI2<K, BigDecimal>> groupBySum(Function<T, K> key, ToBigDecimalFunction<T> value);

    /**
     * 分组求和
     *
     * @param key       分组K
     * @param key2      二级分组K
     * @param value     聚合字段
     */
    <K, J> IFrame<FI3<K, J, BigDecimal>> groupBySum(Function<T, K> key, Function<T, J> key2, ToBigDecimalFunction<T> value);

    /**
     * 分组求和
     *
     * @param key     分组K
     * @param key2    二级分组K
     * @param key3    三级分组K
     * @param value   聚合字段
     */
    <K, J, H> IFrame<FI4<K, J, H, BigDecimal>> groupBySum(Function<T, K> key,
                                                          Function<T, J> key2,
                                                          Function<T, H> key3,
                                                          ToBigDecimalFunction<T> value);

    /**
     * 分组求数量
     *
     * @param key   分组K
     */
    <K> IFrame<FI2<K, Long>> groupByCount(Function<T, K> key);

    /**
     * 分组求数量
     *
     * @param key   分组K
     * @param key2  二级分组K
     */
    <K, J> IFrame<FI3<K, J, Long>> groupByCount(Function<T, K> key, Function<T, J> key2);

    /**
     * 分组求数量
     *
     * @param key     分组K
     * @param key2    二级分组K
     * @param key3    三级分组K
     */
    <K, J, H> IFrame<FI4<K, J, H, Long>> groupByCount(Function<T, K> key, Function<T, J> key2, Function<T, H> key3);


    /**
     * 分组求和及数量
     *
     * @param key           分组的字段
     * @param value         求和的字段
     * @return              FItem3<key, 和, 数量>
     */
    <K> IFrame<FI3<K, BigDecimal,Long>> groupBySumCount(Function<T, K> key, ToBigDecimalFunction<T> value);

    /**
     * 分组求和及数量
     *
     * @param key           分组K
     * @param key2          二级分组K
     * @param value         求和字段
     * @return              FItem4<key,K2, 和, 数量>
     */
    <K, J> IFrame<FI4<K, J, BigDecimal, Long>> groupBySumCount(Function<T, K> key, Function<T, J> key2, ToBigDecimalFunction<T> value);


    /**
     * 分组求平均值
     *
     * @param key     分组的字段
     * @param value 聚合的字段
     */
    <K> IFrame<FI2<K, BigDecimal>> groupByAvg(Function<T, K> key, ToBigDecimalFunction<T> value) ;

    /**
     * 分组求平均
     *
     * @param key     分组K
     * @param key2    二级分组K
     * @param value 聚合字段
     */
    <K, J> IFrame<FI3<K, J, BigDecimal>> groupByAvg(Function<T, K> key, Function<T, J> key2, ToBigDecimalFunction<T> value);

    /**
     * 分组求平均
     *
     * @param key     分组K
     * @param key2    二级分组K
     * @param key3    三级分组K
     * @param value 聚合字段
     */
    <K, J, H> IFrame<FI4<K, J, H, BigDecimal>> groupByAvg(Function<T, K> key,
                                                          Function<T, J> key2,
                                                          Function<T, H> key3,
                                                          ToBigDecimalFunction<T> value) ;

    /**
     * 分组求最大
     *
     * @param key     分组K
     * @param value 聚合字段
     */
    <K, V extends Comparable<V>> IFrame<FI2<K, T>> groupByMax(Function<T, K> key, Function<T, V> value) ;
    /**
     * 分组求最小
     *
     * @param key     分组K
     * @param value 聚合字段
     */
    <K, V extends Comparable<V>> IFrame<FI2<K, T>> groupByMin(Function<T, K> key, Function<T, V> value);

    /**
     * 分组求最大和最小值
     *
     * @param key     分组K
     * @param value 聚合字段
     */
    <K, V extends Comparable<V>> IFrame<FI2<K, MaxMin<V>>> groupByMaxMinValue(Function<T, K> key, Function<T, V> value);

    /**
     * 分组求最大和最小值
     *
     * @param key     分组K
     * @param key2    二级分组K
     * @param value 聚合字段
     */
    <K, J, V extends Comparable<V>> IFrame<FI3<K, J, MaxMin<V>>> groupByMaxMinValue(Function<T, K> key,
                                                                                    Function<T, J> key2,
                                                                                    Function<T, V> value);

    /**
     * 分组求最大和最小
     *
     * @param key     分组K
     * @param value 聚合字段
     */
    <K, V extends Comparable<V>> IFrame<FI2<K, MaxMin<T>>> groupByMaxMin(Function<T, K> key,
                                                                         Function<T, V> value) ;

    /**
     * 分组求最大和最小
     *
     * @param key     分组K
     * @param key2   二级分组K
     * @param value 聚合字段
     */
    <K, J, V extends Comparable<V>> IFrame<FI3<K, J, MaxMin<T>>> groupByMaxMin(Function<T, K> key,
                                                                               Function<T, J> key2,
                                                                               Function<T, V> value);

}
