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
 *      The operations before and after are discontinuous, and all operations take effect in real time.
 *      Even after executing the termination operation, they can still be reused without the need to re read to generate a stream
 *
 * @author  caizhihao
 */
public interface JDFrame<T> extends IFrame<T> {

     static <T> JDFrame<T> read(List<T> list) {
        return new JDFrameImpl<>(list);
    }


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

    JDFrame<T> sortDesc(Comparator<T> comparator);

    <R extends Comparable<R>> JDFrame<T> sortDesc(Function<T, R> function);

    JDFrame<T> sortAsc(Comparator<T> comparator);

    <R extends Comparable<R>> JDFrame<T> sortAsc(Function<T, R> function);


    JDFrame<T> catRankingSameAsc(Comparator<T> comparator, int n);

    <R extends Comparable<R>> JDFrame<T> catRankingSameAsc(Function<T, R> function, int n);

    JDFrame<T> catRankingSameDesc(Comparator<T> comparator, int n);

    <R extends Comparable<R>> JDFrame<T> catRankingSameDesc(Function<T, R> function, int n);

    JDFrame<T> cutFirst(int n);


    JDFrame<T> catLast(int n);

    JDFrame<T> distinct();

    <R extends Comparable<R>> JDFrame<T> distinct(Function<T, R> function);

    <R extends Comparable<R>> JDFrame<T> distinct(Comparator<T> comparator);

    JDFrame<T> where(Predicate<? super T> predicate);

    <R> JDFrame<T> whereNull(Function<T, R> function);

    <R> JDFrame<T> whereNotNull(Function<T, R> function);

    <R extends Comparable<R>> JDFrame<T> whereBetween(Function<T, R> function, R start, R end);

    <R extends Comparable<R>> JDFrame<T> whereBetweenN(Function<T, R> function, R start, R end);

    <R extends Comparable<R>> JDFrame<T> whereBetweenR(Function<T, R> function, R start, R end);

    <R extends Comparable<R>> JDFrame<T> whereBetweenL(Function<T, R> function, R start, R end);

    <R extends Comparable<R>> JDFrame<T> whereNotBetween(Function<T, R> function, R start, R end);

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

    <K> JDFrame<FI2<K, BigDecimal>> groupBySum(Function<T, K> key, BigDecimalFunction<T> value);

    <K, J> JDFrame<FI3<K, J, BigDecimal>> groupBySum(Function<T, K> key, Function<T, J> key2, BigDecimalFunction<T> value);

    <K, J, H> JDFrame<FI4<K, J, H, BigDecimal>> groupBySum(Function<T, K> key,
                                                           Function<T, J> key2,
                                                           Function<T, H> key3,
                                                           BigDecimalFunction<T> value);
    <K> JDFrame<FI2<K, Long>> groupByCount(Function<T, K> key);

    <K, J> JDFrame<FI3<K, J, Long>> groupByCount(Function<T, K> key, Function<T, J> key2);
    <K, J, H> JDFrame<FI4<K, J, H, Long>> groupByCount(Function<T, K> key, Function<T, J> key2, Function<T, H> key3);

    <K> JDFrame<FI3<K, BigDecimal,Long>> groupBySumCount(Function<T, K> key, BigDecimalFunction<T> value);

    <K, J> JDFrame<FI4<K, J, BigDecimal, Long>> groupBySumCount(Function<T, K> key, Function<T, J> key2, BigDecimalFunction<T> value);

    <K> JDFrame<FI2<K, BigDecimal>> groupByAvg(Function<T, K> key, BigDecimalFunction<T> value) ;

    <K, J> JDFrame<FI3<K, J, BigDecimal>> groupByAvg(Function<T, K> key, Function<T, J> key2, BigDecimalFunction<T> value);

    <K, J, H> JDFrame<FI4<K, J, H, BigDecimal>> groupByAvg(Function<T, K> key,
                                                           Function<T, J> key2,
                                                           Function<T, H> key3,
                                                           BigDecimalFunction<T> value) ;

    <K, V extends Comparable<V>> JDFrame<FI2<K, T>> groupByMax(Function<T, K> key, Function<T, V> value) ;

    <K, V extends Comparable<V>> JDFrame<FI2<K, T>> groupByMin(Function<T, K> key, Function<T, V> value);


    <K, V extends Comparable<V>> JDFrame<FI2<K, MaxMin<V>>> groupByMaxMinValue(Function<T, K> key, Function<T, V> value);

    <K, J, V extends Comparable<V>> JDFrame<FI3<K, J, MaxMin<V>>> groupByMaxMinValue(Function<T, K> key,
                                                                                     Function<T, J> key2,
                                                                                     Function<T, V> value);

    <K, V extends Comparable<V>> JDFrame<FI2<K, MaxMin<T>>> groupByMaxMin(Function<T, K> key,
                                                                          Function<T, V> value) ;

    <K, J, V extends Comparable<V>> JDFrame<FI3<K, J, MaxMin<T>>> groupByMaxMin(Function<T, K> key,
                                                                                Function<T, J> key2,
                                                                                Function<T, V> value);
}
