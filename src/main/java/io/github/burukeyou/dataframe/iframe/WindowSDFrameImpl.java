package io.github.burukeyou.dataframe.iframe;

import io.github.burukeyou.dataframe.iframe.function.SetFunction;
import io.github.burukeyou.dataframe.iframe.item.FI2;
import io.github.burukeyou.dataframe.iframe.window.Window;

import java.math.BigDecimal;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author  caizhihao
 * @param <T>
 */
public class WindowSDFrameImpl<T> extends SDFrameImpl<T> implements WindowSDFrame<T> {

    public WindowSDFrameImpl(Window<T> window, Stream<T> data) {
        super(data);
        this.window = window;
    }

    protected <R> WindowSDFrameImpl<R> returnWDF(Window<R> window,Stream<R> stream) {
        return new WindowSDFrameImpl<>(window,stream);
    }

    @Override
    public SDFrameImpl<FI2<T, Integer>> overRowNumber() {
        return  super.overRowNumber(this.window);
    }

    @Override
    public WindowSDFrameImpl<T> overRowNumberS(SetFunction<T, Integer> setFunction) {
        return returnWDF(this.window, overRowNumberS(setFunction, this.window).stream());
    }

    @Override
    public SDFrameImpl<FI2<T, Integer>> overRank() {
        return super.overRank(this.window);
    }

    @Override
    public WindowSDFrameImpl<T> overRankS(SetFunction<T, Integer> setFunction) {
        return returnWDF(this.window, overRankS(setFunction, this.window).stream());
    }

    @Override
    public SDFrameImpl<FI2<T, Integer>> overDenseRank() {
        return super.overDenseRank(this.window);
    }

    @Override
    public WindowSDFrameImpl<T> overDenseRankS(SetFunction<T, Integer> setFunction) {
        return returnWDF(this.window, overDenseRankS(setFunction, this.window).stream());
    }

    @Override
    public SDFrameImpl<FI2<T, BigDecimal>> overPercentRank() {
        return super.overPercentRank(this.window);
    }

    @Override
    public WindowSDFrameImpl<T> overPercentRankS(SetFunction<T, BigDecimal> setFunction) {;
        return returnWDF(this.window, overPercentRankS(setFunction, this.window).stream());
    }

    @Override
    public SDFrameImpl<FI2<T, BigDecimal>> overCumeDist() {
        return super.overCumeDist(this.window);
    }

    @Override
    public WindowSDFrameImpl<T> overCumeDistS(SetFunction<T, BigDecimal> setFunction) {
        return returnWDF(this.window, overCumeDistS(setFunction, this.window).stream());
    }

    @Override
    public <F> SDFrameImpl<FI2<T, F>> overLag(Function<T, F> field, int n) {
        return super.overLag(this.window,field,n);
    }

    @Override
    public <F> SDFrameImpl<FI2<T, F>> overLead(Function<T, F> field, int n) {
        return super.overLead(this.window,field,n);
    }

    @Override
    public <F> SDFrameImpl<FI2<T, F>> overNthValue(Function<T, F> field, int n) {
        return super.overNthValue(this.window,field,n);
    }

    @Override
    public <F> SDFrameImpl<FI2<T, F>> overFirstValue(Function<T, F> field) {
        return super.overNthValue(this.window,field,1);
    }

    @Override
    public <F> SDFrameImpl<FI2<T, F>> overLastValue(Function<T, F> field) {
        return super.overNthValue(this.window,field,-1);

    }

    @Override
    public <F> SDFrameImpl<FI2<T, BigDecimal>> overSum(Function<T, F> field) {
        return super.overSum(this.window,field);
    }

    @Override
    public <F> WindowSDFrameImpl<T> overSumS(SetFunction<T, BigDecimal> setFunction, Function<T, F> field) {
        return returnWDF(this.window, overSumS(setFunction, this.window,field).stream());
    }

    @Override
    public <F> SDFrameImpl<FI2<T, BigDecimal>> overAvg(Function<T, F> field) {
        return overAvg(this.window,field);
    }

    @Override
    public <F> WindowSDFrameImpl<T> overAvgS(SetFunction<T, BigDecimal> setFunction, Function<T, F> field) {
        return returnWDF(this.window, overAvgS(setFunction, this.window, field).stream());
    }

    @Override
    public <F extends Comparable<? super F>> SDFrameImpl<FI2<T, F>> overMaxValue(Function<T, F> field) {
        return super.overMaxValue(this.window,field);
    }

    @Override
    public <F extends Comparable<? super F>> WindowSDFrameImpl<T> overMaxValueS(SetFunction<T, F> setFunction, Function<T, F> field) {
        return returnWDF(this.window, overMaxValueS(setFunction, this.window, field).stream());
    }

    @Override
    public <F extends Comparable<? super F>> SDFrameImpl<FI2<T, F>> overMinValue(Function<T, F> field) {
        return super.overMinValue(this.window,field);
    }

    @Override
    public <F extends Comparable<? super F>> WindowSDFrameImpl<T> overMinValueS(SetFunction<T, F> setFunction, Function<T, F> field) {
        return returnWDF(this.window, overMinValueS(setFunction,this.window,field).stream());
    }

    @Override
    public SDFrameImpl<FI2<T, Integer>> overCount() {
        return super.overCount(this.window);
    }

    @Override
    public WindowSDFrameImpl<T> overCountS(SetFunction<T, Integer> setFunction) {
        return returnWDF(this.window, overCountS(setFunction,this.window).stream());
    }
}
