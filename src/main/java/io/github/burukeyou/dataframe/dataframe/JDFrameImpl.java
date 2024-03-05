package io.github.burukeyou.dataframe.dataframe;


import io.github.burukeyou.dataframe.IFrame;
import io.github.burukeyou.dataframe.JDFrame;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;


/**
 * @author caizhihao
 */
public class JDFrameImpl<T> extends AbstractDataFrame<T> implements JDFrame<T> {

    public List<T> dataList;

    public JDFrameImpl(List<T> list) {
        dataList = list;
    }

    @Override
    public  Stream<T> stream(){
        return dataList.stream();
    }

    public JDFrameImpl<T> read(Stream<T> stream){
        return read(stream.collect(toList()));
    }

    @Override
    public JDFrameImpl<T> read(List<T> list) {
        return new JDFrameImpl<>(list);
    }

    /**
     * ===========================    =====================================
     **/
    public List<T> toLists() {
        return dataList;
    }

    /**
     * ===========================   排序相关  =====================================
     **/

    @Override
    public JDFrameImpl<T> sortDesc(Comparator<T> comparator) {
        return read(stream().sorted(comparator.reversed()));
    }

    @Override
    public <R extends Comparable<R>> JDFrameImpl<T> sortDesc(Function<T, R> function) {
        sortDesc(Comparator.comparing(function));
        return this;
    }

    @Override
    public JDFrameImpl<T> sortAsc(Comparator<T> comparator) {
        return read(stream().sorted(comparator));
    }

    @Override
    public <R extends Comparable<R>> JDFrameImpl<T> sortAsc(Function<T, R> function) {
        return sortAsc(Comparator.comparing(function));
    }

    /** ===========================   截取相关  ===================================== **/

    /**
     * 截取前n个
     */
    @Override
    public JDFrameImpl<T> first(int n) {
        DFList<T> first = new DFList<>(toLists()).first(n);
        return read(first.build());
    }

    @Override
    protected IFrame<T> returnThis(Stream<T> stream) {
        return read(stream);
    }

    @Override
    protected <R> JDFrameImpl<R> readDF(List<R> dataList) {
        return new JDFrameImpl<>(dataList);
    }
}
