package io.github.burukeyou.dataframe.dataframe;


import io.github.burukeyou.dataframe.SDFrame;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;


/**
 * @author caizhihao
 */
public class SDFrameImpl<T>  extends AbstractDataFrame<T> implements SDFrame<T> {

    protected Stream<T> data;

    public SDFrameImpl(List<T> list) {
        this.data = list.stream();
    }

    public SDFrameImpl<T> read(List<T> list) {
        return new SDFrameImpl<>(list);
    }

    @Override
    public List<T> toLists() {
        return data.collect(toList());
    }

    @Override
    public  Stream<T> stream(){
        return data;
    }

    /**
     * ===========================   排序相关  =====================================
     **/

    @Override
    public SDFrameImpl<T> sortDesc(Comparator<T> comparator) {
        data = stream().sorted(comparator.reversed());
        return this;
    }

    @Override
    public <R extends Comparable<R>> SDFrameImpl<T> sortDesc(Function<T, R> function) {
        sortDesc(Comparator.comparing(function));
        return this;
    }

    @Override
    public SDFrameImpl<T> sortAsc(Comparator<T> comparator) {
        data = stream().sorted(comparator);
        return this;
    }

    @Override
    public <R extends Comparable<R>> SDFrameImpl<T> sortAsc(Function<T, R> function) {
        sortAsc(Comparator.comparing(function));
        return this;
    }


    /** ===========================   截取相关  ===================================== **/

    /**
     * 截取前n个
     */
    public SDFrameImpl<T> first(int n) {
        DFList<T> first = new DFList<>(toLists()).first(n);
        data = first.build().stream();
        return this;
    }


    @Override
    protected SDFrameImpl<T> returnThis(Stream<T> stream) {
        this.data = stream;
        return this;
    }

    @Override
    protected <R> SDFrameImpl<R> readDF(List<R> dataList) {
        return new SDFrameImpl<>(dataList);
    }
}
