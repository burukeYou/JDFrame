package io.github.burukeyou.dataframe.iframe;

import io.github.burukeyou.dataframe.iframe.function.ConsumerIndex;
import io.github.burukeyou.dataframe.iframe.function.ListSelectOneFunction;
import io.github.burukeyou.dataframe.iframe.function.ReplenishFunction;
import io.github.burukeyou.dataframe.iframe.function.SetFunction;
import io.github.burukeyou.dataframe.iframe.impl.JDFrameImpl;
import io.github.burukeyou.dataframe.iframe.item.FI2;
import io.github.burukeyou.dataframe.iframe.item.FI3;
import io.github.burukeyou.dataframe.iframe.window.Sorter;
import io.github.burukeyou.dataframe.iframe.window.Window;
import io.github.burukeyou.dataframe.util.FrameUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 *  JDFrame
 *      The operations before and after are discontinuous, and all operations take effect in real time.
 *      Even after executing the termination operation, they can still be reused without the need to re read to generate a stream
 *
 * @author  caizhihao
 */
public interface JDFrame<T> extends ConfigurableJDFrame<T> {

    /**
     * Convert a list to JDFrame
     */
    static <R> JDFrame<R> read(List<R> list) {
        return new JDFrameImpl<>(list);
    }

    /**
     * Convert a map to JDFrame
     */
    static <K,V> JDFrame<FI2<K,V>> read(Map<K,V> map) {
        return new JDFrameImpl<>(FrameUtil.toListFI2(map));
    }

    /**
     * Convert a map to JDFrame
     */
    static <K,J,V> JDFrame<FI3<K,J,V>> readMap(Map<K,Map<J,V>> map) {
        return new JDFrameImpl<>(FrameUtil.toListFI3(map));
    }

    /**
     * Convert to other JDFrame
     */
    <R> JDFrame<R> from(Stream<R> data);


    /**
     * Performs the given action for each element of the Iterable until all elements have been processed or the action throws an exception.
     */
    JDFrame<T> forEachDo(Consumer<? super T> action);

    /**
     * such as {@link #forEachDo(Consumer)} , but is parallel to forEach
     */
    JDFrame<T> forEachParallel(Consumer<? super T> action);

    /**
     * Performs the given action for each element of the Iterable until all elements have been processed or the action throws an exception.
     */
    JDFrame<T> forEachDo(ConsumerIndex<? super T> action);


    /**
     * ===========================   Frame Setting =====================================
     **/
    /**
     * Set default decimal places
     */
    JDFrame<T> defaultScale(int scale);

    /**
     *  Set default decimal places
     */
    JDFrame<T> defaultScale(int scale, RoundingMode roundingMode);

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
     * ===========================   Frame Convert  =====================================
     */
    /**
     * convert to the new Frame
     * @param map           convert operation
     * @return              the new Frame
     * @param <R>           the new Frame type
     */
    <R> JDFrame<R> map(Function<T,R> map);

    /**
     * parallel convert  to the new Frame
     * @param map           convert operation
     * @return              the new Frame
     * @param <R>           the new Frame type
     */
    <R> JDFrame<R> mapParallel(Function<T,R> map);

    /**
     * Percentage convert
     *          you can convert the value of a certain field to a percentage,
     *          Then assign a value to a certain column through SetFunction
     * @param get           need percentage convert field
     * @param set           field for storing percentage values
     * @param scale         percentage retain decimal places
     * @param <R>           the percentage field type
     */
    <R extends Number> JDFrame<T> mapPercent(Function<T,R> get, SetFunction<T, BigDecimal> set, int scale);

    /**
     * Percentage convert
     *    such as {@link IFrame#mapPercent(Function, SetFunction, int)}, but default scale is 2
     * @param get           need percentage convert field
     * @param set           field for storing percentage values
     */
    <R extends Number> JDFrame<T> mapPercent(Function<T,R> get, SetFunction<T,BigDecimal> set);

    /**
     * partition
     *      cut the matrix into multiple small matrices, with each matrix size n
     *
     * @param n         size of each zone
     */
    JDFrame<List<T>> partition(int n);

    /**
     * ddd ordinal column
     * @return                      FI2(T,Number)
     */
    JDFrame<FI2<T,Integer>> addRowNumberCol();

    /**
     * Sort by comparator first, then add ordinal columns
     * @param sorter    the sort comparator
     */
    JDFrame<FI2<T,Integer>> addRowNumberCol(Sorter<T> sorter);

    /**
     * Add a numbered column to a specific column
     * @param set           specific column
     */
    JDFrame<T> addRowNumberCol(SetFunction<T,Integer> set);

    /**
     * Add a numbered column to a specific column
     * @param sorter    the sorter
     * @param set           specific column
     */
    JDFrame<T> addRowNumberCol(Sorter<T> sorter,SetFunction<T,Integer> set);

    /**
     * Add ranking columns by comparator
     *      Ranking logic, the same value means the Ranking is the same. This is different from {@link #addRowNumberCol}
     * @param sorter    the ranking  comparator
     */
    JDFrame<FI2<T,Integer>> addRankCol(Sorter<T> sorter);

    /**
     * Add ranking column to a certain column by Comparator
     * @param sorter            the ranking  sorter
     * @param set                   certain column
     */
    JDFrame<T> addRankCol(Sorter<T> sorter, SetFunction<T,Integer> set);


    /**
     *  Convert columns to multiple rows, to expand fields of arrays or complex types by element, generating multiple rows of data
     *  Cut the string into multiple lines according to the specified delimiter
     *
     *
     * @param getFunction        wait to explode field
     * @param delimiter          split delimiter, support regex
     */
    JDFrame<FI2<T,String>> explodeString(Function<T,String> getFunction, String delimiter);

    /**
     *  Convert columns to multiple rows, to expand fields of arrays or complex types by element, generating multiple rows of data
     *  Cut the string into multiple lines according to the specified delimiter
     *
     *
     * @param getFunction        wait to explode field
     * @param setFunction        accept the value after explode
     * @param delimiter          split delimiter, support regex
     */
    JDFrame<T> explodeString(Function<T,String> getFunction, SetFunction<T,String> setFunction,String delimiter);

    /**
     *  Convert columns to multiple rows, to expand fields of arrays or complex types by element, generating multiple rows of data
     *  Cut the string into multiple lines according to the specified delimiter
     *
     *  Support explode field value type is JSON string array
     *
     * @param getFunction        wait to explode field
     */
    JDFrame<FI2<T,String>> explodeJsonArray(Function<T,String> getFunction);


    /**
     *  Convert columns to multiple rows, to expand fields of arrays or complex types by element, generating multiple rows of data
     *  Cut the string into multiple lines according to the specified delimiter
     *
     *  Support explode field value type is JSON string array
     *
     * @param getFunction        wait to explode field
     * @param setFunction        accept the value after explode
     */
    JDFrame<T> explodeJsonArray(Function<T,String> getFunction,SetFunction<T,String> setFunction);

    /**
     *  Convert columns to multiple rows, to expand fields of arrays or complex types by element, generating multiple rows of data
     *  Cut the string into multiple lines according to the specified delimiter
     *
     *  Support explode field value type is collection
     *
     * @param getFunction        wait to explode field
     */
    <E> JDFrame<FI2<T,E>> explodeCollection(Function<T,? extends Collection<E>> getFunction);


    /**
     *  Convert columns to multiple rows, to expand fields of arrays or complex types by element, generating multiple rows of data
     *  Cut the string into multiple lines according to the specified delimiter
     *
     *  Support explode field value type is collection
     *
     * @param getFunction        wait to explode field
     * @param setFunction        accept the value after explode
     */
    <E> JDFrame<T> explodeCollection(Function<T,? extends Collection<E>> getFunction,SetFunction<T,E> setFunction);


    /**
     *  Convert columns to multiple rows, to expand fields of arrays or complex types by element, generating multiple rows of data
     *  Cut the string into multiple lines according to the specified delimiter
     *
     *  Support explode field value type is array or collection
     *
     * @param getFunction        wait to explode field
     * @param elementClass       the array or collection element class
     */
    <E> JDFrame<FI2<T,E>> explodeCollectionArray(Function<T,?> getFunction,Class<E> elementClass);


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
    <E> JDFrame<T> explodeCollectionArray(Function<T,?> getFunction,SetFunction<T,E> setFunction,Class<E> elementClass);


    /**
     * ===========================   Sort Frame  =====================================
     **/

    /**
     * Descending order
     * @param comparator         comparator
     */
    JDFrame<T> sortDesc(Comparator<T> comparator);

    /**
     * Descending order by field
     * @param function      sort field
     * @param <R>           the  sort field type
     */
    <R extends Comparable<? super R>> JDFrame<T> sortDesc(Function<T, R> function);

    /**
     * Ascending order
     * @param comparator         comparator
     */
    JDFrame<T> sortAsc(Comparator<T> comparator);

    /**
     * Ascending order
     * @param function      sort field
     */
    <R extends Comparable<R>> JDFrame<T> sortAsc(Function<T, R> function);


    /** ===========================   Cut Frame  ===================================== **/

    /**
     *  Cut the top n element
     * @param n    the top n
     */
    JDFrame<T> cutFirst(int n);

    /**
     * Cut the last n element
     * @param n    the last n
     */
    JDFrame<T> cutLast(int n);

    /**
     * cut elements within the scope
     */
    JDFrame<T> cut(Integer startIndex,Integer endIndex);

    /**
     * cut paginated data
     * @param page              The current page number is considered as the first page, regardless of whether it is passed as 0 or 1
     * @param pageSize          page size
     */
    JDFrame<T> cutPage(int page,int pageSize);

    /**
     * Cut the top N rankings data
     *          The same value is considered to have the same ranking
     * @param sorter                the ranking sorter
     * @param n                     the top n
     */
    JDFrame<T> cutFirstRank(Sorter<T> sorter, int n);


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
    JDFrame<T> distinct();


    /**
     * distinct by field value
     * @param function          the field
     * @param <R>               field value type
     */
    <R extends Comparable<R>> JDFrame<T> distinct(Function<T, R> function);

    /**
     * distinct by field value
     * @param function          the field
     * @param listOneFunction          When there are more than one repeated element, this method will be called back, and customization will determine which element to choose
     * @param <R>               field value type
     */
    <R extends Comparable<R>> JDFrame<T> distinct(Function<T, R> function, ListSelectOneFunction<T> listOneFunction);


    /**
     * distinct by  comparator
     * @param comparator        the comparator
     */
     JDFrame<T> distinct(Comparator<T> comparator);

    /**
     * distinct by  comparator
     * @param comparator        the comparator
     * @param function          When there are more than one repeated element, this method will be called back, and customization will determine which element to choose
     */
    JDFrame<T> distinct(Comparator<T> comparator, ListSelectOneFunction<T> function);


    /** ===========================   Window Function  ===================================== **/

    /**
     * open a window
     * @param window            window param
     */
    WindowJDFrame<T> window(Window<T> window);

    /**
     * open a empty window
     */
    WindowJDFrame<T> window();



    /** ===========================   Other  ===================================== **/

    /**
     * Summarize all collectDim values, calculate the difference between them, and then add the missing difference to the Frame through getEmptyObject
     *
     */
    <C> JDFrame<T> replenish(Function<T, C> collectDim, List<C> allDim, Function<C,T> getEmptyObject);


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
    <G, C> JDFrame<T> replenish(Function<T, G> groupDim,
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
    <G, C> JDFrame<T> replenish(Function<T, G> groupDim, Function<T, C> collectDim, ReplenishFunction<G,C,T> getEmptyObject);

}
