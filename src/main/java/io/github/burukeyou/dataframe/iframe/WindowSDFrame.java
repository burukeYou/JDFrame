package io.github.burukeyou.dataframe.iframe;

import io.github.burukeyou.dataframe.iframe.function.SetFunction;
import io.github.burukeyou.dataframe.iframe.item.FI2;

import java.math.BigDecimal;
import java.util.function.Function;

/**
 * @author  caizhihao
 * @param <T>
 */
public interface WindowSDFrame<T> {

    SDFrame<FI2<T,Integer>> overRowNumber();

    SDFrame<T> overRowNumber(SetFunction<T,Integer> setFunction);

    SDFrame<FI2<T,Integer>> overRank();

    SDFrame<T> overRank(SetFunction<T,Integer> setFunction);

    SDFrame<FI2<T,Integer>> overDenseRank();

    SDFrame<T> overDenseRank(SetFunction<T,Integer> setFunction);

    SDFrame<FI2<T, BigDecimal>> overPercentRank();

    SDFrame<T> overPercentRank(SetFunction<T,BigDecimal> setFunction);

    SDFrame<FI2<T,BigDecimal>> overCumeDist();

    SDFrame<T> overCumeDist(SetFunction<T,BigDecimal> setFunction);

    <F> SDFrame<FI2<T,F>> overLag(Function<T,F> field, int n);

    <F> SDFrame<FI2<T,F>> overLead(Function<T,F> field, int n);

    <F> SDFrame<FI2<T,F>> overNthValue(Function<T,F> field, int n);

    <F> SDFrame<FI2<T,F>> overFirstValue(Function<T,F> field, int n);

    <F> SDFrame<FI2<T,F>> overLastValue(Function<T,F> field,int n);

    <F> SDFrame<FI2<T,BigDecimal>> overSum(Function<T,F> field);

    <F> SDFrame<T> overSum(SetFunction<T,BigDecimal> setFunction,Function<T,F> field);

    <F> SDFrame<FI2<T,BigDecimal>> overAvg(Function<T,F> field);

    <F> SDFrame<T> overAvg(SetFunction<T,BigDecimal> setFunction,Function<T,F> field);

    <F extends Comparable<? super F>> SDFrame<FI2<T,F>> overMaxValue(Function<T,F> field);

    <F extends Comparable<? super F>> SDFrame<T> overMaxValue(SetFunction<T,F> setFunction,Function<T,F> field);

    <F extends Comparable<? super F>> SDFrame<FI2<T,F>> overMinValue(Function<T,F> field);

    <F extends Comparable<? super F>> SDFrame<T> overMinValue(SetFunction<T,F> setFunction,Function<T,F> field);

    SDFrame<FI2<T,Integer>> overCount();

    SDFrame<T> overCount(SetFunction<T,Integer> setFunction);
}
