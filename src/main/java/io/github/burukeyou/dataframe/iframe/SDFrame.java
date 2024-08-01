package io.github.burukeyou.dataframe.iframe;

import io.github.burukeyou.dataframe.iframe.function.ConsumerIndex;
import io.github.burukeyou.dataframe.iframe.function.NumberFunction;
import io.github.burukeyou.dataframe.iframe.function.ReplenishFunction;
import io.github.burukeyou.dataframe.iframe.function.SetFunction;
import io.github.burukeyou.dataframe.iframe.item.FI2;
import io.github.burukeyou.dataframe.iframe.item.FI3;
import io.github.burukeyou.dataframe.iframe.item.FI4;
import io.github.burukeyou.dataframe.iframe.support.*;
import io.github.burukeyou.dataframe.iframe.window.Sorter;
import io.github.burukeyou.dataframe.iframe.window.Window;
import io.github.burukeyou.dataframe.util.FrameUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Stream DataFrame
 *      The operations before and after are continuous, consistent with the stream flow,
 *      and some operations terminate the execution of the operation.
 *      The stream cannot be reused and needs to be re read to generate the stream, making it suitable for serial use
 *
 * @author caizhihao
 */
public interface SDFrame<T> extends IFrame<T> {

    /**
     * Convert a list to SDFrame
     */
    static <R> SDFrame<R> read(List<R> list) {
        return new SDFrameImpl<>(list);
    }

    /**
     * Convert a map to SDFrame
     */
    static <K,V> SDFrame<FI2<K,V>> read(Map<K,V> map) {
        return new SDFrameImpl<>(FrameUtil.toListFI2(map));
    }

    /**
     * Convert a map to SDFrame
     */
    static <K,J,V> SDFrame<FI3<K,J,V>> readMap(Map<K,Map<J,V>> map) {
        return new SDFrameImpl<>(FrameUtil.toListFI3(map));
    }

    /**
     * Convert to other SDFrame
     */
    <R> SDFrame<R> from(Stream<R> data);

    /**
     * Performs the given action for each element of the Iterable until all elements have been processed or the action throws an exception.
     */
    SDFrame<T> forEachDo(Consumer<? super T> action);


    /**
     * Performs the given action for each element of the Iterable until all elements have been processed or the action throws an exception.
     */
    SDFrame<T> forEachDo(ConsumerIndex<? super T> action);



    /**
     * ===========================   Frame Setting =====================================
     **/

    /**
     * Set default decimal places
     */
    SDFrame<T> defaultScale(int scale);

    /**
     *  Set default decimal places
     */
    SDFrame<T> defaultScale(int scale, RoundingMode roundingMode);


    /**
     * ===========================   Frame Info =====================================
     **/
    /**
     * print the 10 row to the console
     *
     */
    void show();

    /**
     * print the n row to the console
     */
    void show(int n);

    /**
     *  Get column headers
     */
    List<String> columns();

    /**
     *  Get a column value
     */
    <R> List<R> col(Function<T, R> function);


    /**
     * ===========================   Frame Join  =====================================
     **/
    /**
     * add element to Frame
     * @param t         element
     */
    SDFrame<T> append(T t);

    /**
     * add other Frame to this
     * @param other         other Frame
     */
    SDFrame<T> union(IFrame<T> other);

    /**
     * inner join Frame
     * @param other         other frame
     * @param on            connection conditions
     * @param join          Connection logic
     * @param <R>           new Frame type
     * @param <K>           other Frame type
     */
    <R,K> SDFrame<R> join(IFrame<K> other, JoinOn<T,K> on, Join<T,K,R> join);

    /**
     * inner join Frame
     *      such as {@link IFrame#join(IFrame, JoinOn, Join)}, but the default Join is {@link DefaultJoin},
     *      it will automatically map to a new Frame based on the same name
     * @param other         other frame
     * @param on            connection conditions
     * @param <R>           new Frame type
     * @param <K>           other Frame type
     */
    <R,K> SDFrame<R> join(IFrame<K> other, JoinOn<T,K> on);

    /**
     * left join Frame
     *      if connection conditions false, The callback value K for Join will be null， always keep T
     * @param other         other frame
     * @param on            connection conditions
     * @param join          Connection logic
     * @param <R>           new Frame type
     * @param <K>           other Frame type
     */
    <R,K> SDFrame<R> leftJoin(IFrame<K> other, JoinOn<T,K> on, Join<T,K,R> join);

    /**
     * left join Frame
     *        such as {@link IFrame#leftJoin(IFrame, JoinOn, Join)}, but the default Join is {@link DefaultJoin},
     * @param other         other frame
     * @param on            connection conditions
     * @param <R>           new Frame type
     * @param <K>           other Frame type
     */
    <R,K> SDFrame<R> leftJoin(IFrame<K> other, JoinOn<T,K> on);

    /**
     * right join Frame
     *      if connection conditions false, The callback value T for Join will be null， always keep K
     * @param other         other frame
     * @param on            connection conditions
     * @param join          Connection logic
     * @param <R>           new Frame type
     * @param <K>           other Frame type
     */
    <R,K> SDFrame<R> rightJoin(IFrame<K> other, JoinOn<T,K> on, Join<T,K,R> join);

    /**
     * right join Frame
     *        such as {@link IFrame#rightJoin(IFrame, JoinOn, Join)}, but the default Join is {@link DefaultJoin},
     * @param other         other frame
     * @param on            connection conditions
     * @param <R>           new Frame type
     * @param <K>           other Frame type
     */
    <R,K> SDFrame<R> rightJoin(IFrame<K> other, JoinOn<T,K> on);

    /**
     * ===========================   Frame Convert  =====================================
     */
    /**
     * convert to the new Frame
     * @param map           convert operation
     * @return              the new Frame
     * @param <R>           the new Frame type
     */
    <R> SDFrame<R> map(Function<T,R> map);

    /**
     * Percentage convert
     *          you can convert the value of a certain field to a percentage,
     *          Then assign a value to a certain column through SetFunction
     * @param get           need percentage convert field
     * @param set           field for storing percentage values
     * @param scale         percentage retain decimal places
     * @param <R>           the percentage field type
     */
    <R extends Number> SDFrame<T> mapPercent(Function<T,R> get, SetFunction<T,BigDecimal> set, int scale);

    /**
     * Percentage convert
     *    such as {@link IFrame#mapPercent(Function, SetFunction, int)}, but default scale is 2
     * @param get           need percentage convert field
     * @param set           field for storing percentage values
     */
    <R extends Number> SDFrame<T> mapPercent(Function<T,R> get, SetFunction<T,BigDecimal> set);

    /**
     * partition
     *      cut the matrix into multiple small matrices, with each matrix size n
     *
     * @param n         size of each zone
     */
    SDFrame<List<T>> partition(int n);

    /**
     * ddd ordinal column
     * @return                      FI2(T,Number)
     */
    SDFrame<FI2<T,Integer>> addRowNumberCol();

    /**
     * Sort by comparator first, then add ordinal columns
     * @param sorter    the sort
     */
    SDFrame<FI2<T,Integer>> addRowNumberCol(Sorter<T> sorter);

    /**
     * Add a numbered column to a specific column
     * @param set           specific column
     */
    SDFrame<T> addRowNumberCol(SetFunction<T,Integer> set);

    /**
     * Add a numbered column to a specific column
     * @param sorter    the sorter
     * @param set           specific column
     */
    SDFrame<T> addRowNumberCol(Sorter<T> sorter,SetFunction<T,Integer> set);

    /**
     * Add ranking columns by comparator
     *      Ranking logic, the same value means the Ranking is the same. This is different from {@link #addRowNumberCol}
     * @param sorter    the ranking  sorter
     */
    SDFrame<FI2<T,Integer>> addRankCol(Sorter<T> sorter);

    /**
     * Add ranking column to a certain column by Comparator
     * @param sorter            the ranking  comparator
     * @param set                   certain column
     */
    SDFrame<T> addRankCol(Sorter<T> sorter, SetFunction<T,Integer> set);


    /**
     * ===========================   Sort Frame  =====================================
     **/

    /**
     * Descending order
     * @param comparator         comparator
     */
    SDFrame<T> sortDesc(Comparator<T> comparator);

    /**
     * Descending order by field
     * @param function      sort field
     * @param <R>           the  sort field type
     */
    <R extends Comparable<? super R>> SDFrame<T> sortDesc(Function<T, R> function);

    /**
     * Ascending order
     * @param comparator         comparator
     */
    SDFrame<T> sortAsc(Comparator<T> comparator);

    /**
     * Ascending order
     * @param function      sort field
     */
    <R extends Comparable<R>> SDFrame<T> sortAsc(Function<T, R> function);


    /** ===========================   Cut Frame ===================================== **/

    /**
     *  Cut the top n element
     * @param n    the top n
     */
    SDFrame<T> cutFirst(int n);

    /**
     * Cut the last n element
     * @param n    the last n
     */
    SDFrame<T> cutLast(int n);


    /**
     * cut elements within the scope
     */
    SDFrame<T> cut(Integer startIndex,Integer endIndex);

    /**
     * cut paginated data
     * @param page              The current page number is considered as the first page, regardless of whether it is passed as 0 or 1
     * @param pageSize          page size
     */
    SDFrame<T> cutPage(int page,int pageSize);


    /**
     * Cut the top N rankings data
     *          The same value is considered to have the same ranking
     * @param sorter                the ranking sorter
     * @param n                     the top n
     */
    SDFrame<T> cutFirstRank(Sorter<T> sorter, int n);



    /** ===========================   View Frame  ===================================== **/

    /**
     * Get the first element
     */
    T head();

    /**
     * Get the first n elements
     */
    List<T> head(int n);

    /**
     * Get the last element
     */
    T tail();

    /**
     * Get the last n elements
     */
    List<T> tail(int n);

    /** ===========================   Distinct Frame  ===================================== **/

    /**
     * distinct by  T value
     */
    SDFrame<T> distinct();


    /**
     * distinct by field value
     * @param function          the field
     * @param <R>               field value type
     */
    <R extends Comparable<R>> SDFrame<T> distinct(Function<T, R> function);


    /**
     * distinct by  comparator
     * @param comparator        the comparator
     */
    <R extends Comparable<R>> SDFrame<T> distinct(Comparator<T> comparator);

    /**
     * Calculate the quantity after deduplication
     */
    <R extends Comparable<R>> long countDistinct(Function<T, R> function);

    /**
     * Calculate the quantity after deduplication
     */
    long countDistinct(Comparator<T> comparator);

    /**
     * ===========================   Where Frame  =====================================
     **/

    /**
     * filter by predicate
     * @param predicate         the predicate
     */
    SDFrame<T> where(Predicate<? super T> predicate);

    /**
     * Filter field values that are null, If it is string compatible, null and '' situations
     * @param function      the filter field
     * @param <R>           the filter field type
     */
    <R> SDFrame<T> whereNull(Function<T, R> function);

    /**
     * Filter field values that are not null,If it is string compatible, null and '' situations
     * @param function      the filter field
     * @param <R>           the filter field type
     */
    <R> SDFrame<T> whereNotNull(Function<T, R> function);

    /**
     * Screening within the interval,front closed and back closed.  [start,end]
     *             [start,end]
     * @param function          the filter field
     * @param start             start value
     * @param end               end value
     */
    <R extends Comparable<R>> SDFrame<T> whereBetween(Function<T, R> function, R start, R end);

    /**
     * Screening within the interval , front open and back open (start,end)
     * @param function          the filter field
     * @param start             start value
     * @param end               end value
     */
    <R extends Comparable<R>> SDFrame<T> whereBetweenN(Function<T, R> function, R start, R end);

    /**
     * Screening within the interval , front open and back close (start,end]
     * @param function          the filter field
     * @param start             start value
     * @param end               end value
     */
    <R extends Comparable<R>> SDFrame<T> whereBetweenR(Function<T, R> function, R start, R end);

    /**
     * Screening within the interval , front close and back open  [start,end)
     * @param function          the filter field
     * @param start             start value
     * @param end               end value
     */
    <R extends Comparable<R>> SDFrame<T> whereBetweenL(Function<T, R> function, R start, R end);


    /**
     * Out of range screening, (front closed and back closed)  [start,end]
     * @param function          the filter field
     * @param start             start value
     * @param end               end value
     */
    <R extends Comparable<R>> SDFrame<T> whereNotBetween(Function<T, R> function, R start, R end);

    /**
     * Out of range screening, (front open and back open)  (start,end)
     * @param function          the filter field
     * @param start             start value
     * @param end               end value
     */
    <R extends Comparable<R>> SDFrame<T> whereNotBetweenN(Function<T, R> function, R start, R end);

    /**
     * The query value is within the specified range
     * @param function          the filter field
     * @param list              specified range
     */
    <R> SDFrame<T> whereIn(Function<T, R> function, List<R> list);

    /**
     * The query value is outside the specified range
     * @param function          the filter field
     * @param list              specified range
     */
    <R> SDFrame<T> whereNotIn(Function<T, R> function, List<R> list);

    /**
     * filter true by predicate
     */
    SDFrame<T> whereTrue(Predicate<T> predicate);

    /**
     * filter not true by predicate
     */
    SDFrame<T> whereNotTrue(Predicate<T> predicate);

    /**
     * Filter equals
     * @param function      the field
     * @param value         need value
     */
    <R> SDFrame<T> whereEq(Function<T, R> function, R value);

    /**
     * Filter not equals
     * @param function      the field
     * @param value         not need value
     */
    <R> SDFrame<T> whereNotEq(Function<T, R> function, R value);

    /**
     * Filter Greater than value
     * @param function      the field
     * @param value         not need value
     */
    <R extends Comparable<R>> SDFrame<T> whereGt(Function<T, R> function, R value);

    /**
     * Filter Greater than or equal to
     * @param function      the field
     * @param value         not need value
     */
    <R extends Comparable<R>> SDFrame<T> whereGe(Function<T, R> function, R value);

    /**
     * Filter LESS than value
     * @param function      the field
     * @param value         not need value
     */
    <R extends Comparable<R>> SDFrame<T> whereLt(Function<T, R> function, R value);

    /**
     * Filter less than or equal to
     * @param function      the field
     * @param value         not need value
     */
    <R extends Comparable<R>> SDFrame<T> whereLe(Function<T, R> function, R value);

    /**
     * Fuzzy query contains specified values
     * @param function              the field
     * @param value                 query value
     */
    <R> SDFrame<T> whereLike(Function<T, R> function, R value);

    /**
     * Fuzzy query not contains specified values
     * @param function              the field
     * @param value                 query value
     */
    <R> SDFrame<T> whereNotLike(Function<T, R> function, R value);

    /**
     * prefix fuzzy query  contains specified values
     * @param function              the field
     * @param value                 query value
     */
    <R> SDFrame<T> whereLikeLeft(Function<T, R> function, R value);

    /**
     * suffix fuzzy query  contains specified values
     * @param function              the field
     * @param value                 query value
     */
    <R> SDFrame<T> whereLikeRight(Function<T, R> function, R value);

    /**
     * ===========================   Summary Frame  =====================================
     **/
    /**
     * Sum the values of the field
     * @param function      the  field
     */
    <R> BigDecimal sum(Function<T, R> function);

    /**
     * average the values of the field
     * @param function      the  field
     */
    <R> BigDecimal avg(Function<T, R> function);

    /**
     * Finding the maximum and minimum element
     * @param function      the  field
     */
    <R extends Comparable<? super R>> MaxMin<T> maxMin(Function<T, R> function);

    /**
     * Finding the maximum and minimum value
     * @param function      the  field
     */
    <R extends Comparable<? super R>> MaxMin<R> maxMinValue(Function<T, R> function);

    /**
     * Finding the maximum  element
     * @param function      the  field
     */
    <R extends Comparable<R>> T max(Function<T, R> function) ;

    /**
     * Finding the maximum  value
     * @param function      the  field
     */
    <R extends Comparable<? super R>> R maxValue(Function<T, R> function);

    /**
     * Finding the minimum  value
     * @param function      the  field
     */
    <R extends Comparable<? super R>> R minValue(Function<T, R> function);

    /**
     * Finding the minimum  element
     * @param function      the  field
     */
    <R extends Comparable<R>> T min(Function<T, R> function);

    /**
     * get row count
     */
    long count();


    /** ===========================   Group Frame  ===================================== **/


    /**
     * Group list
     * @param key        group field
     */
    <K> SDFrame<FI2<K,List<T>>> group(Function<? super T, ? extends K> key);

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


    /** ===========================   Window Function  ===================================== **/

    /**
     * open a window
     * @param window            window param
     */
     WindowSDFrame<T> window(Window<T> window);

    /**
     * open a empty window
     */
     WindowSDFrame<T> window();

    /**
     * rowNumber window function
     * @param overParam           window param
     */
     SDFrame<FI2<T,Integer>> overRowNumber(Window<T> overParam);

    /**
     * rowNumber window function
     */
     SDFrame<FI2<T,Integer>> overRowNumber();

    /**
     * rowNumber window function
     *              set the function result to the setFunction
     * @param setFunction            function result accept
     * @param overParam              window param
     */
    SDFrame<T> overRowNumberS(SetFunction<T,Integer> setFunction, Window<T> overParam);

    /**
     * rowNumber window function
     *              set the function result to the setFunction
     * @param setFunction            function result accept
     */
    SDFrame<T> overRowNumberS(SetFunction<T,Integer> setFunction);

    /**
     * rank window function
     * @param overParam           window param
     */
    SDFrame<FI2<T,Integer>> overRank(Window<T> overParam);

    /**
     *  rank window function
     *            set the function result to the setFunction
     * @param setFunction               function result accept
     * @param overParam                 window param
     */
     SDFrame<T> overRankS(SetFunction<T,Integer> setFunction, Window<T> overParam);

    /**
     * dense rank window function
     * @param overParam           window param
     */
     SDFrame<FI2<T,Integer>> overDenseRank(Window<T> overParam);

    /**
     *  dense rank window function
     *            set the function result to the setFunction
     * @param setFunction               function result accept
     * @param overParam                 window param
     */
     SDFrame<T> overDenseRankS(SetFunction<T,Integer> setFunction, Window<T> overParam);

    /**
     * Percent rank window function
     * @param overParam           window param
     */
     SDFrame<FI2<T,BigDecimal>> overPercentRank(Window<T> overParam);

    /**
     *  Percent rank window function
     *            set the function result to the setFunction
     * @param setFunction               function result accept
     * @param overParam                 window param
     */
     SDFrame<T> overPercentRankS(SetFunction<T,BigDecimal> setFunction, Window<T> overParam);

    /**
     * Cume Dist window function
     * @param overParam           window param
     */
     SDFrame<FI2<T,BigDecimal>> overCumeDist(Window<T> overParam);

    /**
     * Cume Dist window function
     *            set the function result to the setFunction
     * @param setFunction               function result accept
     * @param overParam                 window param
     */
     SDFrame<T> overCumeDistS(SetFunction<T,BigDecimal> setFunction, Window<T> overParam);

    /**
     * Lag window function
     *          get the value of the first n rows of the current row
     * @param overParam             window param
     * @param field                 field value
     * @param n                     first n rows
     */
    <F> SDFrame<FI2<T,F>> overLag(Window<T> overParam,Function<T,F> field,int n);

    /**
     * Lag window function
     *          get the value of the first n rows of the current row
     * @param setFunction                function result accept
     * @param overParam                  window param
     * @param field                      field value
     * @param n                          first n rows
     */
    <F> SDFrame<T> overLagS(SetFunction<T,F> setFunction,Window<T> overParam,Function<T,F> field,int n);

    /**
     * Lag window function
     *          get the value of the first n rows of the current row
     * @param field                      field value
     * @param n                          first n rows
     */
    <F> SDFrame<FI2<T,F>> overLag(Function<T,F> field,int n);

    /**
     * Lag window function
     *          get the value of the first n rows of the current row
     * @param setFunction                function result accept
     * @param field                      field value
     * @param n                          first n rows
     */
    <F> SDFrame<T> overLagS(SetFunction<T,F> setFunction,Function<T,F> field,int n);

    /**
     * lead window function
     *          get the value of the last n rows of the current row
     * @param overParam                  window param
     * @param field                      field value
     * @param n                          last n rows
     */
    <F> SDFrame<FI2<T,F>> overLead(Window<T> overParam,Function<T,F> field,int n);

    /**
     * lead window function
     *          get the value of the last n rows of the current row
     * @param setFunction                function result accept
     * @param overParam                  window param
     * @param field                      field value
     * @param n                          last n rows
     */
    <F> SDFrame<T> overLeadS(SetFunction<T,F> setFunction,Window<T> overParam,Function<T,F> field,int n);

    /**
     * lead window function
     *          get the value of the last n rows of the current row
     * @param field                      field value
     * @param n                          last n rows
     */
    <F> SDFrame<FI2<T,F>> overLead(Function<T,F> field,int n);

    /**
     * lead window function
     *          get the value of the last n rows of the current row
     * @param setFunction                function result accept
     * @param field                      field value
     * @param n                          last n rows
     */
    <F> SDFrame<T> overLeadS(SetFunction<T,F> setFunction,Function<T,F> field,int n);

    /**
     * NthValue window function
     *          get the Nth row within the window range
     * @param field                      field value
     * @param n                          last n rows
     */
    <F> SDFrame<FI2<T,F>> overNthValue(Window<T> overParam,Function<T,F> field,int n);

    /**
     * NthValue window function
     *          get the Nth row within the window range
     * @param setFunction                function result accept
     * @param overParam                  window param
     * @param field                      field value
     * @param n                          last n rows
     */
    <F> SDFrame<T> overNthValueS(SetFunction<T,F> setFunction,Window<T> overParam,Function<T,F> field,int n);

    /**
     * NthValue window function
     *          get the Nth row within the window range
     * @param field                      field value
     * @param n                          last n rows
     */
    <F> SDFrame<FI2<T,F>> overNthValue(Function<T,F> field,int n);

    /**
     * NthValue window function
     *          get the Nth row within the window range
     * @param setFunction                function result accept
     * @param field                      field value
     * @param n                          last n rows
     */
    <F> SDFrame<T> overNthValueS(SetFunction<T,F> setFunction,Function<T,F> field,int n);

    /**
     * FirstValue window function
     *          get the first row within the window range
     * @param field                      field value
     */
    <F> SDFrame<FI2<T,F>> overFirstValue(Window<T> overParam,Function<T,F> field);

    /**
     * FirstValue window function
     *          get the first row within the window range
     * @param setFunction                function result accept
     * @param overParam                  window param
     * @param field                      field value
     */
    <F> SDFrame<T> overFirstValueS(SetFunction<T,F> setFunction,Window<T> overParam,Function<T,F> field);

    /**
     * FirstValue window function
     *          get the first row within the window range
     * @param field                      field value
     */
    <F> SDFrame<FI2<T,F>> overFirstValue(Function<T,F> field);

    /**
     * FirstValue window function
     *          get the first row within the window range
     * @param setFunction                function result accept
     * @param field                      field value
     */
    <F> SDFrame<T> overFirstValueS(SetFunction<T,F> setFunction,Function<T,F> field);

    /**
     * LastValue window function
     *          get the last row within the window range
     * @param overParam                  window param
     * @param field                      field value
     */
    <F> SDFrame<FI2<T,F>> overLastValue(Window<T> overParam,Function<T,F> field);

    /**
     * LastValue window function
     *          get the last row within the window range
     * @param setFunction                function result accept
     * @param overParam                  window param
     * @param field                      field value
     */
    <F> SDFrame<T> overLastValueS(SetFunction<T,F> setFunction,Window<T> overParam,Function<T,F> field);

    /**
     * LastValue window function
     *          get the last row within the window range
     * @param field                      field value
     */
    <F> SDFrame<FI2<T,F>> overLastValue(Function<T,F> field);

    /**
     * LastValue window function
     *          get the last row within the window range
     * @param setFunction                function result accept
     * @param field                      field value
     */
    <F> SDFrame<T> overLastValueS(SetFunction<T,F> setFunction,Function<T,F> field);

    /**
     * Sum window function
     *         calculate the sum within the window range
     * @param overParam                  window param
     * @param field                      field value
     */
    <F> SDFrame<FI2<T,BigDecimal>> overSum(Window<T> overParam,Function<T,F> field);

    /**
     * Sum window function
     *         calculate the sum within the window range
     * @param field                      field value
     */
    <F> SDFrame<FI2<T,BigDecimal>> overSum(Function<T,F> field);

    /**
     * Sum window function
     *         calculate the sum within the window range
     * @param setFunction                function result accept
     * @param overParam                  window param
     * @param field                      field value
     */
    <F> SDFrame<T> overSumS(SetFunction<T,BigDecimal> setFunction, Window<T> overParam, Function<T,F> field);


    /**
     * Sum window function
     *         calculate the sum within the window range
     * @param setFunction                function result accept
     * @param field                      field value
     */
    <F> SDFrame<T> overSumS(SetFunction<T,BigDecimal> setFunction, Function<T,F> field);

    /**
     * avg window function
     *         calculate the avg value within the window range
     * @param overParam                  window param
     * @param field                      field value
     */
    <F> SDFrame<FI2<T,BigDecimal>> overAvg(Window<T> overParam,Function<T,F> field);

    /**
     * avg window function
     *         calculate the avg value within the window range
     * @param field                      field value
     */
    <F> SDFrame<FI2<T,BigDecimal>> overAvg(Function<T,F> field);

    /**
     * avg window function
     *         calculate the avg value within the window range
     * @param setFunction                function result accept
     * @param overParam                  window param
     * @param field                      field value
     */
    <F> SDFrame<T> overAvgS(SetFunction<T,BigDecimal> setFunction, Window<T> overParam, Function<T,F> field);

    /**
     * avg window function
     *         calculate the avg value within the window range
     * @param setFunction                function result accept
     * @param field                      field value
     */
    <F> SDFrame<T> overAvgS(SetFunction<T,BigDecimal> setFunction, Function<T,F> field);

    /**
     * max window function
     *         calculate the max value within the window range
     * @param overParam                  window param
     * @param field                      field value
     */
    <F extends Comparable<? super F>> SDFrame<FI2<T,F>> overMaxValue(Window<T> overParam,Function<T,F> field);

    /**
     * max window function
     *         calculate the max value within the window range
     * @param field                      field value
     */
    <F extends Comparable<? super F>> SDFrame<FI2<T,F>> overMaxValue(Function<T,F> field);

    /**
     * max window function
     *         calculate the max value within the window range
     * @param setFunction                function result accept
     * @param overParam                  window param
     * @param field                      field value
     */
    <F extends Comparable<? super F>> SDFrame<T> overMaxValueS(SetFunction<T,F> setFunction, Window<T> overParam, Function<T,F> field);

    /**
     * max window function
     *         calculate the max value within the window range
     * @param setFunction                function result accept
     * @param field                      field value
     */
    <F extends Comparable<? super F>> SDFrame<T> overMaxValueS(SetFunction<T,F> setFunction, Function<T,F> field);

    /**
     * min window function
     *         calculate the min value within the window range
     * @param overParam                  window param
     * @param field                      field value
     */
    <F extends Comparable<? super F>> SDFrame<FI2<T,F>> overMinValue(Window<T> overParam,Function<T,F> field);

    /**
     * min window function
     *         calculate the min value within the window range
     * @param field                      field value
     */
    <F extends Comparable<? super F>> SDFrame<FI2<T,F>> overMinValue(Function<T,F> field);

    /**
     * min window function
     *         calculate the min value within the window range
     * @param setFunction                function result accept
     * @param overParam                  window param
     * @param field                      field value
     */
    <F extends Comparable<? super F>> SDFrame<T> overMinValueS(SetFunction<T,F> setFunction, Window<T> overParam, Function<T,F> field);

    /**
     * min window function
     *         calculate the min value within the window range
     * @param setFunction                function result accept
     * @param field                      field value
     */
    <F extends Comparable<? super F>> SDFrame<T> overMinValueS(SetFunction<T,F> setFunction, Function<T,F> field);

    /**
     * count window function
     *         calculate the count within the window range
     * @param overParam                  window param
     */
    SDFrame<FI2<T,Integer>> overCount(Window<T> overParam);

    /**
     * count window function
     *         calculate the count within the window range
     */
    SDFrame<FI2<T,Integer>> overCount();

    /**
     * count window function
     *         calculate the count within the window range
     * @param setFunction                function result accept
     * @param overParam                  window param
     */
    SDFrame<T> overCountS(SetFunction<T,Integer> setFunction, Window<T> overParam);

    /**
     * count window function
     *         calculate the count within the window range
     * @param setFunction                function result accept
     */
    SDFrame<T> overCountS(SetFunction<T,Integer> setFunction);

    /**
     * Ntile window function
     *         assign bucket numbers evenly to windows, starting from 1
     * @param n              size of buckets
     */
    SDFrame<FI2<T,Integer>> overNtile(int n);


    /**
     * Ntile window function
     *         assign bucket numbers evenly to windows, starting from 1
     * @param n              size of buckets
     * @param overParam                  window param
     */
    SDFrame<FI2<T,Integer>> overNtile(Window<T> overParam, int n);

    /**
     * Ntile window function
     *         assign bucket numbers evenly to windows, starting from 1
     * @param setFunction                function result accept
     * @param n              size of buckets
     * @param overParam                  window param
     */
    SDFrame<T> overNtileS(SetFunction<T,Integer> setFunction,Window<T> overParam, int n);

    /**
     * Ntile window function
     *         assign bucket numbers evenly to windows, starting from 1
     * @param setFunction                function result accept
     * @param n              size of buckets
     */
    SDFrame<T> overNtileS(SetFunction<T,Integer> setFunction, int n);


    /** ===========================   Other  ===================================== **/

    /**
     * Summarize all collectDim values, calculate the difference between them, and then add the missing difference to the Frame through getEmptyObject
     *
     */
    <C> SDFrame<T> replenish(Function<T, C> collectDim, List<C> allDim, Function<C,T> getEmptyObject);

    /**
     * Calculate the difference in groups and then add the difference to that group
     *
     *  according to the groupDim dimension, and then summarize all collectDim fields within each group
     *  After summarizing, calculate the difference sets with allAbscissa, which are the entries that need to be supplemented.
     *  Then, generate empty objects according to the ReplenishFunction logic and add them to the group
     *
     * @param groupDim              Dimension fields for grouping
     * @param collectDim            Data fields collected within the group
     * @param allDim                All dimensions that need to be displayed within the group
     * @param getEmptyObject        Logic for generating empty objects
     *
     * @param <G>        The type of grouping
     * @param <C>        type of collection within the group
     *
     *The set supplemented by @ return
     */
    <G, C> SDFrame<T> replenish(Function<T, G> groupDim,
                                Function<T, C> collectDim,
                                List<C> allDim,
                                ReplenishFunction<G,C,T> getEmptyObject);

    /**
     *  such as {@link IFrame#replenish(Function, Function, List, ReplenishFunction)}, but can not Specify allDim，
     *  will auto generate allDim, The default allDim is the value of all collectDim fields in the set
     *
     * @param groupDim              Dimension fields for grouping
     * @param collectDim            Data fields collected within the group
     * @param getEmptyObject        Logic for generating empty objects
     *
     * @param <G>        The type of grouping
     * @param <C>        type of collection within the group
     */
    <G, C> SDFrame<T> replenish(Function<T, G> groupDim, Function<T, C> collectDim, ReplenishFunction<G,C,T> getEmptyObject);

}
