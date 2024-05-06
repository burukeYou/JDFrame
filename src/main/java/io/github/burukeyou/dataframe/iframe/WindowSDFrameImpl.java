package io.github.burukeyou.dataframe.iframe;

import io.github.burukeyou.dataframe.iframe.function.SetFunction;
import io.github.burukeyou.dataframe.iframe.item.FI2;
import io.github.burukeyou.dataframe.iframe.window.Window;

import java.math.BigDecimal;
import java.util.Spliterator;
import java.util.function.Consumer;
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


    @Override
    public void forEach(Consumer<? super T> action) {
        super.forEach(action);
    }

    @Override
    public Spliterator<T> spliterator() {
        return super.spliterator();
    }


    protected <R> WindowSDFrameImpl<R> returnWDF(Window<R> window,Stream<R> stream) {
        return new WindowSDFrameImpl<>(window,stream);
    }

    @Override
    public SDFrameImpl<FI2<T, Integer>> overRowNumber() {
        return  overRowNumber(this.window);
    }

    @Override
    public WindowSDFrameImpl<T> overRowNumber(SetFunction<T, Integer> setFunction) {
        return returnWDF(this.window, overRowNumber(setFunction, this.window).stream());
    }

    @Override
    public SDFrameImpl<FI2<T, Integer>> overRank() {
        return overRank(this.window);
    }

    @Override
    public WindowSDFrameImpl<T> overRank(SetFunction<T, Integer> setFunction) {
        return returnWDF(this.window,overRank(setFunction, this.window).stream());
    }

    @Override
    public SDFrameImpl<FI2<T, Integer>> overDenseRank() {
        return overDenseRank(this.window);
    }

    @Override
    public WindowSDFrameImpl<T> overDenseRank(SetFunction<T, Integer> setFunction) {
        return returnWDF(this.window,overDenseRank(setFunction, this.window).stream());
    }

    @Override
    public SDFrameImpl<FI2<T, BigDecimal>> overPercentRank() {
        return overPercentRank(this.window);
    }

    @Override
    public WindowSDFrameImpl<T> overPercentRank(SetFunction<T, BigDecimal> setFunction) {;
        return returnWDF(this.window,overPercentRank(setFunction, this.window).stream());
    }

    @Override
    public SDFrameImpl<FI2<T, BigDecimal>> overCumeDist() {
        return overCumeDist(this.window);
    }

    @Override
    public WindowSDFrameImpl<T> overCumeDist(SetFunction<T, BigDecimal> setFunction) {
        return returnWDF(this.window, overCumeDist(setFunction, this.window).stream());
    }

    @Override
    public <F> SDFrameImpl<FI2<T, F>> overLag(Function<T, F> field, int n) {
        return overLag(field,n);
    }

    @Override
    public <F> SDFrameImpl<FI2<T, F>> overLead(Function<T, F> field, int n) {
        return overLead(field,n);
    }

    @Override
    public <F> SDFrameImpl<FI2<T, F>> overNthValue(Function<T, F> field, int n) {
        return overNthValue(field,n);
    }

    @Override
    public <F> SDFrameImpl<FI2<T, F>> overFirstValue(Function<T, F> field) {
        return overNthValue(field,1);
    }

    @Override
    public <F> SDFrameImpl<FI2<T, F>> overLastValue(Function<T, F> field) {
        return overNthValue(field,-1);

    }

    @Override
    public <F> SDFrameImpl<FI2<T, BigDecimal>> overSum(Function<T, F> field) {
        return overSum(this.window,field);
    }

    @Override
    public <F> WindowSDFrameImpl<T> overSum(SetFunction<T, BigDecimal> setFunction, Function<T, F> field) {
        return returnWDF(this.window,overSum(setFunction, this.window,field).stream());
    }

    @Override
    public <F> SDFrameImpl<FI2<T, BigDecimal>> overAvg(Function<T, F> field) {
        return overAvg(this.window,field);
    }

    @Override
    public <F> WindowSDFrameImpl<T> overAvg(SetFunction<T, BigDecimal> setFunction, Function<T, F> field) {
        return returnWDF(this.window,overAvg(setFunction, this.window, field).stream());
    }

    @Override
    public <F extends Comparable<? super F>> SDFrameImpl<FI2<T, F>> overMaxValue(Function<T, F> field) {
        return overMaxValue(this.window,field);
    }

    @Override
    public <F extends Comparable<? super F>> WindowSDFrameImpl<T> overMaxValue(SetFunction<T, F> setFunction, Function<T, F> field) {
        return returnWDF(this.window,overMaxValue(setFunction, this.window, field).stream());
    }

    @Override
    public <F extends Comparable<? super F>> SDFrameImpl<FI2<T, F>> overMinValue(Function<T, F> field) {
        return overMinValue(this.window,field);
    }

    @Override
    public <F extends Comparable<? super F>> WindowSDFrameImpl<T> overMinValue(SetFunction<T, F> setFunction, Function<T, F> field) {
        return returnWDF(this.window,overMinValue(setFunction,this.window,field).stream());
    }

    @Override
    public SDFrameImpl<FI2<T, Integer>> overCount() {
        return overCount(this.window);
    }

    @Override
    public WindowSDFrameImpl<T> overCount(SetFunction<T, Integer> setFunction) {
        return returnWDF(this.window,overCount(setFunction,this.window).stream());
    }
}
