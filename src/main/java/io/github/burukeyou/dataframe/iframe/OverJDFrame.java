package io.github.burukeyou.dataframe.iframe;

import io.github.burukeyou.dataframe.iframe.function.SetFunction;
import io.github.burukeyou.dataframe.iframe.item.FI2;
import io.github.burukeyou.dataframe.iframe.window.Window;

import java.math.BigDecimal;
import java.util.function.Function;

/**
 * @author      caizhihao
 * @param <T>
 */
public interface OverJDFrame<T> {

    /**
     * rowNumber window function
     * @param overParam           window param
     */
    JDFrame<FI2<T,Integer>> overRowNumber(Window<T> overParam);

    /**
     * rowNumber window function
     */
    JDFrame<FI2<T,Integer>> overRowNumber();

    /**
     * rowNumber window function
     *              set the function result to the setFunction
     * @param setFunction            function result accept
     * @param overParam              window param
     */
    JDFrame<T> overRowNumberS(SetFunction<T,Integer> setFunction, Window<T> overParam);

    /**
     * rowNumber window function
     *              set the function result to the setFunction
     * @param setFunction            function result accept
     */
    JDFrame<T> overRowNumberS(SetFunction<T,Integer> setFunction);

    /**
     * rank window function
     * @param overParam           window param
     */
    JDFrame<FI2<T,Integer>> overRank(Window<T> overParam);

    /**
     *  rank window function
     *            set the function result to the setFunction
     * @param setFunction               function result accept
     * @param overParam                 window param
     */
    JDFrame<T> overRankS(SetFunction<T,Integer> setFunction, Window<T> overParam);

    /**
     * dense rank window function
     * @param overParam           window param
     */
    JDFrame<FI2<T,Integer>> overDenseRank(Window<T> overParam);

    /**
     *  dense rank window function
     *            set the function result to the setFunction
     * @param setFunction               function result accept
     * @param overParam                 window param
     */
    JDFrame<T> overDenseRankS(SetFunction<T,Integer> setFunction, Window<T> overParam);

    /**
     * Percent rank window function
     * @param overParam           window param
     */
    JDFrame<FI2<T, BigDecimal>> overPercentRank(Window<T> overParam);

    /**
     *  Percent rank window function
     *            set the function result to the setFunction
     * @param setFunction               function result accept
     * @param overParam                 window param
     */
    JDFrame<T> overPercentRankS(SetFunction<T,BigDecimal> setFunction, Window<T> overParam);

    /**
     * Cume Dist window function
     * @param overParam           window param
     */
    JDFrame<FI2<T,BigDecimal>> overCumeDist(Window<T> overParam);

    /**
     * Cume Dist window function
     *            set the function result to the setFunction
     * @param setFunction               function result accept
     * @param overParam                 window param
     */
    JDFrame<T> overCumeDistS(SetFunction<T,BigDecimal> setFunction, Window<T> overParam);

    /**
     * Lag window function
     *          get the value of the first n rows of the current row
     * @param overParam             window param
     * @param field                 field value
     * @param n                     first n rows
     */
    <F> JDFrame<FI2<T,F>> overLag(Window<T> overParam, Function<T,F> field, int n);

    /**
     * Lag window function
     *          get the value of the first n rows of the current row
     * @param setFunction                function result accept
     * @param overParam                  window param
     * @param field                      field value
     * @param n                          first n rows
     */
    <F> JDFrame<T> overLagS(SetFunction<T,F> setFunction,Window<T> overParam,Function<T,F> field,int n);

    /**
     * Lag window function
     *          get the value of the first n rows of the current row
     * @param field                      field value
     * @param n                          first n rows
     */
    <F> JDFrame<FI2<T,F>> overLag(Function<T,F> field,int n);

    /**
     * Lag window function
     *          get the value of the first n rows of the current row
     * @param setFunction                function result accept
     * @param field                      field value
     * @param n                          first n rows
     */
    <F> JDFrame<T> overLagS(SetFunction<T,F> setFunction,Function<T,F> field,int n);

    /**
     * lead window function
     *          get the value of the last n rows of the current row
     * @param overParam                  window param
     * @param field                      field value
     * @param n                          last n rows
     */
    <F> JDFrame<FI2<T,F>> overLead(Window<T> overParam,Function<T,F> field,int n);

    /**
     * lead window function
     *          get the value of the last n rows of the current row
     * @param setFunction                function result accept
     * @param overParam                  window param
     * @param field                      field value
     * @param n                          last n rows
     */
    <F> JDFrame<T> overLeadS(SetFunction<T,F> setFunction,Window<T> overParam,Function<T,F> field,int n);

    /**
     * lead window function
     *          get the value of the last n rows of the current row
     * @param field                      field value
     * @param n                          last n rows
     */
    <F> JDFrame<FI2<T,F>> overLead(Function<T,F> field,int n);

    /**
     * lead window function
     *          get the value of the last n rows of the current row
     * @param setFunction                function result accept
     * @param field                      field value
     * @param n                          last n rows
     */
    <F> JDFrame<T> overLeadS(SetFunction<T,F> setFunction,Function<T,F> field,int n);

    /**
     * NthValue window function
     *          get the Nth row within the window range
     * @param field                      field value
     * @param n                          last n rows
     */
    <F> JDFrame<FI2<T,F>> overNthValue(Window<T> overParam,Function<T,F> field,int n);

    /**
     * NthValue window function
     *          get the Nth row within the window range
     * @param setFunction                function result accept
     * @param overParam                  window param
     * @param field                      field value
     * @param n                          last n rows
     */
    <F> JDFrame<T> overNthValueS(SetFunction<T,F> setFunction,Window<T> overParam,Function<T,F> field,int n);

    /**
     * NthValue window function
     *          get the Nth row within the window range
     * @param field                      field value
     * @param n                          last n rows
     */
    <F> JDFrame<FI2<T,F>> overNthValue(Function<T,F> field,int n);

    /**
     * NthValue window function
     *          get the Nth row within the window range
     * @param setFunction                function result accept
     * @param field                      field value
     * @param n                          last n rows
     */
    <F> JDFrame<T> overNthValueS(SetFunction<T,F> setFunction,Function<T,F> field,int n);

    /**
     * FirstValue window function
     *          get the first row within the window range
     * @param field                      field value
     */
    <F> JDFrame<FI2<T,F>> overFirstValue(Window<T> overParam,Function<T,F> field);

    /**
     * FirstValue window function
     *          get the first row within the window range
     * @param setFunction                function result accept
     * @param overParam                  window param
     * @param field                      field value
     */
    <F> JDFrame<T> overFirstValueS(SetFunction<T,F> setFunction,Window<T> overParam,Function<T,F> field);

    /**
     * FirstValue window function
     *          get the first row within the window range
     * @param field                      field value
     */
    <F> JDFrame<FI2<T,F>> overFirstValue(Function<T,F> field);

    /**
     * FirstValue window function
     *          get the first row within the window range
     * @param setFunction                function result accept
     * @param field                      field value
     */
    <F> JDFrame<T> overFirstValueS(SetFunction<T,F> setFunction,Function<T,F> field);

    /**
     * LastValue window function
     *          get the last row within the window range
     * @param overParam                  window param
     * @param field                      field value
     */
    <F> JDFrame<FI2<T,F>> overLastValue(Window<T> overParam,Function<T,F> field);

    /**
     * LastValue window function
     *          get the last row within the window range
     * @param setFunction                function result accept
     * @param overParam                  window param
     * @param field                      field value
     */
    <F> JDFrame<T> overLastValueS(SetFunction<T,F> setFunction,Window<T> overParam,Function<T,F> field);

    /**
     * LastValue window function
     *          get the last row within the window range
     * @param field                      field value
     */
    <F> JDFrame<FI2<T,F>> overLastValue(Function<T,F> field);

    /**
     * LastValue window function
     *          get the last row within the window range
     * @param setFunction                function result accept
     * @param field                      field value
     */
    <F> JDFrame<T> overLastValueS(SetFunction<T,F> setFunction,Function<T,F> field);

    /**
     * Sum window function
     *         calculate the sum within the window range
     * @param overParam                  window param
     * @param field                      field value
     */
    <F> JDFrame<FI2<T,BigDecimal>> overSum(Window<T> overParam,Function<T,F> field);

    /**
     * Sum window function
     *         calculate the sum within the window range
     * @param field                      field value
     */
    <F> JDFrame<FI2<T,BigDecimal>> overSum(Function<T,F> field);

    /**
     * Sum window function
     *         calculate the sum within the window range
     * @param setFunction                function result accept
     * @param overParam                  window param
     * @param field                      field value
     */
    <F> JDFrame<T> overSumS(SetFunction<T,BigDecimal> setFunction, Window<T> overParam, Function<T,F> field);


    /**
     * Sum window function
     *         calculate the sum within the window range
     * @param setFunction                function result accept
     * @param field                      field value
     */
    <F> JDFrame<T> overSumS(SetFunction<T,BigDecimal> setFunction, Function<T,F> field);

    /**
     * avg window function
     *         calculate the avg value within the window range
     * @param overParam                  window param
     * @param field                      field value
     */
    <F> JDFrame<FI2<T,BigDecimal>> overAvg(Window<T> overParam,Function<T,F> field);

    /**
     * avg window function
     *         calculate the avg value within the window range
     * @param field                      field value
     */
    <F> JDFrame<FI2<T,BigDecimal>> overAvg(Function<T,F> field);

    /**
     * avg window function
     *         calculate the avg value within the window range
     * @param setFunction                function result accept
     * @param overParam                  window param
     * @param field                      field value
     */
    <F> JDFrame<T> overAvgS(SetFunction<T,BigDecimal> setFunction, Window<T> overParam, Function<T,F> field);

    /**
     * avg window function
     *         calculate the avg value within the window range
     * @param setFunction                function result accept
     * @param field                      field value
     */
    <F> JDFrame<T> overAvgS(SetFunction<T,BigDecimal> setFunction, Function<T,F> field);

    /**
     * max window function
     *         calculate the max value within the window range
     * @param overParam                  window param
     * @param field                      field value
     */
    <F extends Comparable<? super F>> JDFrame<FI2<T,F>> overMaxValue(Window<T> overParam,Function<T,F> field);

    /**
     * max window function
     *         calculate the max value within the window range
     * @param field                      field value
     */
    <F extends Comparable<? super F>> JDFrame<FI2<T,F>> overMaxValue(Function<T,F> field);

    /**
     * max window function
     *         calculate the max value within the window range
     * @param setFunction                function result accept
     * @param overParam                  window param
     * @param field                      field value
     */
    <F extends Comparable<? super F>> JDFrame<T> overMaxValueS(SetFunction<T,F> setFunction, Window<T> overParam, Function<T,F> field);

    /**
     * max window function
     *         calculate the max value within the window range
     * @param setFunction                function result accept
     * @param field                      field value
     */
    <F extends Comparable<? super F>> JDFrame<T> overMaxValueS(SetFunction<T,F> setFunction, Function<T,F> field);

    /**
     * min window function
     *         calculate the min value within the window range
     * @param overParam                  window param
     * @param field                      field value
     */
    <F extends Comparable<? super F>> JDFrame<FI2<T,F>> overMinValue(Window<T> overParam,Function<T,F> field);

    /**
     * min window function
     *         calculate the min value within the window range
     * @param field                      field value
     */
    <F extends Comparable<? super F>> JDFrame<FI2<T,F>> overMinValue(Function<T,F> field);

    /**
     * min window function
     *         calculate the min value within the window range
     * @param setFunction                function result accept
     * @param overParam                  window param
     * @param field                      field value
     */
    <F extends Comparable<? super F>> JDFrame<T> overMinValueS(SetFunction<T,F> setFunction, Window<T> overParam, Function<T,F> field);

    /**
     * min window function
     *         calculate the min value within the window range
     * @param setFunction                function result accept
     * @param field                      field value
     */
    <F extends Comparable<? super F>> JDFrame<T> overMinValueS(SetFunction<T,F> setFunction, Function<T,F> field);

    /**
     * count window function
     *         calculate the count within the window range
     * @param overParam                  window param
     */
    JDFrame<FI2<T,Integer>> overCount(Window<T> overParam);

    /**
     * count window function
     *         calculate the count within the window range
     */
    JDFrame<FI2<T,Integer>> overCount();

    /**
     * count window function
     *         calculate the count within the window range
     * @param setFunction                function result accept
     * @param overParam                  window param
     */
    JDFrame<T> overCountS(SetFunction<T,Integer> setFunction, Window<T> overParam);

    /**
     * count window function
     *         calculate the count within the window range
     * @param setFunction                function result accept
     */
    JDFrame<T> overCountS(SetFunction<T,Integer> setFunction);

    /**
     * Ntile window function
     *         assign bucket numbers evenly to windows, starting from 1
     * @param n              size of buckets
     */
    JDFrame<FI2<T,Integer>> overNtile(int n);


    /**
     * Ntile window function
     *         assign bucket numbers evenly to windows, starting from 1
     * @param n              size of buckets
     * @param overParam                  window param
     */
    JDFrame<FI2<T,Integer>> overNtile(Window<T> overParam, int n);

    /**
     * Ntile window function
     *         assign bucket numbers evenly to windows, starting from 1
     * @param setFunction                function result accept
     * @param n              size of buckets
     * @param overParam                  window param
     */
    JDFrame<T> overNtileS(SetFunction<T,Integer> setFunction,Window<T> overParam, int n);

    /**
     * Ntile window function
     *         assign bucket numbers evenly to windows, starting from 1
     * @param setFunction                function result accept
     * @param n              size of buckets
     */
    JDFrame<T> overNtileS(SetFunction<T,Integer> setFunction, int n);
}
