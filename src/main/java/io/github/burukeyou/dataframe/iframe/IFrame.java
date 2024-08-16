package io.github.burukeyou.dataframe.iframe;

import io.github.burukeyou.dataframe.iframe.function.ConsumerIndex;
import io.github.burukeyou.dataframe.iframe.function.ListSelectOneFunction;
import io.github.burukeyou.dataframe.iframe.function.ReplenishFunction;
import io.github.burukeyou.dataframe.iframe.function.SetFunction;
import io.github.burukeyou.dataframe.iframe.item.FI2;
import io.github.burukeyou.dataframe.iframe.window.Sorter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * A Simple DataFrame Stream API Interface define
 *
 *
 * @author caizhihao
 */
public interface IFrame<T> extends ISummaryFrame<T>, IWhereFrame<T>, IJoinFrame<T>, IGroupFrame<T>, IOperationFrame<T>,Iterable<T>{


    /**
     * convert to data by Collector, same as stream Collector
     * @param collector     the {@code Collector} describing the reduction
     * @param <R>           the type of the result
     * @param <A>           the intermediate accumulation type of the {@code Collector}
     * @return              the result of the reduction
     */
    <R, A> R collect(Collector<? super T, A, R> collector);

    /**
     * Convert to list
     * @return      the list
     */
    List<T> toLists();

    /**
     * Convert to Array
     * @return      the Array， if Frame is empty will return null rather than empty array
     */
    T[] toArray();

    /**
     * Convert to Array
     * @param  elementClass         the array element class type
     * @return      the Array， Even if the Frame is empty, it will return an empty array instead of null
     */
    T[] toArray(Class<T> elementClass);

    /**
     * Convert to Map
     * @param keyMapper       a mapping function to produce keys
     * @param valueMapper     a mapping function to produce values
     */
    <K,V> Map<K,V> toMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper);

    /**
     * Convert to Map
     * @param keyMapper       a mapping function to produce first keys
     * @param key2Mapper      a mapping function to produce second keys
     * @param valueMapper     a mapping function to produce values
     */
    <K,K2,V> Map<K,Map<K2,V>> toMulti2Map(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends K2> key2Mapper, Function<? super T, ? extends V> valueMapper);

    /**
     * Convert to Map
     * @param keyMapper       a mapping function to produce first keys
     * @param key2Mapper      a mapping function to produce second keys
     * @param key3Mapper      a mapping function to produce third keys
     * @param valueMapper     a mapping function to produce values
     */
    <K,K2,K3,V> Map<K,Map<K2,Map<K3,V>>> toMulti3Map(Function<? super T, ? extends K> keyMapper,
                                                     Function<? super T, ? extends K2> key2Mapper,
                                                     Function<? super T, ? extends K3> key3Mapper,
                                                     Function<? super T, ? extends V> valueMapper);


    /**
     * get stream
     * @return      the stream
     */
    Stream<T> stream();

    /**
     * Convert to other IFrame
     */
    <R> IFrame<R> from(Stream<R> data);

    /**
     * Convert to other IFrame
     */
    <R> IFrame<R> from(List<R> data);

    /**
     * Performs the given action for each element of the Iterable until all elements have been processed or the action throws an exception.
     */
    IFrame<T> forEachDo(Consumer<? super T> action);


    /**
     * Performs the given action for each element of the Iterable until all elements have been processed or the action throws an exception.
     */
    IFrame<T> forEachIndexDo(ConsumerIndex<? super T> action);


    /**
     * such as {@link #forEachDo(Consumer)} , but is parallel to forEach
     */
    IFrame<T> forEachParallel(Consumer<? super T> action);

    /**
     * traverse each element determine whether the specified object is included
     * @param other         specified object
     */
    boolean contains(T other);

    /**
     * traverse each element determine whether the specified object value is included
     * @param valueFunction     field value
     */
    <U> boolean containsValue(Function<T,U> valueFunction, U value);

    /**
     * Concatenate the values of the fields according to the specified delimiter and  prefix ,suffix
     * @param joinField     splicing fields
     * @param delimiter     the delimiter to be used between each element
     * @param prefix        the sequence of characters to be used at the beginning of the joined result
     * @param suffix        the sequence of characters to be used at the end  of the joined result
     *
     */
    <U> String joining(Function<T,U> joinField,CharSequence delimiter, CharSequence prefix, CharSequence suffix);


    /**
     * Concatenate the values of the fields according to the specified delimiter
     * @param joinField     splicing fields
     * @param delimiter     the delimiter to be used between each element
     *
     */
    <U> String joining(Function<T,U> joinField,CharSequence delimiter);


    /**
     * ===========================   Frame Setting =====================================
     **/
    /**
     * Set default decimal places
     */
    IFrame<T> defaultScale(int scale);

    /**
     *  Set default decimal places
     */
    IFrame<T> defaultScale(int scale, RoundingMode roundingMode);

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
     * Get paginated data
     * @param page              The current page number is considered as the first page, regardless of whether it is passed as 0 or 1
     * @param pageSize          page size
     */
    List<T> page(int page,int pageSize);

    /**
     * If the number of rows is 0, it is empty
     */
    boolean isEmpty();

    /**
     * If the number of rows is greater than 0, it is not empty
     */
    boolean isNotEmpty();


    /**
     * ===========================   Frame Convert  =====================================
     */
    /**
     * convert to the new Frame
     * @param map           convert operation
     * @return              the new Frame
     * @param <R>           the new Frame type
     */
    <R> IFrame<R> map(Function<T,R> map);

    /**
     * parallel convert  to the new Frame
     * @param map           convert operation
     * @return              the new Frame
     * @param <R>           the new Frame type
     */
    <R> IFrame<R> mapParallel(Function<T,R> map);

    /**
     * Percentage convert
     *          you can convert the value of a certain field to a percentage,
     *          Then assign a value to a certain column through SetFunction
     * @param get           need percentage convert field
     * @param set           field for storing percentage values
     * @param scale         percentage retain decimal places
     * @param <R>           the percentage field type
     */
    <R extends Number> IFrame<T> mapPercent(Function<T,R> get, SetFunction<T,BigDecimal> set, int scale);

    /**
     * Percentage convert
     *    such as {@link IFrame#mapPercent(Function, SetFunction, int)}, but default scale is 2
     * @param get           need percentage convert field
     * @param set           field for storing percentage values
     */
    <R extends Number> IFrame<T> mapPercent(Function<T,R> get, SetFunction<T,BigDecimal> set);

    /**
     * partition
     *      cut the matrix into multiple small matrices, with each matrix size n
     *
     * @param n         size of each zone
     */
    IFrame<List<T>> partition(int n);

    /**
     * add sort number to the {@link FI2#c2} field
     *      Default sequence number from 1 to frame.length
     */
    IFrame<FI2<T,Integer>> addRowNumberCol();

    /**
     * Sort by sorter first, then add ordinal columns
     * @param sorter    the sorter
     */
    IFrame<FI2<T,Integer>> addRowNumberCol(Sorter<T> sorter);

    /**
     * Add a numbered column to a specific column
     * @param set           specific column
     */
    IFrame<T> addRowNumberCol(SetFunction<T,Integer> set);

    /**
     * Add a numbered column to a specific column
     * @param sorter    the sorter
     * @param set           specific column
     */
    IFrame<T> addRowNumberCol(Sorter<T> sorter,SetFunction<T,Integer> set);

    /**
     * Add ranking columns by comparator
     *      Ranking logic, the same value means the Ranking is the same. This is different from {@link #addRowNumberCol}
     * @param sorter    the ranking  sorter
     */
    IFrame<FI2<T,Integer>> addRankCol(Sorter<T> sorter);


    /**
     * Add ranking column to a certain column by Comparator
     * @param sorter            the ranking  comparator
     * @param set                   certain column
     */
    IFrame<T> addRankCol(Sorter<T> sorter, SetFunction<T,Integer> set);

    /**
     *  Convert columns to multiple rows, to expand fields of arrays or complex types by element, generating multiple rows of data
     *  Cut the string into multiple lines according to the specified delimiter
     *  support string text format "a,b,c,d" or "[a,b,c,d]"
     *
     *
     * @param getFunction        wait to explode field
     * @param delimiter          split delimiter, support regex
     */
    IFrame<FI2<T,String>> explodeString(Function<T,String> getFunction, String delimiter);


    /**
     *  Convert columns to multiple rows, to expand fields of arrays or complex types by element, generating multiple rows of data
     *  Cut the string into multiple lines according to the specified delimiter
     *
     *
     * @param getFunction        wait to explode field
     * @param setFunction        accept the value after explode
     * @param delimiter          split delimiter, support regex
     */
    IFrame<T> explodeString(Function<T,String> getFunction, SetFunction<T,String> setFunction,String delimiter);

    /**
     *  Convert columns to multiple rows, to expand fields of arrays or complex types by element, generating multiple rows of data
     *  Cut the string into multiple lines according to the specified delimiter
     *
     *  Support explode field value type is JSON string array
     *
     * @param getFunction        wait to explode field
     */
    IFrame<FI2<T,String>> explodeJsonArray(Function<T,String> getFunction);


    /**
     *  Convert columns to multiple rows, to expand fields of arrays or complex types by element, generating multiple rows of data
     *  Cut the string into multiple lines according to the specified delimiter
     *
     *  Support explode field value type is JSON string array
     *
     * @param getFunction        wait to explode field
     * @param setFunction        accept the value after explode
     */
    IFrame<T> explodeJsonArray(Function<T,String> getFunction,SetFunction<T,String> setFunction);

    /**
     *  Convert columns to multiple rows, to expand fields of arrays or complex types by element, generating multiple rows of data
     *  Cut the string into multiple lines according to the specified delimiter
     *
     *  Support explode field value type is collection
     *
     * @param getFunction        wait to explode field
     */
    <E> IFrame<FI2<T,E>> explodeCollection(Function<T,? extends Collection<E>> getFunction);


    /**
     *  Convert columns to multiple rows, to expand fields of arrays or complex types by element, generating multiple rows of data
     *  Cut the string into multiple lines according to the specified delimiter
     *
     *  Support explode field value type is collection
     *
     * @param getFunction        wait to explode field
     * @param setFunction        accept the value after explode
     */
    <E> IFrame<T> explodeCollection(Function<T,? extends Collection<E>> getFunction,SetFunction<T,E> setFunction);

    /**
     *  Convert columns to multiple rows, to expand fields of arrays or complex types by element, generating multiple rows of data
     *  Cut the string into multiple lines according to the specified delimiter
     *
     *  Support explode field value type is array or collection
     *
     * @param getFunction        wait to explode field
     * @param elementClass       the array or collection element class
     */
    <E> IFrame<FI2<T,E>> explodeCollectionArray(Function<T,?> getFunction,Class<E> elementClass);


    /**
     *  Convert columns to multiple rows, to expand fields of arrays or complex types by element, generating multiple rows of data
     *  Cut the string into multiple lines according to the specified delimiter
     *
     *  Support explode field value type is array or collection
     *
     * @param getFunction        wait to explode field
     * @param setFunction        accept the value after explode
     * @param elementClass       the array or collection element class
     */
    <E> IFrame<T> explodeCollectionArray(Function<T,?> getFunction,SetFunction<T,E> setFunction,Class<E> elementClass);


    /**
     * ===========================   Sort Frame  =====================================
     **/

    /**
     * Descending order
     * @param comparator         comparator
     */
    IFrame<T> sortDesc(Comparator<T> comparator);

    /**
     * Descending order by field
     * @param function      sort field
     * @param <R>           the  sort field type
     */
    <R extends Comparable<? super R>> IFrame<T> sortDesc(Function<T, R> function);

    /**
     * Ascending order
     * @param comparator         comparator
     */
    IFrame<T> sortAsc(Comparator<T> comparator);

    /**
     * Ascending order
     * @param function      sort field
     */
    <R extends Comparable<R>> IFrame<T> sortAsc(Function<T, R> function);


    /** ===========================   Cut Frame  ===================================== **/

    /**
     *  Cut the top n element
     * @param n    the top n
     */
    IFrame<T> cutFirst(int n);

    /**
     * Cut the last n element
     * @param n    the last n
     */
    IFrame<T> cutLast(int n);

    /**
     * cut elements within the scope
     */
    IFrame<T> cut(Integer startIndex,Integer endIndex);

    /**
     * cut paginated data
     * @param page              The current page number is considered as the first page, regardless of whether it is passed as 0 or 1
     * @param pageSize          page size
     */
    IFrame<T> cutPage(int page,int pageSize);


    /**
     * Cut the top N rankings data
     *          The same value is considered to have the same ranking
     * @param sorter                the ranking sorter
     * @param n                     the top n
     */
    IFrame<T> cutFirstRank(Sorter<T> sorter, int n);


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

    /**
     *  Get elements within the scope.  [startIndex,endIndex]
     */
    List<T> getList(Integer startIndex, Integer endIndex);

    /** ===========================   Distinct Frame  ===================================== **/

    /**
     * distinct by  T value
     */
    IFrame<T> distinct();


    /**
     * distinct by field value
     * @param function          the field
     * @param <R>               field value type
     */
    <R extends Comparable<R>> IFrame<T> distinct(Function<T, R> function);

    /**
     * distinct by field value
     * @param function          the field
     * @param listOneFunction          When there are more than one repeated element, this method will be called back, and customization will determine which element to choose
     * @param <R>               field value type
     */
    <R extends Comparable<R>> IFrame<T> distinct(Function<T, R> function, ListSelectOneFunction<T> listOneFunction);


    /**
     * distinct by  comparator
     * @param comparator        the comparator
     */
    IFrame<T> distinct(Comparator<T> comparator);

    /**
     * distinct by  comparator
     * @param comparator        the comparator
     * @param function          When there are more than one repeated element, this method will be called back, and customization will determine which element to choose
     */
    IFrame<T> distinct(Comparator<T> comparator, ListSelectOneFunction<T> function);


    /** ===========================   Other  ===================================== **/

    /**
     * Summarize all collectDim values, calculate the difference between them,
     * and then add the missing difference to the Frame through getEmptyObject
     * Finally, return the list that needs to be supplemented
     *
     * @param  collectDim           Summary value
     * @param  allDim               all need value
     * @param  getEmptyObject       Generate a new object based on the difference using this function
     *
     */
    <C> List<T> replenishList(Function<T, C> collectDim, List<C> allDim, Function<C,T> getEmptyObject);


    /**
     * Summarize all collectDim values, calculate the difference between them,
     * and then add the missing difference to the Frame through getEmptyObject
     *
     * @param  collectDim           Summary value
     * @param  allDim               all need value
     * @param  getEmptyObject       Generate a new object based on the difference using this function
     */
    <C> IFrame<T> replenish(Function<T, C> collectDim, List<C> allDim, Function<C,T> getEmptyObject);


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
    <G, C> IFrame<T> replenish(Function<T, G> groupDim, Function<T, C> collectDim, List<C> allDim, ReplenishFunction<G,C,T> getEmptyObject);

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
    <G, C> IFrame<T> replenish(Function<T, G> groupDim, Function<T, C> collectDim, ReplenishFunction<G,C,T> getEmptyObject);
}
