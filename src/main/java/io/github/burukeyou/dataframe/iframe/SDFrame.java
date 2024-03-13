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
 *      The operations before and after are continuous, consistent with the stream flow,
 *      and some operations terminate the execution of the operation.
 *      The stream cannot be reused and needs to be re read to generate the stream, making it suitable for serial use
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
    SDFrame<T> sortDesc(Comparator<T> comparator);

    <R extends Comparable<R>> SDFrame<T> sortDesc(Function<T, R> function);

    SDFrame<T> sortAsc(Comparator<T> comparator);

    <R extends Comparable<R>> SDFrame<T> sortAsc(Function<T, R> function);

    SDFrame<T> catRankingSameAsc(Comparator<T> comparator, int n);

    <R extends Comparable<R>> SDFrame<T> catRankingSameAsc(Function<T, R> function, int n);

    SDFrame<T> catRankingSameDesc(Comparator<T> comparator, int n);

    <R extends Comparable<R>> SDFrame<T> catRankingSameDesc(Function<T, R> function, int n);
    SDFrame<T> cutFirst(int n);

    SDFrame<T> catLast(int n);
    SDFrame<T> distinct();

    <R extends Comparable<R>> SDFrame<T> distinct(Function<T, R> function);

    <R extends Comparable<R>> SDFrame<T> distinct(Comparator<T> comparator);
    SDFrame<T> where(Predicate<? super T> predicate);
    <R> SDFrame<T> whereNull(Function<T, R> function);

    <R> SDFrame<T> whereNotNull(Function<T, R> function);
    <R extends Comparable<R>> SDFrame<T> whereBetween(Function<T, R> function, R start, R end);
    <R extends Comparable<R>> SDFrame<T> whereBetweenN(Function<T, R> function, R start, R end);

    <R extends Comparable<R>> SDFrame<T> whereBetweenR(Function<T, R> function, R start, R end);

    <R extends Comparable<R>> SDFrame<T> whereBetweenL(Function<T, R> function, R start, R end);

    <R extends Comparable<R>> SDFrame<T> whereNotBetween(Function<T, R> function, R start, R end);

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

    <K> SDFrame<FI2<K, BigDecimal>> groupBySum(Function<T, K> key, BigDecimalFunction<T> value);

    <K, J> SDFrame<FI3<K, J, BigDecimal>> groupBySum(Function<T, K> key, Function<T, J> key2, BigDecimalFunction<T> value);

    <K, J, H> SDFrame<FI4<K, J, H, BigDecimal>> groupBySum(Function<T, K> key,
                                                           Function<T, J> key2,
                                                           Function<T, H> key3,
                                                           BigDecimalFunction<T> value);

    <K> SDFrame<FI2<K, Long>> groupByCount(Function<T, K> key);

    <K, J> SDFrame<FI3<K, J, Long>> groupByCount(Function<T, K> key, Function<T, J> key2);

    <K, J, H> SDFrame<FI4<K, J, H, Long>> groupByCount(Function<T, K> key, Function<T, J> key2, Function<T, H> key3);

    <K> SDFrame<FI3<K, BigDecimal,Long>> groupBySumCount(Function<T, K> key, BigDecimalFunction<T> value);

    <K, J> SDFrame<FI4<K, J, BigDecimal, Long>> groupBySumCount(Function<T, K> key, Function<T, J> key2, BigDecimalFunction<T> value);

    <K> SDFrame<FI2<K, BigDecimal>> groupByAvg(Function<T, K> key, BigDecimalFunction<T> value) ;

    <K, J> SDFrame<FI3<K, J, BigDecimal>> groupByAvg(Function<T, K> key, Function<T, J> key2, BigDecimalFunction<T> value);

    <K, J, H> SDFrame<FI4<K, J, H, BigDecimal>> groupByAvg(Function<T, K> key,
                                                           Function<T, J> key2,
                                                           Function<T, H> key3,
                                                           BigDecimalFunction<T> value) ;

    <K, V extends Comparable<V>> SDFrame<FI2<K, T>> groupByMax(Function<T, K> key, Function<T, V> value) ;

    <K, V extends Comparable<V>> SDFrame<FI2<K, T>> groupByMin(Function<T, K> key, Function<T, V> value);


    <K, V extends Comparable<V>> SDFrame<FI2<K, MaxMin<V>>> groupByMaxMinValue(Function<T, K> key, Function<T, V> value);

    <K, J, V extends Comparable<V>> SDFrame<FI3<K, J, MaxMin<V>>> groupByMaxMinValue(Function<T, K> key,
                                                                                     Function<T, J> key2,
                                                                                     Function<T, V> value);
    <K, V extends Comparable<V>> SDFrame<FI2<K, MaxMin<T>>> groupByMaxMin(Function<T, K> key,
                                                                          Function<T, V> value) ;

    <K, J, V extends Comparable<V>> SDFrame<FI3<K, J, MaxMin<T>>> groupByMaxMin(Function<T, K> key,
                                                                                Function<T, J> key2,
                                                                                Function<T, V> value);
}
