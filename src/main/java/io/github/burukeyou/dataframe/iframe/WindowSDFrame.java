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

    SDFrame<FI2<T,Integer>> overRowNumber();

    WindowSDFrame<T> overRowNumberS(SetFunction<T,Integer> setFunction);

    SDFrame<FI2<T,Integer>> overRank();

    WindowSDFrame<T> overRankS(SetFunction<T,Integer> setFunction);

    SDFrame<FI2<T,Integer>> overDenseRank();

    WindowSDFrame<T> overDenseRankS(SetFunction<T,Integer> setFunction);

    SDFrame<FI2<T, BigDecimal>> overPercentRank();

    WindowSDFrame<T> overPercentRankS(SetFunction<T,BigDecimal> setFunction);

    SDFrame<FI2<T,BigDecimal>> overCumeDist();

    WindowSDFrame<T> overCumeDistS(SetFunction<T,BigDecimal> setFunction);

    <F> SDFrame<FI2<T,F>> overLag(Function<T,F> field, int n);

    <F> SDFrame<FI2<T,F>> overLead(Function<T,F> field, int n);

    <F> SDFrame<FI2<T,F>> overNthValue(Function<T,F> field, int n);

    <F> SDFrame<FI2<T,F>> overFirstValue(Function<T,F> field);

    <F> SDFrame<FI2<T,F>> overLastValue(Function<T,F> field);

    <F> SDFrame<FI2<T,BigDecimal>> overSum(Function<T,F> field);

    <F> WindowSDFrame<T> overSumS(SetFunction<T,BigDecimal> setFunction, Function<T,F> field);

    <F> SDFrame<FI2<T,BigDecimal>> overAvg(Function<T,F> field);

    <F> WindowSDFrame<T> overAvgS(SetFunction<T,BigDecimal> setFunction, Function<T,F> field);

    <F extends Comparable<? super F>> SDFrame<FI2<T,F>> overMaxValue(Function<T,F> field);

    <F extends Comparable<? super F>> WindowSDFrame<T> overMaxValueS(SetFunction<T,F> setFunction, Function<T,F> field);

    <F extends Comparable<? super F>> SDFrame<FI2<T,F>> overMinValue(Function<T,F> field);

    <F extends Comparable<? super F>> WindowSDFrame<T> overMinValueS(SetFunction<T,F> setFunction, Function<T,F> field);

    SDFrame<FI2<T,Integer>> overCount();

    WindowSDFrame<T> overCountS(SetFunction<T,Integer> setFunction);

    SDFrame<FI2<T,Integer>> overNtile(int n);

    WindowSDFrame<T> overNtileS(SetFunction<T,Integer> setFunction, int n);
}
