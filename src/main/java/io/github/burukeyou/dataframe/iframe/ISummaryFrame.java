package io.github.burukeyou.dataframe.iframe;

import io.github.burukeyou.dataframe.iframe.support.MaxMin;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.function.Function;

/**
 * Summary Frame data
 *
 * @author      caizhihao
 */
public interface ISummaryFrame<T> {

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

    /**
     * Calculate the quantity after deduplication
     */
    long countDistinct(Comparator<T> comparator);

    /**
     * Calculate the quantity after deduplication
     */
    <R extends Comparable<R>> long countDistinct(Function<T, R> function);
}
