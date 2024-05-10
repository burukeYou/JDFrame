package io.github.burukeyou.dataframe.iframe;

import io.github.burukeyou.dataframe.iframe.function.SetFunction;
import io.github.burukeyou.dataframe.iframe.item.FI2;

import java.math.BigDecimal;
import java.util.function.Function;

/**
 * @author  caizhihao
 * @param <T>
 */
public interface WindowSDFrame<T> extends SDFrame<T>  {


    /**
     * rowNumber window function
     */
    SDFrame<FI2<T,Integer>> overRowNumber();

    /**
     * rowNumber window function
     *              set the function result to the setFunction
     * @param setFunction            function result accept
     */
    WindowSDFrame<T> overRowNumberS(SetFunction<T,Integer> setFunction);

    /**
     * rank window function
     */
    SDFrame<FI2<T,Integer>> overRank();

    /**
     *  rank window function
     *            set the function result to the setFunction
     * @param setFunction               function result accept
     */
    WindowSDFrame<T> overRankS(SetFunction<T,Integer> setFunction);

    /**
     *  dense rank window function
     *            set the function result to the setFunction
     */
    SDFrame<FI2<T,Integer>> overDenseRank();

    /**
     *  dense rank window function
     *            set the function result to the setFunction
     * @param setFunction               function result accept
     */
    WindowSDFrame<T> overDenseRankS(SetFunction<T,Integer> setFunction);


    /**
     *  Percent rank window function
     *            set the function result to the setFunction
     */
    SDFrame<FI2<T, BigDecimal>> overPercentRank();


    /**
     *  Percent rank window function
     *            set the function result to the setFunction
     * @param setFunction               function result accept
     */
    WindowSDFrame<T> overPercentRankS(SetFunction<T,BigDecimal> setFunction);

    /**
     * Cume Dist window function
     *            set the function result to the setFunction
     */
    SDFrame<FI2<T,BigDecimal>> overCumeDist();

    /**
     * Cume Dist window function
     *            set the function result to the setFunction
     * @param setFunction               function result accept
     */
    WindowSDFrame<T> overCumeDistS(SetFunction<T,BigDecimal> setFunction);

    /**
     * Lag window function
     *          get the value of the first n rows of the current row
     * @param field                      field value
     * @param n                          first n rows
     */
    <F> SDFrame<FI2<T,F>> overLag(Function<T,F> field, int n);

    /**
     * Lag window function
     *          get the value of the first n rows of the current row
     * @param setFunction                function result accept
     * @param field                      field value
     * @param n                          first n rows
     */
    <F> WindowSDFrame<T> overLagS(SetFunction<T,F> setFunction,Function<T,F> field,int n);

    /**
     * lead window function
     *          get the value of the last n rows of the current row
     * @param field                      field value
     * @param n                          last n rows
     */
    <F> SDFrame<FI2<T,F>> overLead(Function<T,F> field, int n);

    /**
     * lead window function
     *          get the value of the last n rows of the current row
     * @param setFunction                function result accept
     * @param field                      field value
     * @param n                          last n rows
     */
    <F> WindowSDFrame<T> overLeadS(SetFunction<T,F> setFunction,Function<T,F> field, int n);

    /**
     * NthValue window function
     *          get the Nth row within the window range
     * @param field                      field value
     * @param n                          last n rows
     */
    <F> SDFrame<FI2<T,F>> overNthValue(Function<T,F> field, int n);

    /**
     * NthValue window function
     *          get the Nth row within the window range
     * @param setFunction                function result accept
     * @param field                      field value
     * @param n                          last n rows
     */
    <F> WindowSDFrame<T> overNthValueS(SetFunction<T,F> setFunction,Function<T,F> field,int n);

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
    <F> WindowSDFrame<T> overFirstValueS(SetFunction<T,F> setFunction,Function<T,F> field);

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
    <F> WindowSDFrame<T> overLastValueS(SetFunction<T,F> setFunction,Function<T,F> field);

    /**
     * sum window function
     *         calculate the sum value within the window range
     * @param field                      field value
     */
    <F> SDFrame<FI2<T,BigDecimal>> overSum(Function<T,F> field);

    /**
     * sum window function
     *         calculate the sum value within the window range
     * @param setFunction                function result accept
     * @param field                      field value
     */
    <F> WindowSDFrame<T> overSumS(SetFunction<T,BigDecimal> setFunction, Function<T,F> field);

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
     * @param field                      field value
     */
    <F> WindowSDFrame<T> overAvgS(SetFunction<T,BigDecimal> setFunction, Function<T,F> field);

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
     * @param field                      field value
     */
    <F extends Comparable<? super F>> WindowSDFrame<T> overMaxValueS(SetFunction<T,F> setFunction, Function<T,F> field);

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
     * @param field                      field value
     */
    <F extends Comparable<? super F>> WindowSDFrame<T> overMinValueS(SetFunction<T,F> setFunction, Function<T,F> field);

    /**
     * count window function
     *         calculate the count within the window range
     */
    SDFrame<FI2<T,Integer>> overCount();


    /**
     * count window function
     *         calculate the count within the window range
     * @param setFunction                function result accept
     */
    WindowSDFrame<T> overCountS(SetFunction<T,Integer> setFunction);

    /**
     * Ntile window function
     *         assign bucket numbers evenly to windows, starting from 1
     * @param n              size of buckets
     */
    SDFrame<FI2<T,Integer>> overNtile(int n);

    /**
     * Ntile window function
     *         assign bucket numbers evenly to windows, starting from 1
     * @param setFunction                function result accept
     * @param n              size of buckets
     */
    WindowSDFrame<T> overNtileS(SetFunction<T,Integer> setFunction, int n);
}
