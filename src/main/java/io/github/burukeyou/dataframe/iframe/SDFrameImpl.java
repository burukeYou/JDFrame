package io.github.burukeyou.dataframe.iframe;


import io.github.burukeyou.dataframe.iframe.function.ReplenishFunction;
import io.github.burukeyou.dataframe.iframe.function.SetFunction;
import io.github.burukeyou.dataframe.iframe.item.FI2;
import io.github.burukeyou.dataframe.iframe.item.FI3;
import io.github.burukeyou.dataframe.iframe.item.FI4;
import io.github.burukeyou.dataframe.iframe.support.*;
import io.github.burukeyou.dataframe.iframe.window.Sorter;
import io.github.burukeyou.dataframe.iframe.window.Window;
import io.github.burukeyou.dataframe.util.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;


/**
 * @author caizhihao
 */
public class SDFrameImpl<T>  extends AbstractDataFrameImpl<T> implements SDFrame<T> {

    protected Stream<T> data;

    public SDFrameImpl(Stream<T> data) {
        List<T> tmp = data.collect(toList());
        if (ListUtils.isNotEmpty(tmp)){
            this.fieldList = buildFieldList(tmp.get(0));
        }
        this.data = tmp.stream();
    }

    public SDFrameImpl(List<T> list) {
        this.data = list.stream();
        if (!list.isEmpty()){
            fieldList = buildFieldList(list.get(0));
        }
    }

    public <R> SDFrameImpl<R> read(List<R> list) {
        return new SDFrameImpl<>(list);
    }

    @Override
    public <R> SDFrame<R> from(Stream<R> data) {
        return new SDFrameImpl<>(data);
    }

    @Override
    public SDFrameImpl<T> forEachDo(Consumer<? super T> action) {
        this.forEach(action);
        return this;
    }

    @Override
    public SDFrame<T> defaultScale(int scale) {
        initDefaultScale(scale,defaultRoundingMode);
        return this;
    }

    @Override
    public SDFrame<T> defaultScale(int scale, RoundingMode roundingMode) {
        initDefaultScale(scale,roundingMode);
        return this;
    }

    @Override
    public <R> SDFrameImpl<R> map(Function<T, R> map) {
        return returnDF(stream().map(map).collect(toList()));
    }

    @Override
    public <R extends Number> SDFrame<T> mapPercent(Function<T, R> get, SetFunction<T, BigDecimal> set) {
        return mapPercent(get,set,2);
    }

    @Override
    public <R extends Number> SDFrame<T> mapPercent(Function<T,R> get, SetFunction<T,BigDecimal> set, int scale){
        toLists().forEach(e -> {
            R value = get.apply(e);
            BigDecimal percentageValue = MathUtils.percentage(MathUtils.toBigDecimal(value), scale);
            set.accept(e,percentageValue);
        });
        return this;
    }

    @Override
    public SDFrameImpl<List<T>> partition(int n) {
        return returnDF(new PartitionList<>(toLists(), n));
    }


    @Override
    public SDFrameImpl<T> append(T t) {
        List<T> ts = toLists();
        ts.add(t);
        data = ts.stream();
        return this;
    }

    @Override
    public SDFrameImpl<T> union(IFrame<T> other) {
        if (other.count() <= 0){
            return this;
        }
        List<T> ts = toLists();
        ts.addAll(other.toLists());
        return returnDF(ts);
    }

    @Override
    public <R, K> SDFrameImpl<R> join(IFrame<K> other, JoinOn<T, K> on, Join<T, K, R> join) {
        return returnDF(joinList(other,on,join));
    }

    @Override
    public <R, K> SDFrameImpl<R> join(IFrame<K> other, JoinOn<T, K> on) {
        return join(other,on,new DefaultJoin<>());
    }

    @Override
    public <R, K> SDFrameImpl<R> leftJoin(IFrame<K> other, JoinOn<T, K> on, Join<T, K, R> join) {
        return returnDF(leftJoinList(other,on,join));
    }

    @Override
    public <R, K> SDFrameImpl<R> leftJoin(IFrame<K> other, JoinOn<T, K> on) {
        return leftJoin(other,on,new DefaultJoin<>());
    }

    @Override
    public <R, K> SDFrameImpl<R> rightJoin(IFrame<K> other, JoinOn<T, K> on, Join<T, K, R> join) {
        return returnDF(rightJoinList(other,on,join));
    }

    @Override
    public <R, K> SDFrameImpl<R> rightJoin(IFrame<K> other, JoinOn<T, K> on) {
        return rightJoin(other,on,new DefaultJoin<>());
    }

    @Override
    public SDFrameImpl<FI2<T, Integer>> addRowNumberCol() {
        List<FI2<T, Integer>> result = new ArrayList<>();
        int index = 1;
        for (T t : this) {
            result.add(new FI2<>(t,index++));
        }
        return returnDF(result);
    }

    @Override
    public SDFrameImpl<FI2<T, Integer>> addRowNumberCol(Sorter<T> sorter) {
        return sortAsc(sorter).addRowNumberCol();
    }

    @Override
    public SDFrameImpl<T> addRowNumberCol(SetFunction<T, Integer> set) {
        int index = 1;
        for (T t : this) {
           set.accept(t,index++);
        }
        return this;
    }

    @Override
    public SDFrameImpl<T> addRowNumberCol(Sorter<T> sorter, SetFunction<T, Integer> set) {
        return sortAsc(sorter).addRowNumberCol(set);
    }

    @Override
    public SDFrameImpl<FI2<T, Integer>> addRankCol(Sorter<T> sorter) {
        return overRank(Window.sortBy(sorter));
    }


    @Override
    public SDFrame<T> addRankCol(Sorter<T> sorter, SetFunction<T, Integer> set) {
        return fi2Frame(this.addRankCol(sorter),set);
    }


    @Override
    public List<T> toLists() {
        List<T> tmp = data.collect(toList());
        data = tmp.stream();
        return tmp;
    }

    @Override
    public  Stream<T> stream(){
        return data;
    }

    @Override
    public long count() {
        List<T> tmp = stream().collect(toList());
        data = tmp.stream();
        return tmp.size();
    }

    @Override
    public <K> SDFrame<FI2<K, List<T>>> group(Function<T, K> key) {
        return returnDF(groupKey(key));
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
    public <R extends Comparable<? super R>> SDFrameImpl<T> sortDesc(Function<T, R> function) {
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

    @Override
    public SDFrame<T> cutFirstRank(Sorter<T> sorter, int n) {
        return overRank(Window.sortBy(sorter)).whereLe(FI2::getC2, n).map(FI2::getC1);
    }


    /** ===========================   截取相关  ===================================== **/

    @Override
    public SDFrame<T> cutFirst(int n) {
        DFList<T> first = new DFList<>(toLists()).first(n);
        List<T> build = first.build();
        return returnThis(build);
    }

    @Override
    public SDFrame<T> cutLast(int n) {
        DFList<T> first = new DFList<>(toLists()).last(n);
        data = first.build().stream();
        return this;
    }

    @Override
    public SDFrame<T> cut(Integer startIndex, Integer endIndex) {
        return returnDF(subList(startIndex,endIndex));
    }

    @Override
    public SDFrame<T> cutPage(int page, int pageSize) {
        return returnDF(page(page,pageSize));
    }

    @Override
    public SDFrame<T> distinct() {
        data = stream().distinct();
        return this;
    }

    @Override
    public <R extends Comparable<R>> SDFrame<T> distinct(Function<T, R> function) {
        return distinct(Comparator.comparing(function));
    }

    @Override
    public <R extends Comparable<R>> SDFrame<T> distinct(Comparator<T> comparator) {
        ArrayList<T> tmp = stream().collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparator)), ArrayList::new));
        data = tmp.stream();
        return this;
    }

    @Override
    public long countDistinct(Comparator<T> comparator) {
        return distinct(comparator).count();
    }

    @Override
    public <R extends Comparable<R>> long countDistinct(Function<T, R> function) {
        return countDistinct(Comparator.comparing(function));
    }

    /**
     * ===========================   筛选相关  =====================================
     **/
    @Override
    public SDFrame<T> where(Predicate<? super T> predicate) {
        return returnThis(stream().filter(predicate));
    }

    @Override
    public <R> SDFrame<T> whereNull(Function<T, R> function) {
        return returnThis(whereNotNullStream(function));
    }

    public <R> SDFrame<T> whereNotNull(Function<T, R> function) {
        return returnThis(whereNotNullStream(function));
    }

    @Override
    public <R extends Comparable<R>> SDFrame<T> whereBetween(Function<T, R> function, R start, R end) {
        if (start == null && end == null) {
            return this;
        }
        return returnThis(whereBetweenStream(function,start,end));
    }

    @Override
    public <R extends Comparable<R>> SDFrame<T> whereBetweenN(Function<T, R> function, R start, R end) {
        if (start == null && end == null) {
            return this;
        }
        return returnThis(whereBetweenNStream(function,start,end));
    }

    @Override
    public <R extends Comparable<R>> SDFrame<T> whereBetweenR(Function<T, R> function, R start, R end) {
        // 筛选条件都不存在默认不筛选
        if (start == null && end == null) {
            return this;
        }
        return returnThis(whereBetweenRStream(function,start,end));
    }

    @Override
    public <R extends Comparable<R>> SDFrame<T> whereBetweenL(Function<T, R> function, R start, R end) {
        if (start == null && end == null) {
            return this;
        }
        return returnThis(whereBetweenLStream(function,start,end));
    }

    @Override
    public <R extends Comparable<R>> SDFrame<T> whereNotBetween(Function<T, R> function, R start, R end) {
        if (start == null || end == null) {
            return this;
        }
        return returnThis(whereNotBetweenStream(function,start,end));
    }
    @Override
    public <R extends Comparable<R>> SDFrame<T> whereNotBetweenN(Function<T, R> function, R start, R end) {
        if (start == null || end == null) {
            return this;
        }
        return returnThis(whereNotBetweenNStream(function,start,end));
    }


    public <R> SDFrame<T> whereIn(Function<T, R> function, List<R> list) {
        if (list == null || list.isEmpty()) {
            return this;
        }
        return returnThis(whereInStream(function,list));
    }


    public <R> SDFrame<T> whereNotIn(Function<T, R> function, List<R> list) {
        if (list == null || list.isEmpty()) {
            return this;
        }
        return returnThis(whereNotInStream(function,list));
    }


    public SDFrame<T> whereTrue(Predicate<T> predicate) {
        return returnThis(stream().filter(predicate));
    }


    public SDFrame<T> whereNotTrue(Predicate<T> predicate) {
        return whereTrue(predicate.negate());
    }


    public <R> SDFrame<T> whereEq(Function<T, R> function, R value) {
        if (null == value) {
            return this;
        }
        return  returnThis(whereEqStream(function,value));
    }


    public <R> SDFrame<T> whereNotEq(Function<T, R> function, R value) {
        if (value == null) {
            return this;
        }
        return returnThis(whereNotEqStream(function,value));
    }


    public <R extends Comparable<R>> SDFrame<T> whereGt(Function<T, R> function, R value) {
        if (value == null) {
            return this;
        }
        return returnThis(whereGtStream(function,value));
    }


    public <R extends Comparable<R>> SDFrame<T> whereGe(Function<T, R> function, R value) {
        if (value == null) {
            return this;
        }
        return returnThis(whereGeStream(function,value));
    }


    public <R extends Comparable<R>> SDFrame<T> whereLt(Function<T, R> function, R value) {
        if (value == null) {
            return this;
        }
        return returnThis(whereLtStream(function,value));
    }


    public <R extends Comparable<R>> SDFrame<T> whereLe(Function<T, R> function, R value) {
        if (value == null) {
            return this;
        }
        return returnThis(whereLeStream(function,value));
    }


    public <R> SDFrame<T> whereLike(Function<T, R> function, R value) {
        if (value == null) {
            return this;
        }
        return returnThis(whereLikeStream(function,value));
    }


    public <R> SDFrame<T> whereNotLike(Function<T, R> function, R value) {
        if (value == null) {
            return this;
        }
        return returnThis(whereNotLikeStream(function,value));
    }


    public <R> SDFrame<T> whereLikeLeft(Function<T, R> function, R value) {
        if (value == null) {
            return this;
        }
        return returnThis(whereLikeLeftStream(function,value));
    }


    public <R> SDFrame<T> whereLikeRight(Function<T, R> function, R value) {
        if (value == null) {
            return this;
        }
        return returnThis(whereLikeRightStream(function,value));
    }



    /** ===========================   分组相关  ===================================== **/



    public <K,R extends Number> SDFrame<FI2<K, BigDecimal>> groupBySum(Function<T, K> key, NumberFunction<T,R> value) {
        Collector<T, ?, BigDecimal> tBigDecimalCollector = CollectorsPlusUtil.summingBigDecimalForNumber(value);
        List<FI2<K, BigDecimal>> collect = groupKey(key, tBigDecimalCollector);
        return returnDF(collect);
    }


    public <K, J,R extends Number> SDFrame<FI3<K, J, BigDecimal>> groupBySum(Function<T, K> key,
                                                            Function<T, J> key2,
                                                            NumberFunction<T,R> value) {
        Collector<T, ?, BigDecimal> tBigDecimalCollector = CollectorsPlusUtil.summingBigDecimalForNumber(value);
        List<FI3<K, J, BigDecimal>> collect = groupKey(key, key2, tBigDecimalCollector);
        return returnDF(collect);
    }



    public <K, J, H,R extends Number> SDFrame<FI4<K, J, H, BigDecimal>> groupBySum(Function<T, K> key,
                                                                  Function<T, J> key2,
                                                                  Function<T, H> key3,
                                                                  NumberFunction<T,R> value) {
        Collector<T, ?, BigDecimal> tBigDecimalCollector = CollectorsPlusUtil.summingBigDecimalForNumber(value);
        List<FI4<K, J, H, BigDecimal>> collect = groupKey(key, key2, key3, tBigDecimalCollector);
        return returnDF(collect);
    }


    public <K> SDFrame<FI2<K, Long>> groupByCount(Function<T, K> key) {
        Collector<Object, ?, Long> counting = counting();
        Map<K, Long> collect = stream().collect(groupingBy(key, counting));
        return returnDF(FrameUtil.toListFI2(collect));
    }


    public <K, J> SDFrame<FI3<K, J, Long>> groupByCount(Function<T, K> key,
                                                        Function<T, J> key2) {
        Collector<Object, ?, Long> counting = counting();
        Map<K, Map<J, Long>> collect = stream().collect(groupingBy(key, groupingBy(key2, counting)));
        return returnDF(FrameUtil.toListFI3(collect));
    }


    public <K, J, H> SDFrame<FI4<K, J, H, Long>> groupByCount(Function<T, K> key,
                                                              Function<T, J> key2,
                                                              Function<T, H> key3) {
        Collector<Object, ?, Long> counting = counting();
        Map<K, Map<J, Map<H, Long>>> collect = stream().collect(groupingBy(key, groupingBy(key2, groupingBy(key3, counting))));
        return returnDF(FrameUtil.toListFI4(collect));
    }


    public <K,R extends Number> SDFrame<FI3<K, BigDecimal,Long>> groupBySumCount(Function<T, K> key, NumberFunction<T,R> value) {
        List<T> dataList = toLists();
        Collector<T, ?, BigDecimal> tBigDecimalCollector = CollectorsPlusUtil.summingBigDecimalForNumber(value);
        List<FI2<K, BigDecimal>> sumList = returnDF(dataList).groupKey(key, tBigDecimalCollector);
        List<FI2<K, Long>> countList =  returnDF(dataList).groupByCount(key).toLists();
        Map<K, Long> countMap = countList.stream().collect(toMap(FI2::getC1, FI2::getC2));
        List<FI3<K, BigDecimal, Long>> collect = sumList.stream().map(e -> new FI3<>(e.getC1(), e.getC2(), countMap.get(e.getC1()))).collect(Collectors.toList());
        return returnDF(collect);
    }


    public <K, J,R extends Number> SDFrame<FI4<K, J, BigDecimal, Long>> groupBySumCount(Function<T, K> key,
                                                                       Function<T, J> key2,
                                                                       NumberFunction<T,R> value) {
        List<T> dataList = toLists();
        Collector<T, ?, BigDecimal> tBigDecimalCollector = CollectorsPlusUtil.summingBigDecimalForNumber(value);
        List<FI3<K, J, BigDecimal>> sumList = returnDF(dataList).groupKey(key, key2, tBigDecimalCollector);
        List<FI3<K, J, Long>> countList =  returnDF(dataList).groupByCount(key, key2).toLists();
        // 合并sum和count字段
        Map<String, FI3<K, J, Long>> countMap = countList.stream().collect(toMap(e -> e.getC1() + "_" + e.getC2(), Function.identity()));
        List<FI4<K, J, BigDecimal, Long>> collect = sumList.stream().map(e -> {
            FI3<K, J, Long> countItem = countMap.get(e.getC1() + "_" + e.getC2());
            return new FI4<>(e.getC1(), e.getC2(), e.getC3(), countItem.getC3());
        }).collect(Collectors.toList());
        return returnDF(collect);
    }


    public <K,R extends Number> SDFrame<FI2<K, BigDecimal>> groupByAvg(Function<T, K> key,
                                                      NumberFunction<T,R> value) {
        Collector<T, ?, BigDecimal> tBigDecimalCollector = CollectorsPlusUtil.averagingBigDecimal(value, defaultScale, getOldRoundingMode());
        List<FI2<K, BigDecimal>> collect = groupKey(key, tBigDecimalCollector);
        return returnDF(collect);
    }


    public <K, J,R extends Number> SDFrame<FI3<K, J, BigDecimal>> groupByAvg(Function<T, K> key,
                                                            Function<T, J> key2,
                                                            NumberFunction<T,R> value) {

        Collector<T, ?, BigDecimal> tBigDecimalCollector = CollectorsPlusUtil.averagingBigDecimal(value, defaultScale, getOldRoundingMode());
        List<FI3<K, J, BigDecimal>> collect = groupKey(key, key2, tBigDecimalCollector);
        return returnDF(collect);
    }


    public <K, J, H,R extends Number> SDFrame<FI4<K, J, H, BigDecimal>> groupByAvg(Function<T, K> key,
                                                                  Function<T, J> key2,
                                                                  Function<T, H> key3,
                                                                  NumberFunction<T,R> value) {
        Collector<T, ?, BigDecimal> tBigDecimalCollector = CollectorsPlusUtil.averagingBigDecimal(value, defaultScale, getOldRoundingMode());
        List<FI4<K, J, H, BigDecimal>> collect = groupKey(key, key2, key3, tBigDecimalCollector);
        return returnDF(collect);
    }


    @Override
    public <K, V extends Comparable<? super V>> SDFrame<FI2<K, T>> groupByMax(Function<T, K> key,
                                                                      Function<T, V> value) {
        Map<K, T> collect = stream().collect(groupingBy(key, collectingAndThen(toList(), getListMaxFunction(value))));
        return returnDF(FrameUtil.toListFI2(collect));
    }

    @Override
    public <K, J, V extends Comparable<? super V>> SDFrame<FI3<K, J, T>> groupByMax(Function<T, K> key, Function<T, J> key2, Function<T, V> value) {
        Map<K, Map<J, T>> collect = groupToMap(key, key2,getListMaxFunction(value));
        return returnDF(FrameUtil.toListFI3(collect));
    }

    @Override
    public <K, V extends Comparable<? super V>> SDFrame<FI2<K, V>> groupByMaxValue(Function<T, K> key, Function<T, V> value) {
        return groupByMax(key, value).map(e -> new FI2<>(e.getC1(), getApplyValue(value,e.getC2())));
    }

    @Override
    public <K, J, V extends Comparable<? super V>> SDFrame<FI3<K, J, V>> groupByMaxValue(Function<T, K> key, Function<T, J> key2, Function<T, V> value) {
        return groupByMax(key, key2,value).map(e -> new FI3<>(e.getC1(),e.getC2(),getApplyValue(value,e.getC3())));
    }

    @Override
    public <K, V extends Comparable<? super V>> SDFrame<FI2<K, T>> groupByMin(Function<T, K> key,
                                                                      Function<T, V> value) {
        Map<K, T> collect = stream().collect(groupingBy(key, collectingAndThen(toList(), e -> e.stream().min(Comparator.comparing(value)).orElse(null))));
        return returnDF(FrameUtil.toListFI2(collect));
    }

    @Override
    public <K, J, V extends Comparable<? super V>> SDFrame<FI3<K, J, T>> groupByMin(Function<T, K> key, Function<T, J> key2, Function<T, V> value) {
        Map<K, Map<J, T>> collect = groupToMap(key, key2,getListMinFunction(value));
        return returnDF(FrameUtil.toListFI3(collect));
    }

    @Override
    public <K, V extends Comparable<? super V>> SDFrame<FI2<K, V>> groupByMinValue(Function<T, K> key, Function<T, V> value) {
        return groupByMin(key, value).map(e -> new FI2<>(e.getC1(),getApplyValue(value,e.getC2())));
    }

    @Override
    public <K, J, V extends Comparable<? super V>> SDFrame<FI3<K, J, V>> groupByMinValue(Function<T, K> key, Function<T, J> key2, Function<T, V> value) {
        return groupByMin(key, key2,value).map(e -> new FI3<>(e.getC1(),e.getC2(),getApplyValue(value,e.getC3())));
    }

    @Override
    public <K, V extends Comparable<? super V>> SDFrame<FI2<K, MaxMin<V>>> groupByMaxMinValue(Function<T, K> key,
                                                                                      Function<T, V> value) {
        Map<K, MaxMin<V>> map = stream().collect(groupingBy(key, collectingAndThen(toList(), getListGroupMaxMinValueFunction(value))));
        return returnDF(FrameUtil.toListFI2(map));
    }

    @Override
    public <K, J, V extends Comparable<? super V>> SDFrame<FI3<K, J, MaxMin<V>>> groupByMaxMinValue(Function<T, K> key,
                                                                                            Function<T, J> key2,
                                                                                            Function<T, V> value) {
        Map<K, Map<J, MaxMin<V>>> map = stream().collect(groupingBy(key, groupingBy(key2, collectingAndThen(toList(), getListGroupMaxMinValueFunction(value)))));
        return returnDF(FrameUtil.toListFI3(map));
    }

    @Override
    public <K, V extends Comparable<? super V>> SDFrame<FI2<K, MaxMin<T>>> groupByMaxMin(Function<T, K> key,
                                                                                 Function<T, V> value) {
        Map<K, MaxMin<T>> map = stream().collect(groupingBy(key, collectingAndThen(toList(), getListGroupMaxMinFunction(value))));
        return returnDF(FrameUtil.toListFI2(map));
    }

    @Override
    public <K, J, V extends Comparable<? super V>> SDFrame<FI3<K, J, MaxMin<T>>> groupByMaxMin(Function<T, K> key,
                                                                                       Function<T, J> key2,
                                                                                       Function<T, V> value) {
        Map<K, Map<J, MaxMin<T>>> map = stream().collect(groupingBy(key, groupingBy(key2, collectingAndThen(toList(), getListGroupMaxMinFunction(value)))));
        return returnDF(FrameUtil.toListFI3(map));
    }

    @Override
    public WindowSDFrame<T> window(Window<T> window) {
        WindowSDFrameImpl<T> frame = new WindowSDFrameImpl<>(window, stream());
        transmitMember(this,frame);
        return frame;
    }

    @Override
    public WindowSDFrame<T> window() {
        WindowSDFrameImpl<T> frame = new WindowSDFrameImpl<>(emptyWindow,stream());
        transmitMember(this,frame);
        return frame;
    }

    public <F> SDFrameImpl<T> fi2Frame(SDFrameImpl<FI2<T, F>> frame,SetFunction<T, F> setFunction){
        return frame.forEachDo(e -> setFunction.accept(e.getC1(),e.getC2())).map(FI2::getC1);
    }

    @Override
    public  SDFrameImpl<FI2<T, Integer>> overRowNumber(Window<T> overParam) {
        return returnDF(windowFunctionForRowNumber(overParam));
    }

    @Override
    public SDFrame<FI2<T, Integer>> overRowNumber() {
        return overRowNumber(emptyWindow);
    }
    @Override
    public SDFrameImpl<T> overRowNumberS(SetFunction<T,Integer> setFunction, Window<T> overParam) {
        return fi2Frame(overRowNumber(overParam),setFunction);
    }

    @Override
    public SDFrame<T> overRowNumberS(SetFunction<T, Integer> setFunction) {
        return overRowNumberS(setFunction, emptyWindow);
    }

    @Override
    public  SDFrameImpl<FI2<T, Integer>> overRank(Window<T> overParam) {
        return returnDF(windowFunctionForRank(overParam));
    }

    @Override
    public SDFrameImpl<T> overRankS(SetFunction<T, Integer> setFunction, Window<T> overParam) {
        return fi2Frame(overRank(overParam),setFunction);
    }

    @Override
    public  SDFrameImpl<FI2<T, Integer>> overDenseRank(Window<T> overParam) {
        return returnDF(windowFunctionForDenseRank(overParam));
    }

    @Override
    public SDFrameImpl<T> overDenseRankS(SetFunction<T, Integer> setFunction, Window<T> overParam) {
        return fi2Frame(overDenseRank(overParam),setFunction);
    }

    @Override
    public  SDFrameImpl<FI2<T, BigDecimal>> overPercentRank(Window<T> overParam) {
        return returnDF(windowFunctionForPercentRank(overParam));
    }

    @Override
    public SDFrameImpl<T> overPercentRankS(SetFunction<T, BigDecimal> setFunction, Window<T> overParam) {
        return fi2Frame(overPercentRank(overParam),setFunction);
    }
    @Override
    public  SDFrameImpl<FI2<T, BigDecimal>> overCumeDist(Window<T> overParam) {
        return returnDF(windowFunctionForCumeDist(overParam));
    }

    @Override
    public SDFrameImpl<T> overCumeDistS(SetFunction<T, BigDecimal> setFunction, Window<T> overParam) {
        return fi2Frame(overCumeDist(overParam),setFunction);
    }

    @Override
    public <F> SDFrameImpl<FI2<T, F>> overLag(Window<T> overParam, Function<T, F> field, int n) {
        return returnDF(windowFunctionForLag(overParam,field,n));
    }

    @Override
    public <F> SDFrame<T> overLagS(SetFunction<T, F> setFunction, Window<T> overParam, Function<T, F> field, int n) {
        return fi2Frame(overLag(overParam,field,n),setFunction);
    }

    @Override
    public <F> SDFrameImpl<FI2<T, F>> overLag(Function<T, F> field, int n) {
        return overLag(emptyWindow,field,n);
    }

    @Override
    public <F> SDFrame<T> overLagS(SetFunction<T, F> setFunction, Function<T, F> field, int n) {
        return fi2Frame(overLag(field,n),setFunction);
    }

    @Override
    public <F> SDFrame<T> overLeadS(SetFunction<T, F> setFunction, Window<T> overParam, Function<T, F> field, int n) {
        return fi2Frame(overLead(overParam,field,n),setFunction);
    }

    @Override
    public <F> SDFrameImpl<FI2<T, F>> overLead(Window<T> overParam, Function<T, F> field, int n) {
        return returnDF(windowFunctionForLead(overParam,field,n));
    }

    @Override
    public <F> SDFrameImpl<FI2<T, F>> overLead(Function<T, F> field, int n) {
        return overLead(emptyWindow,field,n);
    }

    @Override
    public <F> SDFrame<T> overLeadS(SetFunction<T, F> setFunction, Function<T, F> field, int n) {
        return fi2Frame(overLead(field,n),setFunction);
    }

    @Override
    public <F> SDFrameImpl<FI2<T, F>> overNthValue(Window<T> overParam, Function<T, F> field, int n) {
        return returnDF(windowFunctionForNthValue(overParam,field,n));
    }

    @Override
    public <F> SDFrame<T> overNthValueS(SetFunction<T, F> setFunction, Window<T> overParam, Function<T, F> field, int n) {
        return fi2Frame(overNthValue(overParam,field,n),setFunction);
    }

    @Override
    public <F> SDFrameImpl<FI2<T, F>> overNthValue(Function<T, F> field, int n) {
        return overNthValue(emptyWindow,field,n);
    }

    @Override
    public <F> SDFrameImpl<T> overNthValueS(SetFunction<T, F> setFunction, Function<T, F> field, int n) {
        return fi2Frame(overNthValue(field,n),setFunction);
    }

    @Override
    public <F> SDFrameImpl<FI2<T, F>> overFirstValue(Window<T> overParam, Function<T, F> field) {
        return overNthValue(overParam,field,1);
    }

    @Override
    public <F> SDFrame<T> overFirstValueS(SetFunction<T, F> setFunction, Window<T> overParam, Function<T, F> field) {
        return fi2Frame(overFirstValue(overParam, field),setFunction);
    }

    @Override
    public <F> SDFrameImpl<FI2<T, F>> overFirstValue(Function<T, F> field) {
        return overFirstValue(emptyWindow,field);
    }

    @Override
    public <F> SDFrameImpl<T> overFirstValueS(SetFunction<T, F> setFunction, Function<T, F> field) {
        return fi2Frame(overFirstValue(field),setFunction);
    }

    @Override
    public <F> SDFrameImpl<FI2<T, F>> overLastValue(Window<T> overParam, Function<T, F> field) {
        return overNthValue(overParam,field,-1);
    }

    @Override
    public <F> SDFrameImpl<T> overLastValueS(SetFunction<T, F> setFunction, Window<T> overParam, Function<T, F> field) {
        return fi2Frame(overLastValue(overParam,field),setFunction);
    }

    @Override
    public <F> SDFrameImpl<FI2<T, F>> overLastValue(Function<T, F> field) {
        return overLastValue(emptyWindow,field);
    }

    @Override
    public <F> SDFrame<T> overLastValueS(SetFunction<T, F> setFunction, Function<T, F> field) {
        return fi2Frame(overLastValue(field),setFunction);
    }

    @Override
    public <F> SDFrameImpl<FI2<T, BigDecimal>> overSum(Window<T> overParam, Function<T, F> field) {
        return returnDF(windowFunctionForSum(overParam,field));
    }

    @Override
    public <F> SDFrameImpl<FI2<T, BigDecimal>> overSum(Function<T, F> field) {
        return overSum(emptyWindow,field);
    }

    @Override
    public <F> SDFrameImpl<T> overSumS(SetFunction<T, BigDecimal> setFunction, Window<T> overParam, Function<T, F> field) {
        return fi2Frame(overSum(overParam,field),setFunction);
    }

    @Override
    public <F> SDFrameImpl<T> overSumS(SetFunction<T, BigDecimal> setFunction, Function<T, F> field) {
        return overSumS(setFunction, emptyWindow,field);
    }

    @Override
    public <F> SDFrameImpl<FI2<T, BigDecimal>> overAvg(Window<T> overParam, Function<T, F> field) {
        return returnDF(windowFunctionForAvg(overParam,field));
    }

    @Override
    public <F> SDFrameImpl<FI2<T, BigDecimal>> overAvg(Function<T, F> field) {
        return overAvg(emptyWindow,field);
    }

    @Override
    public <F> SDFrameImpl<T> overAvgS(SetFunction<T, BigDecimal> setFunction, Window<T> overParam, Function<T, F> field) {
        return fi2Frame(overAvg(overParam,field),setFunction);
    }

    @Override
    public <F> SDFrameImpl<T> overAvgS(SetFunction<T, BigDecimal> setFunction, Function<T, F> field) {
        return overAvgS(setFunction, emptyWindow,field);
    }

    @Override
    public <F extends Comparable<? super F>>  SDFrameImpl<FI2<T, F>> overMaxValue(Window<T> overParam, Function<T, F> field) {
        return returnDF(windowFunctionForMaxValue(overParam,field));
    }

    @Override
    public <F extends Comparable<? super F>> SDFrameImpl<FI2<T, F>> overMaxValue(Function<T, F> field) {
        return overMaxValue(emptyWindow,field);
    }

    @Override
    public <F extends Comparable<? super F>> SDFrameImpl<T> overMaxValueS(SetFunction<T, F> setFunction, Window<T> overParam, Function<T, F> field) {
        return fi2Frame(overMaxValue(overParam,field),setFunction);
    }

    @Override
    public <F extends Comparable<? super F>> SDFrameImpl<T> overMaxValueS(SetFunction<T, F> setFunction, Function<T, F> field) {
        return overMaxValueS(setFunction, emptyWindow,field);
    }

    @Override
    public <F extends Comparable<? super F>> SDFrameImpl<FI2<T, F>> overMinValue(Window<T> overParam, Function<T, F> field) {
        return returnDF(windowFunctionForMinValue(overParam,field));
    }

    @Override
    public <F extends Comparable<? super F>> SDFrameImpl<FI2<T, F>> overMinValue(Function<T, F> field) {
        return overMinValue(emptyWindow,field);
    }

    @Override
    public <F extends Comparable<? super F>> SDFrameImpl<T> overMinValueS(SetFunction<T, F> setFunction, Window<T> overParam, Function<T, F> field) {
        return fi2Frame(overMinValue(overParam,field),setFunction);
    }

    @Override
    public <F extends Comparable<? super F>> SDFrameImpl<T> overMinValueS(SetFunction<T, F> setFunction, Function<T, F> field) {
        return overMinValueS(setFunction, emptyWindow,field);
    }

    @Override
    public SDFrameImpl<FI2<T, Integer>> overCount(Window<T> overParam) {
        return returnDF(windowFunctionForCount(overParam));
    }

    @Override
    public SDFrameImpl<FI2<T, Integer>> overCount() {
        return overCount(emptyWindow);
    }

    @Override
    public SDFrameImpl<T> overCountS(SetFunction<T, Integer> setFunction, Window<T> overParam) {
        return fi2Frame(overCount(overParam),setFunction);
    }

    @Override
    public SDFrameImpl<T> overCountS(SetFunction<T, Integer> setFunction) {
        return overCountS(setFunction, emptyWindow);
    }


    @Override
    public SDFrameImpl<FI2<T, Integer>> overNtile(int n) {
        return overNtile(emptyWindow, n);
    }

    @Override
    public SDFrameImpl<FI2<T, Integer>> overNtile(Window<T> overParam, int n) {
        return returnDF(windowFunctionForNtile(overParam,n));
    }

    @Override
    public SDFrameImpl<T> overNtileS(SetFunction<T, Integer> setFunction, Window<T> overParam, int n) {
        return fi2Frame(overNtile(overParam,n),setFunction);
    }

    @Override
    public SDFrameImpl<T> overNtileS(SetFunction<T, Integer> setFunction, int n) {
        return overNtileS(setFunction, emptyWindow,n);
    }

    @Override
    public <G, C> SDFrameImpl<T> replenish(Function<T, G> groupDim, Function<T, C> collectDim, List<C> allDim, ReplenishFunction<G, C, T> getEmptyObject) {
        return returnDF(replenish(toLists(),groupDim,collectDim,allDim,getEmptyObject));
    }

    @Override
    public <C> SDFrameImpl<T> replenish(Function<T, C> collectDim, List<C> allDim, Function<C, T> getEmptyObject) {
        return returnDF(replenish(toLists(),collectDim,allDim,getEmptyObject));
    }

    @Override
    public <G, C> SDFrameImpl<T> replenish(Function<T, G> groupDim, Function<T, C> collectDim, ReplenishFunction<G, C, T> getEmptyObject) {
        return returnDF(replenish(toLists(),groupDim,collectDim,getEmptyObject));
    }

    protected SDFrameImpl<T> returnThis(Stream<T> stream) {
        this.data = stream;
        return this;
    }

    protected SDFrameImpl<T> returnThis(List<T> dataList) {
        this.data = dataList.stream();
        return this;
    }

    protected <R> SDFrameImpl<R> returnDF(List<R> dataList) {
        SDFrameImpl<R> frame = new SDFrameImpl<>(dataList);
        transmitMember(this,frame);
        return frame;
    }
    
    
}
