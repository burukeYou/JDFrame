package io.github.burukeyou.dataframe.iframe;


import io.github.burukeyou.dataframe.iframe.item.FI2;
import io.github.burukeyou.dataframe.iframe.item.FI3;
import io.github.burukeyou.dataframe.iframe.item.FI4;
import io.github.burukeyou.dataframe.iframe.support.DefaultJoin;
import io.github.burukeyou.dataframe.iframe.support.Join;
import io.github.burukeyou.dataframe.iframe.support.JoinOn;
import io.github.burukeyou.dataframe.iframe.support.NumberFunction;
import io.github.burukeyou.dataframe.util.CollectorsPlusUtil;
import io.github.burukeyou.dataframe.util.MathUtils;
import io.github.burukeyou.dataframe.util.PartitionList;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;


/**
 * @author caizhihao
 */
public class JDFrameImpl<T> extends AbstractDataFrameImpl<T> implements JDFrame<T> {

    public List<T> dataList;

    public JDFrameImpl(List<T> list) {
        dataList = list;
        if (dataList != null && !dataList.isEmpty()){
            fieldList = buildFieldList(dataList.get(0));
        }
    }

    @Override
    public  Stream<T> stream(){
        return dataList.stream();
    }

    @Override
    public <R> JDFrameImpl<R> from(Stream<R> stream){
        return from(stream.collect(toList()));
    }

    public <R> JDFrameImpl<R> from(List<R> list) {
        return new JDFrameImpl<>(list);
    }

    @Override
    public <R> JDFrame<R> map(Function<T, R> map) {
        return from(stream().map(map));
    }

    @Override
    public <R extends Number> JDFrame<T> mapPercent(Function<T, R> get, SetFunction<T, BigDecimal> set) {
        return mapPercent(get,set,2);
    }

    @Override
    public <R extends Number> JDFrame<T> mapPercent(Function<T, R> get, SetFunction<T, BigDecimal> set, int scale) {
        toLists().forEach(e -> {
            R value = get.apply(e);
            BigDecimal percentageValue = MathUtils.percentage(MathUtils.toBigDecimal(value), scale);
            set.accept(e,percentageValue);
        });
        return this;
    }

    @Override
    public JDFrame<List<T>> partition(int n) {
        return from(new PartitionList<>(toLists(), n));
    }


    @Override
    public JDFrame<T> append(T t) {
        toLists().add(t);
        return this;
    }

    @Override
    public JDFrame<T> union(IFrame<T> other) {
        if (other.count() <= 0){
            return this;
        }
        toLists().addAll(other.toLists());
        return this;
    }

    @Override
    public <R, K> JDFrame<R> join(IFrame<K> other, JoinOn<T, K> on, Join<T, K, R> join) {
        return from(joinList(other,on,join));
    }

    @Override
    public <R, K> JDFrame<R> join(IFrame<K> other, JoinOn<T, K> on) {
        return join(other,on,new DefaultJoin<>());
    }

    @Override
    public <R, K> JDFrame<R> leftJoin(IFrame<K> other, JoinOn<T, K> on, Join<T, K, R> join) {
        return from(leftJoinList(other,on,join));
    }

    @Override
    public <R, K> JDFrame<R> leftJoin(IFrame<K> other, JoinOn<T, K> on) {
        return leftJoin(other,on,new DefaultJoin<>());
    }

    @Override
    public <R, K> JDFrame<R> rightJoin(IFrame<K> other, JoinOn<T, K> on, Join<T, K, R> join) {
        return from(rightJoinList(other,on,join));
    }

    @Override
    public <R, K> JDFrame<R> rightJoin(IFrame<K> other, JoinOn<T, K> on) {
        return rightJoin(other,on,new DefaultJoin<>());
    }

    @Override
    public JDFrame<FI2<T, Integer>> addSortNoCol() {
        List<FI2<T, Integer>> result = new ArrayList<>();
        int index = 1;
        for (T t : this) {
            result.add(new FI2<>(t,index++));
        }
        return from(result);
    }

    @Override
    public JDFrame<FI2<T, Integer>> addSortNoCol(Comparator<T> comparator) {
        return sortAsc(comparator).addSortNoCol();
    }

    @Override
    public <R extends Comparable<R>> JDFrame<FI2<T, Integer>> addSortNoCol(Function<T, R> function) {
        return addSortNoCol(Comparator.comparing(function));
    }

    @Override
    public JDFrame<T> addSortNoCol(SetFunction<T, Integer> set) {
        int index = 0;
        for (T t : this) {
            set.accept(t,index++);
        }
        return this;
    }

    @Override
    public JDFrame<FI2<T, Integer>> addRankingSameCol(Comparator<T> comparator) {
        return from(rankingSameAsc(toLists(),comparator));
    }

    @Override
    public <R extends Comparable<R>> JDFrame<FI2<T, Integer>> addRankingSameColAsc(Function<T, R> function) {
        return addRankingSameCol(Comparator.comparing(function));
    }

    @Override
    public JDFrame<T> addRankingSameCol(Comparator<T> comparator, SetFunction<T, Integer> set) {
        List<FI2<T, Integer>> tmpList = rankingSameAsc(toLists(), comparator);
        for (FI2<T, Integer> p : tmpList) {
            set.accept(p.getC1(),p.getC2());
        }
        return this;
    }

    @Override
    public <R extends Comparable<R>> JDFrame<T> addRankingSameColAsc(Function<T, R> function, SetFunction<T, Integer> set) {
        return addRankingSameCol(Comparator.comparing(function),set);
    }

    @Override
    public <R extends Comparable<R>> JDFrame<T> addRankingSameColDesc(Function<T, R> function, SetFunction<T, Integer> set) {
        return addRankingSameCol(Comparator.comparing(function).reversed(),set);
    }


    public List<T> toLists() {
        return dataList;
    }

    /**
     * ===========================   排序相关  =====================================
     **/

    @Override
    public JDFrameImpl<T> sortDesc(Comparator<T> comparator) {
        return from(stream().sorted(comparator.reversed()));
    }

    @Override
    public <R extends Comparable<R>> JDFrameImpl<T> sortDesc(Function<T, R> function) {
        return sortDesc(Comparator.comparing(function));
    }

    @Override
    public JDFrameImpl<T> sortAsc(Comparator<T> comparator) {
        return from(stream().sorted(comparator));
    }

    @Override
    public <R extends Comparable<R>> JDFrameImpl<T> sortAsc(Function<T, R> function) {
        return sortAsc(Comparator.comparing(function));
    }

    @Override
    public JDFrame<T> cutRankingSameAsc(Comparator<T> comparator, int n) {
        List<FI2<T, Integer>> tmpList = rankingSameAsc(toLists(), comparator, n);
        return from(tmpList.stream().map(FI2::getC1).collect(toList()));
    }

    @Override
    public <R extends Comparable<R>> JDFrame<T> cutRankingSameAsc(Function<T, R> function, int n) {
        return this.cutRankingSameAsc(Comparator.comparing(function),n);
    }

    @Override
    public JDFrame<T> cutRankingSameDesc(Comparator<T> comparator, int n) {
        return this.cutRankingSameAsc(comparator.reversed(), n);
    }

    @Override
    public <R extends Comparable<R>> JDFrame<T> cutRankingSameDesc(Function<T, R> function, int n) {
        return this.cutRankingSameDesc(Comparator.comparing(function),n);
    }

    /** ===========================   截取相关  ===================================== **/

    /**
     * 截取前n个
     */
    @Override
    public JDFrameImpl<T> cutFirst(int n) {
        DFList<T> first = new DFList<>(toLists()).first(n);
        return from(first.build());
    }


    @Override
    public JDFrameImpl<T> cutLast(int n) {
        DFList<T> first = new DFList<>(toLists()).last(n);
        return from(first.build());
    }



    @Override
    public JDFrame<T> distinct() {
        return returnDF(stream().distinct());
    }

    @Override
    public <R extends Comparable<R>> JDFrame<T> distinct(Function<T, R> function) {
        return distinct(Comparator.comparing(function));
    }

    @Override
    public <R extends Comparable<R>> JDFrame<T> distinct(Comparator<T> comparator) {
        ArrayList<T> tmp = stream().collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparator)), ArrayList::new));
        return returnDF(tmp);
    }

    @Override
    public JDFrame<T> where(Predicate<? super T> predicate) {
        return from(stream().filter(predicate));
    }

    @Override
    public long countDistinct(Comparator<T> comparator) {
        return distinct(comparator).count();
    }

    @Override
    public <R extends Comparable<R>> long countDistinct(Function<T, R> function) {
        return this.countDistinct(Comparator.comparing(function));
    }

    /**
     * ===========================   筛选相关  =====================================
     **/
    @Override
    public <R> JDFrame<T> whereNull(Function<T, R> function) {
        return returnDF(whereNullStream(function));
    }

    public <R> JDFrame<T> whereNotNull(Function<T, R> function) {
        return returnDF(whereNotNullStream(function));
    }

    public <R extends Comparable<R>> JDFrame<T> whereBetween(Function<T, R> function, R start, R end) {
        if (start == null && end == null) {
            return this;
        }
        return returnDF(whereBetweenStream(function,start,end));
    }

    @Override
    public <R extends Comparable<R>> JDFrame<T> whereBetweenN(Function<T, R> function, R start, R end) {
        if (start == null && end == null) {
            return this;
        }
        return returnDF(whereBetweenNStream(function,start,end));
    }


    public <R extends Comparable<R>> JDFrame<T> whereBetweenR(Function<T, R> function, R start, R end) {
        if (start == null && end == null) {
            return this;
        }
        return returnDF(whereBetweenRStream(function,start,end));
    }

    @Override
    public <R extends Comparable<R>> JDFrame<T> whereBetweenL(Function<T, R> function, R start, R end) {
        if (start == null && end == null) {
            return this;
        }
        return returnDF(whereBetweenLStream(function,start,end));
    }


    public <R extends Comparable<R>> JDFrame<T> whereNotBetween(Function<T, R> function, R start, R end) {
        if (start == null || end == null) {
            return this;
        }
        return returnDF(whereNotBetweenStream(function,start,end));
    }

    @Override
    public <R extends Comparable<R>> JDFrame<T> whereNotBetweenN(Function<T, R> function, R start, R end) {
        if (start == null || end == null) {
            return this;
        }
        return returnDF(whereNotBetweenNStream(function,start,end));
    }

    public <R> JDFrame<T> whereIn(Function<T, R> function, List<R> list) {
        if (list == null || list.isEmpty()) {
            return this;
        }
        return returnDF(whereInStream(function,list));
    }


    public <R> JDFrame<T> whereNotIn(Function<T, R> function, List<R> list) {
        if (list == null || list.isEmpty()) {
            return this;
        }
        return returnDF(whereNotInStream(function,list));
    }

    public JDFrame<T> whereTrue(Predicate<T> predicate) {
        return returnDF(stream().filter(predicate));
    }


    public JDFrame<T> whereNotTrue(Predicate<T> predicate) {
        return whereTrue(predicate.negate());
    }


    public <R> JDFrame<T> whereEq(Function<T, R> function, R value) {
        if (null == value) {
            return this;
        }
        return  returnDF(whereEqStream(function,value));
    }


    public <R> JDFrame<T> whereNotEq(Function<T, R> function, R value) {
        if (value == null) {
            return this;
        }
        return returnDF(whereNotEqStream(function,value));
    }


    public <R extends Comparable<R>> JDFrame<T> whereGt(Function<T, R> function, R value) {
        if (value == null) {
            return this;
        }
        return returnDF(whereGtStream(function,value));
    }


    public <R extends Comparable<R>> JDFrame<T> whereGe(Function<T, R> function, R value) {
        if (value == null) {
            return this;
        }
        return returnDF(whereGeStream(function,value));
    }


    public <R extends Comparable<R>> JDFrame<T> whereLt(Function<T, R> function, R value) {
        if (value == null) {
            return this;
        }
        return returnDF(whereLtStream(function,value));
    }


    public <R extends Comparable<R>> JDFrame<T> whereLe(Function<T, R> function, R value) {
        if (value == null) {
            return this;
        }
        return returnDF(whereLeStream(function,value));
    }


    public <R> JDFrame<T> whereLike(Function<T, R> function, R value) {
        if (value == null) {
            return this;
        }
        return returnDF(whereLikeStream(function,value));
    }


    public <R> JDFrame<T> whereNotLike(Function<T, R> function, R value) {
        if (value == null) {
            return this;
        }
        return returnDF(whereNotLikeStream(function,value));
    }


    public <R> JDFrame<T> whereLikeLeft(Function<T, R> function, R value) {
        if (value == null) {
            return this;
        }
        return returnDF(whereLikeLeftStream(function,value));
    }


    public <R> JDFrame<T> whereLikeRight(Function<T, R> function, R value) {
        if (value == null) {
            return this;
        }
        return returnDF(whereLikeRightStream(function,value));
    }

    @Override
    public <K> JDFrame<FI2<K, List<T>>> group(Function<T, K> key) {
        return returnDF(groupKey(key));
    }



    /** ===========================   分组相关  ===================================== **/


    public <K,R extends Number> JDFrame<FI2<K, BigDecimal>> groupBySum(Function<T, K> key,
                                                      NumberFunction<T,R> value) {
        Collector<T, ?, BigDecimal> tBigDecimalCollector = CollectorsPlusUtil.summingBigDecimalForNumber(value);
        List<FI2<K, BigDecimal>> collect = groupKey(key, tBigDecimalCollector);
        return returnDF(collect);
    }


    public <K, J,R extends Number> JDFrame<FI3<K, J, BigDecimal>> groupBySum(Function<T, K> key,
                                                            Function<T, J> key2,
                                                            NumberFunction<T,R> value) {
        Collector<T, ?, BigDecimal> tBigDecimalCollector = CollectorsPlusUtil.summingBigDecimalForNumber(value);
        List<FI3<K, J, BigDecimal>> collect = groupKey(key, key2, tBigDecimalCollector);
        return returnDF(collect);
    }



    public <K, J, H,R extends Number> JDFrame<FI4<K, J, H, BigDecimal>> groupBySum(Function<T, K> key,
                                                                  Function<T, J> key2,
                                                                  Function<T, H> key3,
                                                                  NumberFunction<T,R> value) {
        Collector<T, ?, BigDecimal> tBigDecimalCollector = CollectorsPlusUtil.summingBigDecimalForNumber(value);
        List<FI4<K, J, H, BigDecimal>> collect = groupKey(key, key2, key3, tBigDecimalCollector);
        return returnDF(collect);
    }


    public <K> JDFrame<FI2<K, Long>> groupByCount(Function<T, K> key) {
        Collector<Object, ?, Long> counting = counting();
        Map<K, Long> collect = stream().collect(groupingBy(key, counting));
        return returnDF(convertToDataFrameItem2(collect));
    }


    public <K, J> JDFrame<FI3<K, J, Long>> groupByCount(Function<T, K> key,
                                                        Function<T, J> key2) {
        Collector<Object, ?, Long> counting = counting();
        Map<K, Map<J, Long>> collect = stream().collect(groupingBy(key, groupingBy(key2, counting)));
        return returnDF(convertToDataFrameItem3(collect));
    }


    public <K, J, H> JDFrame<FI4<K, J, H, Long>> groupByCount(Function<T, K> key,
                                                              Function<T, J> key2,
                                                              Function<T, H> key3) {
        Collector<Object, ?, Long> counting = counting();
        Map<K, Map<J, Map<H, Long>>> collect = stream().collect(groupingBy(key, groupingBy(key2, groupingBy(key3, counting))));
        return returnDF(convertToDataFrameItem4(collect));
    }


    public <K,R extends Number> JDFrame<FI3<K, BigDecimal,Long>> groupBySumCount(Function<T, K> key, NumberFunction<T,R> value) {
        List<T> dataList = toLists();
        Collector<T, ?, BigDecimal> tBigDecimalCollector = CollectorsPlusUtil.summingBigDecimalForNumber(value);
        List<FI2<K, BigDecimal>> sumList = returnDF(dataList).groupKey(key, tBigDecimalCollector);
        List<FI2<K, Long>> countList =  from(dataList).groupByCount(key).toLists();
        Map<K, Long> countMap = countList.stream().collect(toMap(FI2::getC1, FI2::getC2));
        List<FI3<K, BigDecimal, Long>> collect = sumList.stream().map(e -> new FI3<>(e.getC1(), e.getC2(), countMap.get(e.getC1()))).collect(Collectors.toList());
        return returnDF(collect);
    }


    public <K, J,R extends Number> JDFrame<FI4<K, J, BigDecimal, Long>> groupBySumCount(Function<T, K> key,
                                                                       Function<T, J> key2,
                                                                       NumberFunction<T,R> value) {
        List<T> dataList = toLists();
        Collector<T, ?, BigDecimal> tBigDecimalCollector = CollectorsPlusUtil.summingBigDecimalForNumber(value);
        List<FI3<K, J, BigDecimal>> sumList = returnDF(dataList).groupKey(key, key2, tBigDecimalCollector);
        List<FI3<K, J, Long>> countList =  from(dataList).groupByCount(key, key2).toLists();
        // 合并sum和count字段
        Map<String, FI3<K, J, Long>> countMap = countList.stream().collect(toMap(e -> e.getC1() + "_" + e.getC2(), Function.identity()));
        List<FI4<K, J, BigDecimal, Long>> collect = sumList.stream().map(e -> {
            FI3<K, J, Long> countItem = countMap.get(e.getC1() + "_" + e.getC2());
            return new FI4<>(e.getC1(), e.getC2(), e.getC3(), countItem.getC3());
        }).collect(Collectors.toList());
        return returnDF(collect);
    }


    public <K,R extends Number> JDFrame<FI2<K, BigDecimal>> groupByAvg(Function<T, K> key,
                                                      NumberFunction<T,R> value) {
        Collector<T, ?, BigDecimal> tBigDecimalCollector = CollectorsPlusUtil.averagingBigDecimal(value, 2, BigDecimal.ROUND_HALF_UP);
        List<FI2<K, BigDecimal>> collect = groupKey(key, tBigDecimalCollector);
        return returnDF(collect);
    }


    public <K, J,R extends Number> JDFrame<FI3<K, J, BigDecimal>> groupByAvg(Function<T, K> key,
                                                            Function<T, J> key2,
                                                            NumberFunction<T,R> value) {

        Collector<T, ?, BigDecimal> tBigDecimalCollector = CollectorsPlusUtil.averagingBigDecimal(value, 2, BigDecimal.ROUND_HALF_UP);
        List<FI3<K, J, BigDecimal>> collect = groupKey(key, key2, tBigDecimalCollector);
        return returnDF(collect);
    }


    public <K, J, H,R extends Number> JDFrame<FI4<K, J, H, BigDecimal>> groupByAvg(Function<T, K> key,
                                                                  Function<T, J> key2,
                                                                  Function<T, H> key3,
                                                                  NumberFunction<T,R> value) {
        Collector<T, ?, BigDecimal> tBigDecimalCollector = CollectorsPlusUtil.averagingBigDecimal(value, 2, BigDecimal.ROUND_HALF_UP);
        List<FI4<K, J, H, BigDecimal>> collect = groupKey(key, key2, key3, tBigDecimalCollector);
        return returnDF(collect);
    }



    public <K, V extends Comparable<V>> JDFrame<FI2<K, T>> groupByMax(Function<T, K> key,
                                                                      Function<T, V> value) {
        Map<K, T> collect = stream().collect(groupingBy(key, collectingAndThen(toList(), getListMaxFunction(value))));
        return returnDF(convertToDataFrameItem2(collect));
    }



    @Override
    public <K,J, V extends Comparable<V>> JDFrame<FI3<K,J,T>> groupByMax(Function<T, K> key, Function<T, J> key2, Function<T, V> value) {
        Map<K, Map<J, T>> collect = groupToMap(key, key2,getListMaxFunction(value));
        return returnDF(convertToDataFrameItem3(collect));
    }


    @Override
    public <K, V extends Comparable<V>> JDFrame<FI2<K, V>> groupByMaxValue(Function<T, K> key, Function<T, V> value) {
        return groupByMax(key, value).map(e -> new FI2<>(e.getC1(), value.apply(e.getC2())));
    }

    @Override
    public <K, J, V extends Comparable<V>> JDFrame<FI3<K, J, V>> groupByMaxValue(Function<T, K> key, Function<T, J> key2, Function<T, V> value) {
        return groupByMax(key, key2,value).map(e -> new FI3<>(e.getC1(),e.getC2(),value.apply(e.getC3())));
    }


    public <K, V extends Comparable<V>> JDFrame<FI2<K, T>> groupByMin(Function<T, K> key,
                                                                      Function<T, V> value) {
        Map<K, T> collect = stream().collect(groupingBy(key, collectingAndThen(toList(), getListMinFunction(value))));
        return returnDF(convertToDataFrameItem2(collect));
    }

    @Override
    public <K, J, V extends Comparable<V>> JDFrame<FI3<K, J, T>> groupByMin(Function<T, K> key, Function<T, J> key2, Function<T, V> value) {
        Map<K, Map<J, T>> collect = groupToMap(key, key2,getListMinFunction(value));
        return returnDF(convertToDataFrameItem3(collect));
    }

    @Override
    public <K, V extends Comparable<V>> JDFrame<FI2<K, V>> groupByMinValue(Function<T, K> key, Function<T, V> value) {
        return groupByMin(key, value).map(e -> new FI2<>(e.getC1(), value.apply(e.getC2())));
    }

    @Override
    public <K, J, V extends Comparable<V>> JDFrame<FI3<K, J, V>> groupByMinValue(Function<T, K> key, Function<T, J> key2, Function<T, V> value) {
        return  groupByMin(key, key2,value).map(e -> new FI3<>(e.getC1(),e.getC2(),value.apply(e.getC3())));
    }


    public <K, V extends Comparable<V>> JDFrame<FI2<K, MaxMin<V>>> groupByMaxMinValue(Function<T, K> key,
                                                                                      Function<T, V> value) {
        Map<K, MaxMin<V>> map = stream().collect(groupingBy(key, collectingAndThen(toList(), getListGroupMaxMinValueFunction(value))));
        return returnDF(convertToDataFrameItem2(map));
    }


    public <K, J, V extends Comparable<V>> JDFrame<FI3<K, J, MaxMin<V>>> groupByMaxMinValue(Function<T, K> key,
                                                                                            Function<T, J> key2,
                                                                                            Function<T, V> value) {
        Map<K, Map<J, MaxMin<V>>> map = stream().collect(groupingBy(key, groupingBy(key2, collectingAndThen(toList(), getListGroupMaxMinValueFunction(value)))));
        return returnDF(convertToDataFrameItem3(map));
    }


    public <K, V extends Comparable<V>> JDFrame<FI2<K, MaxMin<T>>> groupByMaxMin(Function<T, K> key,
                                                                                 Function<T, V> value) {
        Map<K, MaxMin<T>> map = stream().collect(groupingBy(key, collectingAndThen(toList(), getListGroupMaxMinFunction(value))));
        return returnDF(convertToDataFrameItem2(map));
    }


    public <K, J, V extends Comparable<V>> JDFrame<FI3<K, J, MaxMin<T>>> groupByMaxMin(Function<T, K> key,
                                                                                       Function<T, J> key2,
                                                                                       Function<T, V> value) {
        Map<K, Map<J, MaxMin<T>>> map = stream().collect(groupingBy(key, groupingBy(key2, collectingAndThen(toList(), getListGroupMaxMinFunction(value)))));
        return returnDF(convertToDataFrameItem3(map));
    }

    private <V extends Comparable<V>> Function<List<T>, MaxMin<V>> getListGroupMaxMinValueFunction(Function<T, V> value) {
        return list -> {
            if (list == null || list.isEmpty()) {
                return null;
            }
            MaxMin<V> maxMin = new MaxMin<>();
            maxMin.setMax(list.stream().max(Comparator.comparing(value)).map(value).orElse(null));
            maxMin.setMin(list.stream().min(Comparator.comparing(value)).map(value).orElse(null));
            return maxMin;
        };
    }

    private <V extends Comparable<V>> Function<List<T>, MaxMin<T>> getListGroupMaxMinFunction(Function<T, V> value) {
        return list -> {
            if (list == null || list.isEmpty()) {
                return new MaxMin<>();
            }
            MaxMin<T> maxMin = new MaxMin<>();
            maxMin.setMax(list.stream().max(Comparator.comparing(value)).orElse(null));
            maxMin.setMin(list.stream().min(Comparator.comparing(value)).orElse(null));
            return maxMin;
        };
    }

    protected <R> JDFrame<R> returnDF(Stream<R> stream) {
        return from(stream);
    }

    protected <R> JDFrameImpl<R> returnDF(List<R> dataList) {
        return from(dataList);
    }
}
